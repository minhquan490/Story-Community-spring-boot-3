package com.story.community.core.common.support;

import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

import com.mongodb.reactivestreams.client.MongoClient;

public class ResourceMongoDatabaseFactory extends SimpleReactiveMongoDatabaseFactory {
    private final String databaseName;

    public ResourceMongoDatabaseFactory(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName);
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
