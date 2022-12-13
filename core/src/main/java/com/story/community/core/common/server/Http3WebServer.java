package com.story.community.core.common.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.reactivestreams.Publisher;
import org.springframework.boot.web.embedded.netty.NettyRouteProvider;
import org.springframework.boot.web.server.GracefulShutdownCallback;
import org.springframework.boot.web.server.GracefulShutdownResult;
import org.springframework.boot.web.server.PortInUseException;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.unix.Errors.NativeIoException;
import io.netty.incubator.codec.http3.Http3;
import io.netty.incubator.codec.quic.InsecureQuicTokenHandler;
import io.netty.incubator.codec.quic.QuicSslContext;
import io.netty.incubator.codec.quic.QuicSslContextBuilder;
import io.netty.util.concurrent.DefaultEventExecutor;
import lombok.extern.log4j.Log4j2;
import reactor.core.CoreSubscriber;
import reactor.netty.ChannelBindException;
import reactor.netty.DisposableServer;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.HttpServerRoutes;

@Log4j2
public class Http3WebServer implements WebServer {

    /**
     * Permission denied error code from {@code errno.h}.
     */
    private static final int ERROR_NO_EACCES = -13;

    private static final Predicate<HttpServerRequest> ALWAYS = (request) -> true;

    private final HttpServer httpServer;

    private final BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> handler;

    private final Duration lifecycleTimeout;

    private final GracefulShutdown gracefulShutdown;

    private List<NettyRouteProvider> routeProviders = Collections.emptyList();

    private volatile DisposableServer disposableServer;

    private int port;

    public Http3WebServer(HttpServer httpServer, ReactorHttpHandlerAdapter handlerAdapter, Duration lifecycleTimeout,
            Shutdown shutdown) {
        Assert.notNull(httpServer, "HttpServer must not be null");
        Assert.notNull(handlerAdapter, "HandlerAdapter must not be null");
        this.lifecycleTimeout = lifecycleTimeout;
        this.handler = handlerAdapter;
        this.httpServer = httpServer.channelGroup(new DefaultChannelGroup(new DefaultEventExecutor()));
        this.gracefulShutdown = (shutdown == Shutdown.GRACEFUL) ? new GracefulShutdown(() -> this.disposableServer)
                : null;
    }

    public void setRouteProviders(List<NettyRouteProvider> routeProviders) {
        this.routeProviders = routeProviders;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void start() throws WebServerException {
        if (this.disposableServer == null) {
            try {
                CoreSubscriber<Channel> server = (CoreSubscriber<Channel>) startHttpServer();
                server.onNext(buildHttp3Channel());
                this.disposableServer = (DisposableServer) server;
            } catch (Exception ex) {
                if (ex instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                PortInUseException.ifCausedBy(ex, ChannelBindException.class, (bindException) -> {
                    if (bindException.localPort() > 0 && !isPermissionDenied(bindException.getCause())) {
                        throw new PortInUseException(bindException.localPort(), ex);
                    }
                });
                throw new WebServerException("Unable to start Netty", ex);
            }
            if (this.disposableServer != null) {
                log.info("Netty started" + getStartedOnMessage(this.disposableServer));
            }
            startDaemonAwaitThread(this.disposableServer);
        }
    }

    private String getStartedOnMessage(DisposableServer server) {
        StringBuilder message = new StringBuilder();
        tryAppend(message, "port %s", server::port);
        tryAppend(message, "path %s", server::path);
        return (message.length() > 0) ? " on " + message : "";
    }

    private void tryAppend(StringBuilder message, String format, Supplier<Object> supplier) {
        try {
            Object value = supplier.get();
            message.append((message.length() != 0) ? " " : "");
            message.append(String.format(format, value));
        } catch (UnsupportedOperationException ex) {
            // Ignore
        }
    }

    DisposableServer startHttpServer() {
        HttpServer server = this.httpServer;
        if (this.routeProviders.isEmpty()) {
            server = server.handle(this.handler);
        } else {
            server = server.route(this::applyRouteProviders);
        }
        if (this.lifecycleTimeout != null) {
            return server.bindNow(this.lifecycleTimeout);
        }
        return server.bindNow();
    }

    private boolean isPermissionDenied(Throwable bindExceptionCause) {
        try {
            if (bindExceptionCause instanceof NativeIoException nativeException) {
                return nativeException.expectedErr() == ERROR_NO_EACCES;
            }
        } catch (Exception ignore) {
            // Ignore
        }
        return false;
    }

    @Override
    public void shutDownGracefully(GracefulShutdownCallback callback) {
        if (this.gracefulShutdown == null) {
            callback.shutdownComplete(GracefulShutdownResult.IMMEDIATE);
            return;
        }
        this.gracefulShutdown.shutDownGracefully(callback);
    }

    private void applyRouteProviders(HttpServerRoutes routes) {
        for (NettyRouteProvider provider : this.routeProviders) {
            routes = provider.apply(routes);
        }
        routes.route(ALWAYS, this.handler);
    }

    private void startDaemonAwaitThread(DisposableServer disposableServer) {
        Thread awaitThread = new Thread("server") {

            @Override
            public void run() {
                disposableServer.onDispose().block();
            }

        };
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void stop() throws WebServerException {
        if (this.disposableServer != null) {
            if (this.gracefulShutdown != null) {
                this.gracefulShutdown.abort();
            }
            try {
                if (this.lifecycleTimeout != null) {
                    this.disposableServer.disposeNow(this.lifecycleTimeout);
                } else {
                    this.disposableServer.disposeNow();
                }
            } catch (IllegalStateException ex) {
                // Continue
            }
            this.disposableServer = null;
        }
    }

    @Override
    public int getPort() {
        if (this.disposableServer != null) {
            try {
                return this.disposableServer.port();
            } catch (UnsupportedOperationException ex) {
                return -1;
            }
        }
        return -1;
    }

    private Channel buildHttp3Channel() throws FileNotFoundException, InterruptedException {
        File keyFile = ResourceUtils.getFile("classpath:localhost-key.pem");
        File certChainFile = ResourceUtils.getFile("classpath:localhost.pem");
        List<String> supportProtocol = new ArrayList<>(Arrays.asList(Http3.supportedApplicationProtocols()));
        supportProtocol.add(HttpProtocol.H2.name().toLowerCase());
        supportProtocol.add(HttpProtocol.H2C.name().toLowerCase());
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        QuicSslContext sslContext = QuicSslContextBuilder
                .forServer(keyFile, null, certChainFile)
                .applicationProtocols(supportProtocol.toArray(new String[0])).build();
        ChannelHandler codec = Http3
                .newQuicServerCodecBuilder()
                .sslContext(sslContext)
                .maxIdleTimeout(5000, TimeUnit.MILLISECONDS)
                .initialMaxData(10000000)
                .initialMaxStreamDataBidirectionalLocal(1000000)
                .initialMaxStreamDataBidirectionalRemote(1000000)
                .initialMaxStreamsBidirectional(100)
                .tokenHandler(InsecureQuicTokenHandler.INSTANCE)
                .handler(new CustomRequestStreamInboundHandler(true))
                .build();
        Bootstrap bootstrap = new Bootstrap();
        return bootstrap
                .group(group)
                .channel(NioDatagramChannel.class)
                .handler(codec)
                .bind(new InetSocketAddress(port))
                .sync()
                .channel();
    }

    public void setPort(int port) {
        this.port = port;
    }
}
