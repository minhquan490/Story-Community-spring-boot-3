package com.story.community.core.bean.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.story.community.core.common.Constant;
import com.story.community.core.common.support.ResourceMongoDatabaseFactory;

@Configuration
public class MongoDatabaseFactoryBean {

	@Bean(name = "reactiveMongoDatabaseFactory")
	@Primary
    ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory() {
        MongoClient mongoClient = MongoClients
                .create("mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000");
		return new ResourceMongoDatabaseFactory(mongoClient, Constant.MONGO_DATABASE_NAME);
    }
}
