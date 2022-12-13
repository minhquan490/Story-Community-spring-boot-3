package com.story.community.core.common.server;

import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.incubator.quic.QuicInbound;

public interface ServerRequest extends QuicInbound, HttpServerRequest {

}
