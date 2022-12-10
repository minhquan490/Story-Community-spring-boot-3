package com.story.community.core.resource.entities.lightnovel;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.story.community.core.common.annotation.CacheStorage;
import com.story.community.core.common.annotation.GeneratedValue;
import com.story.community.core.resource.entities.AbstractEntity;
import com.story.community.core.resource.entities.Status;
import com.story.community.core.resource.generator.LightnovelIdGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "lightnovel", language = "vi")
@CacheStorage(name = "lightnovel")
public class Lightnovel extends AbstractEntity {

    @Id
    @Field(name = "_id", targetType = FieldType.STRING)
    @GeneratedValue(generator = LightnovelIdGenerator.class)
    private String id;

    @Field(name = "name", targetType = FieldType.STRING)
    @Indexed(unique = true, name = "idx_lightnovel_name")
    private String name;

    @Field(name = "type")
    private Type type;

    @Field(name = "author", targetType = FieldType.STRING)
    private String author;

    @Field(name = "artist", targetType = FieldType.STRING)
    private String artist;

    @Field(name = "status")
    private Status status;

    @Field(name = "character_number", targetType = FieldType.INT32)
    private int characterNumber;

    @Field(name = "view", targetType = FieldType.INT32)
    private int view;

    @Field(name = "total_chapter", targetType = FieldType.INT32)
    private int totalChaper;

    @Field(name = "summary", targetType = FieldType.STRING)
    private String summary;

    @Field(name = "other_names", targetType = FieldType.ARRAY)
    private List<String> otherNames;

    @Field(name = "categories")
    @DocumentReference(lazy = true, lookup = "{'mangas' : ?#{#self._id}}")
    private List<LightnovelCategory> categories;

    @Field(name = "chapters")
    @DocumentReference(lazy = true, lookup = "{'lightnovel' : ?#{#self._id}}")
    private List<LightnovelChapter> chapters;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
