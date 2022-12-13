package com.story.community.core.common.server;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Http3Address implements Supplier<InetSocketAddress> {

    private final InetSocketAddress address;

    @Override
    public InetSocketAddress get() {
        return address;
    }

}
