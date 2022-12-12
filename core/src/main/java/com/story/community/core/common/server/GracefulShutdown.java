package com.story.community.core.common.server;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.boot.web.server.GracefulShutdownCallback;
import org.springframework.boot.web.server.GracefulShutdownResult;

import lombok.extern.log4j.Log4j2;
import reactor.netty.DisposableServer;

@Log4j2
public class GracefulShutdown {

    private final Supplier<DisposableServer> disposableServer;

    private volatile Thread shutdownThread;

    private volatile boolean shuttingDown;

    GracefulShutdown(Supplier<DisposableServer> disposableServer) {
        this.disposableServer = disposableServer;
    }

    void shutDownGracefully(GracefulShutdownCallback callback) {
        DisposableServer server = this.disposableServer.get();
        if (server == null) {
            return;
        }
        log.info("Commencing graceful shutdown. Waiting for active requests to complete");
        this.shutdownThread = new Thread(() -> doShutdown(callback, server), "netty-shutdown");
        this.shutdownThread.start();
    }

    private void doShutdown(GracefulShutdownCallback callback, DisposableServer server) {
        this.shuttingDown = true;
        try {
            server.disposeNow(Duration.ofNanos(Long.MAX_VALUE));
            log.info("Graceful shutdown complete");
            callback.shutdownComplete(GracefulShutdownResult.IDLE);
        } catch (Exception ex) {
            log.info("Graceful shutdown aborted with one or more requests still active");
            callback.shutdownComplete(GracefulShutdownResult.REQUESTS_ACTIVE);
        } finally {
            this.shutdownThread = null;
            this.shuttingDown = false;
        }
    }

    void abort() {
        Thread shutdown = this.shutdownThread;
        if (shutdown != null) {
            while (!this.shuttingDown) {
                sleep(50);
            }
            this.shutdownThread.interrupt();
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
