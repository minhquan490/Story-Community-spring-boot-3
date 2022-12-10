package com.story.community.core.bean.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;

@Configuration
public class MongoTransactionManagerBean {
	private ReactiveMongoDatabaseFactory mongoDatabaseFactory;

	@Autowired
	public void setMongoDatabaseFactory(ReactiveMongoDatabaseFactory mongoDatabaseFactory) {
		this.mongoDatabaseFactory = mongoDatabaseFactory;
	}

    @Bean
    ReactiveMongoTransactionManager transactionManager() {
        return new ReactiveMongoTransactionManager(mongoDatabaseFactory);
    }
}
