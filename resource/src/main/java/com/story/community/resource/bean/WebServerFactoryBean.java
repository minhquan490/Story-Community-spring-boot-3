package com.story.community.resource.bean;

import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyRouteProvider;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorResourceFactory;

import com.story.community.core.common.server.Http3WebServerFactory;

@Configuration
public class WebServerFactoryBean {

    @Value("${server.port}")
    private int port;

    @Bean(name = "nettyReactiveWebServerFactory")
    @Primary
    ReactiveWebServerFactory webServerFactory(ReactorResourceFactory resourceFactory,
            ObjectProvider<NettyRouteProvider> routes, ObjectProvider<NettyServerCustomizer> serverCustomizers) {
        Http3WebServerFactory factory = new Http3WebServerFactory();
        factory.setResourceFactory(resourceFactory);
        routes.orderedStream().forEach(factory::addRouteProviders);
        factory.getServerCustomizers().addAll(serverCustomizers.orderedStream().collect(Collectors.toList()));
        factory.setPort(port);
        return factory;
    }
}
