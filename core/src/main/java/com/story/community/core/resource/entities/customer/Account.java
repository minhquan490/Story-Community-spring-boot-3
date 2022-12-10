package com.story.community.core.resource.entities.customer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.story.community.core.common.annotation.CacheStorage;
import com.story.community.core.common.annotation.GeneratedValue;
import com.story.community.core.common.annotation.HashBy;
import com.story.community.core.resource.entities.AbstractEntity;
import com.story.community.core.resource.generator.AccountIdGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "account", language = "vi")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@CacheStorage(name = "account")
public class Account extends AbstractEntity {

    @Field(name = "_id", targetType = FieldType.STRING)
    @Id
    @GeneratedValue(generator = AccountIdGenerator.class)
    private String id;

    @Indexed(unique = true, name = "idx_account_username")
    @Field(name = "username", targetType = FieldType.STRING)
    private String username;

    @Field(name = "password", targetType = FieldType.STRING)
    @HashBy
    private String password;

    @Field(name = "non_expired", targetType = FieldType.BOOLEAN)
    private boolean accountNonExpired;

    @Field(name = "non_locked", targetType = FieldType.BOOLEAN)
    private boolean accountNonLocked;

    @Field(name = "enabled", targetType = FieldType.BOOLEAN)
    private boolean enabled;

    @Field(name = "customer")
    @DocumentReference(lazy = true)
    @JsonIgnore
    private Customer customer;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
