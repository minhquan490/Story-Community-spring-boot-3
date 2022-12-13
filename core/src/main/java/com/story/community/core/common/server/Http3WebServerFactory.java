package com.story.community.core.common.server;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.web.embedded.netty.NettyRouteProvider;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.boot.web.reactive.server.AbstractReactiveWebServerFactory;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.boot.web.server.WebServer;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.util.Assert;

import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;

public class Http3WebServerFactory extends AbstractReactiveWebServerFactory {

    private Set<NettyServerCustomizer> serverCustomizers = new LinkedHashSet<>();

    private List<NettyRouteProvider> routeProviders = new ArrayList<>();

    private Duration lifecycleTimeout;

    private boolean useForwardHeaders;

    private ReactorResourceFactory resourceFactory;

    private Shutdown shutdown;

    @Override
    public WebServer getWebServer(HttpHandler httpHandler) {
        HttpServer httpServer = createHttpServer();
        ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
        Http3WebServer webServer = createHttp3WebServer(httpServer, handlerAdapter, this.lifecycleTimeout,
                getShutdown());
        webServer.setRouteProviders(this.routeProviders);
        webServer.setPort(getPort());
        return webServer;
    }

    Http3WebServer createHttp3WebServer(HttpServer httpServer, ReactorHttpHandlerAdapter handlerAdapter,
            Duration lifecycleTimeout, Shutdown shutdown) {
        return new Http3WebServer(httpServer, handlerAdapter, lifecycleTimeout, shutdown);
    }

    /**
     * Returns a mutable collection of the {@link NettyServerCustomizer}s that will
     * be
     * applied to the Netty server builder.
     * 
     * @return the customizers that will be applied
     */
    public Collection<NettyServerCustomizer> getServerCustomizers() {
        return this.serverCustomizers;
    }

    /**
     * Set {@link NettyServerCustomizer}s that should be applied to the Netty server
     * builder. Calling this method will replace any existing customizers.
     * 
     * @param serverCustomizers the customizers to set
     */
    public void setServerCustomizers(Collection<? extends NettyServerCustomizer> serverCustomizers) {
        Assert.notNull(serverCustomizers, "ServerCustomizers must not be null");
        this.serverCustomizers = new LinkedHashSet<>(serverCustomizers);
    }

    /**
     * Add {@link NettyServerCustomizer}s that should be applied while building the
     * server.
     * 
     * @param serverCustomizers the customizers to add
     */
    public void addServerCustomizers(NettyServerCustomizer... serverCustomizers) {
        Assert.notNull(serverCustomizers, "ServerCustomizer must not be null");
        this.serverCustomizers.addAll(Arrays.asList(serverCustomizers));
    }

    /**
     * Add {@link NettyRouteProvider}s that should be applied, in order, before the
     * handler for the Spring application.
     * 
     * @param routeProviders the route providers to add
     */
    public void addRouteProviders(NettyRouteProvider... routeProviders) {
        Assert.notNull(routeProviders, "NettyRouteProvider must not be null");
        this.routeProviders.addAll(Arrays.asList(routeProviders));
    }

    /**
     * Set the maximum amount of time that should be waited when starting or
     * stopping the
     * server.
     * 
     * @param lifecycleTimeout the lifecycle timeout
     */
    public void setLifecycleTimeout(Duration lifecycleTimeout) {
        this.lifecycleTimeout = lifecycleTimeout;
    }

    /**
     * Set if x-forward-* headers should be processed.
     * 
     * @param useForwardHeaders if x-forward headers should be used
     * @since 2.1.0
     */
    public void setUseForwardHeaders(boolean useForwardHeaders) {
        this.useForwardHeaders = useForwardHeaders;
    }

    /**
     * Set the {@link ReactorResourceFactory} to get the shared resources from.
     * 
     * @param resourceFactory the server resources
     * @since 2.1.0
     */
    public void setResourceFactory(ReactorResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    @Override
    public void setShutdown(Shutdown shutdown) {
        this.shutdown = shutdown;
    }

    @Override
    public Shutdown getShutdown() {
        return this.shutdown;
    }

    private HttpServer createHttpServer() {
        HttpServer server = HttpServer.create();
        server.accessLog(true);
        server.compress(true);
        if (this.resourceFactory != null) {
            LoopResources resources = this.resourceFactory.getLoopResources();
            Assert.notNull(resources, "No LoopResources: is ReactorResourceFactory not initialized yet?");
            server = server.runOn(resources).bindAddress(this::getListenAddress);
        } else {
            server = server.bindAddress(this::getListenAddress);
        }
        if (getSsl() != null && getSsl().isEnabled()) {
            server = customizeSslConfiguration(server);
        }
        if (getCompression() != null && getCompression().getEnabled()) {
            CompressionCustomizer compressionCustomizer = new CompressionCustomizer(getCompression());
            server = compressionCustomizer.apply(server);
        }
        server = server.protocol(listProtocols()).forwarded(this.useForwardHeaders);
        return applyCustomizers(server);
    }

    private HttpServer customizeSslConfiguration(HttpServer httpServer) {
        SslServerCustomizer sslServerCustomizer = new SslServerCustomizer(getSsl(), getHttp2(),
                getOrCreateSslStoreProvider());
        return sslServerCustomizer.apply(httpServer);
    }

    private HttpProtocol[] listProtocols() {
        List<HttpProtocol> protocols = new ArrayList<>();
        protocols.add(HttpProtocol.HTTP11);
        if (getHttp2() != null && getHttp2().isEnabled()) {
            if (getSsl() != null && getSsl().isEnabled()) {
                protocols.add(HttpProtocol.H2);
            } else {
                protocols.add(HttpProtocol.H2C);
            }
        }
        return protocols.toArray(new HttpProtocol[0]);
    }

    private InetSocketAddress getListenAddress() {
        if (getAddress() != null) {
            return new InetSocketAddress(getAddress().getHostAddress(), getPort());
        }
        return new InetSocketAddress(getPort());
    }

    private HttpServer applyCustomizers(HttpServer server) {
        for (NettyServerCustomizer customizer : this.serverCustomizers) {
            server = customizer.apply(server);
        }
        return server;
    }
}
