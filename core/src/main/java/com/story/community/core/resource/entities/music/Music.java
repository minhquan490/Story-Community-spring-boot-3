package com.story.community.core.resource.entities.music;

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
import com.story.community.core.resource.entities.customer.Customer;
import com.story.community.core.resource.generator.MusicIdGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "music", language = "vi")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@CacheStorage(name = "music")
public class Music extends AbstractEntity {

    @Id
    @Field(name = "_id", targetType = FieldType.STRING)
    @GeneratedValue(generator = MusicIdGenerator.class)
    private String id;

    @Field(name = "name", targetType = FieldType.STRING)
    @Indexed(unique = true, name = "idx_music_name")
    private String name;

    @Field(name = "duration", targetType = FieldType.INT32)
    private int duration;

    @Field(name = "size", targetType = FieldType.INT32)
    private int size;

    @Field(name = "bit_rate", targetType = FieldType.STRING)
    private String bitRate;

    @Field(name = "chanel", targetType = FieldType.INT32)
    private int chanel;

    @Field(name = "url", targetType = FieldType.STRING)
    private String url;

    @Field(name = "owner")
    @DocumentReference(lazy = true)
    private Customer owner;

    @Field(name = "categories")
    @DocumentReference(lazy = true, lookup = "{'mangas' : ?#{#self._id}}")
    private List<MusicCategory> categories;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
