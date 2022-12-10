package com.story.community.core.bean.resource;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.CertificateFileSslStoreProvider;
import org.springframework.boot.web.server.Http2;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.story.community.core.common.configuration.CustomNettyServerCustomizer;

@Configuration
public class NettyWebServerBean {

    @Bean
    NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
        Ssl ssl = new Ssl();
        ssl.setEnabled(true);
		ssl.setCertificate("classpath:localhost.pem");
		ssl.setCertificatePrivateKey("classpath:localhost-key.pem");
        Http2 http2 = new Http2();
        http2.setEnabled(true);
        NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory();
        webServerFactory.addServerCustomizers(
                new CustomNettyServerCustomizer(ssl, http2, CertificateFileSslStoreProvider.from(ssl)));
        return webServerFactory;
    }
}
