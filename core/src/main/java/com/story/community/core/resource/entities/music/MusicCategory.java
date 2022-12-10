package com.story.community.core.resource.entities.music;

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
import com.story.community.core.resource.entities.Category;
import com.story.community.core.resource.generator.MusicIdGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@CacheStorage(name = "musicCategory")
@Document(collection = "music_category", language = "vi")
public class MusicCategory extends Category {

    @Id
    @Field(name = "_id", targetType = FieldType.STRING)
    @GeneratedValue(generator = MusicIdGenerator.class)
    private String id;

    @Field(name = "musics")
    @DocumentReference(lazy = true, lookup = "{'categories' : ?#{#self._id}}")
    @JsonIgnore
    @ReadOnlyProperty
    private List<Music> musics;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
