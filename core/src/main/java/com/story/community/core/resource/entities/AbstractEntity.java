package com.story.community.core.resource.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import com.story.community.core.resource.entities.customer.Customer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractEntity implements Entity {

    @CreatedBy
    @Field(name = "created_by")
    private Customer createBy;

    @CreatedDate
    @Field(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Field(name = "last_modified_by")
    private Customer lastUpdateBy;

    @LastModifiedDate
    @Field(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
