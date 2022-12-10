package com.story.community.core.resource.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Chapter extends AbstractEntity {

    @Field(name = "name", targetType = FieldType.STRING)
    @Indexed(unique = true, name = "idx_chapter_name")
    private String name;

    @Field(name = "status")
    private Status status;
}
