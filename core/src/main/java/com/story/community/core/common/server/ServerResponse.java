package com.story.community.core.common.server;

import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.incubator.quic.QuicOutbound;

public interface ServerResponse extends QuicOutbound, HttpServerResponse {

}
