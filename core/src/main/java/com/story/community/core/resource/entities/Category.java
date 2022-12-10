package com.story.community.core.resource.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Category extends AbstractEntity {

    @Field(name = "name", targetType = FieldType.STRING)
    @Indexed(unique = true, name = "idx_category_name")
    private String name;
}
