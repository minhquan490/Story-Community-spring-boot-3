package com.story.community.core.resource.entities.manga;

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
import com.story.community.core.resource.entities.customer.Customer;
import com.story.community.core.resource.generator.MangaIdGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "manga", language = "vi")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@CacheStorage(name = "manga")
public class Manga extends AbstractEntity {

    @Field(name = "_id", targetType = FieldType.STRING)
    @Id
    @GeneratedValue(generator = MangaIdGenerator.class)
    private String id;

    @Field(name = "name", targetType = FieldType.STRING)
    @Indexed(unique = true, name = "idx_manga_name")
    private String name;

    @Field(name = "author", targetType = FieldType.STRING)
    private String author;

    @Field(name = "status")
    private Status status;

    @Field(name = "view", targetType = FieldType.INT32)
    private int view;

    @Field(name = "summary", targetType = FieldType.STRING)
    private String summary;

    @Field(name = "is_hot", targetType = FieldType.BOOLEAN)
    private boolean isHot;

    @Field(name = "owner")
    @DocumentReference(lazy = true)
    private Customer owner;

    @Field(name = "categories")
    @DocumentReference(lazy = true, lookup = "{'mangas' :?#{#self._id}}")
    private List<MangaCategory> categories;

    @Field(name = "chapters")
    @DocumentReference(lazy = true, lookup = "{'manga' : ?#{#self._id}}")
    private List<MangaChapter> chapters;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
