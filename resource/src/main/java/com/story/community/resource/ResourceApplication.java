package com.story.community.resource;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication(scanBasePackages = {
		"com.story.community.core.common",
		"com.story.community.core.resource",
		"com.story.community.resource",
		"com.story.community.core.bean.resource"
})
@EnableAsync
@EnableAspectJAutoProxy
@EnableReactiveMongoAuditing(auditorAwareRef = "auditorProvider")
@EnableReactiveMongoRepositories(basePackages = "com.story.community.core.resource.repository")
@EnableScheduling
@EnableWebFlux
@EnableTransactionManagement
@EnableCaching
public class ResourceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ResourceApplication.class)
				.lazyInitialization(true)
				.logStartupInfo(true)
				.build(args)
				.run();
	}

}
