package com.story.community.core.resource.entities.manga;

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
import com.story.community.core.resource.generator.MangaChapterIdGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "manga_chapter", language = "vi")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@CacheStorage(name = "mangaChapter")
public class MangaChapter extends Chapter {

    @Id
    @Field(name = "_id", targetType = FieldType.STRING)
    @GeneratedValue(generator = MangaChapterIdGenerator.class)
    private String id;

    @Field(name = "content_urls", targetType = FieldType.ARRAY)
    private List<String> contentUrls;

    @Field(name = "date_uploaded", targetType = FieldType.DATE_TIME)
    private LocalDate dateUploaded;

    @Field(name = "view", targetType = FieldType.INT32)
    private int view;

    @Field(name = "manga")
    @DocumentReference(lazy = true)
    @ReadOnlyProperty
    @JsonIgnore
    private Manga manga;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
