package com.story.community.core.bean.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoTemplateBean {
	private ReactiveMongoDatabaseFactory mongoDatabaseFactory;
	private MappingMongoConverter mappingMongoConverter;

	@Autowired
	public void setMappingMongoConverter(MappingMongoConverter mappingMongoConverter) {
		this.mappingMongoConverter = mappingMongoConverter;
	}

	@Autowired
	public void setMongoDatabaseFactory(ReactiveMongoDatabaseFactory mongoDatabaseFactory) {
		this.mongoDatabaseFactory = mongoDatabaseFactory;
	}

	@Bean(name = "reactiveMongoTemplate")
	@Primary
    ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoDatabaseFactory, mappingMongoConverter);
    }
}
