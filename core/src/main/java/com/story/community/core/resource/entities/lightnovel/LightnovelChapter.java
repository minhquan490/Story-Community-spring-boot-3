package com.story.community.core.resource.entities.lightnovel;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.story.community.core.common.annotation.CacheStorage;
import com.story.community.core.common.annotation.GeneratedValue;
import com.story.community.core.resource.entities.Chapter;
import com.story.community.core.resource.generator.LightnovelChapterIdGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "lightnovel_chapter")
@CacheStorage(name = "lightnovelChapter")
public class LightnovelChapter extends Chapter {

    @Field(name = "_id", targetType = FieldType.STRING)
    @Id
    @GeneratedValue(generator = LightnovelChapterIdGenerator.class)
    private String id;

    @Field(name = "number", targetType = FieldType.INT32)
    private int number;

    @Field(name = "date_uploaded", targetType = FieldType.DATE_TIME)
    private LocalDate dateUploaded;

    @Field(name = "illustration_urls", targetType = FieldType.ARRAY)
    private List<String> illustrationUrls;

    @Field(name = "content", targetType = FieldType.STRING)
    private String content;

    @Field(name = "character_number", targetType = FieldType.INT32)
    private int characterNumber;

    @Field(name = "lightnovel")
    @DocumentReference(lazy = true)
    @ReadOnlyProperty
    @JsonIgnore
    private Lightnovel lightnovel;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
