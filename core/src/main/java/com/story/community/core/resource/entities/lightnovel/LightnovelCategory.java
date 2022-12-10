package com.story.community.core.resource.entities.lightnovel;

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
import com.story.community.core.resource.generator.LightnovelCategoryIdGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "lightnovel_category", language = "vi")
@CacheStorage(name = "lightnovelCategory")
public class LightnovelCategory extends Category {

    @Id
    @Field(name = "_id", targetType = FieldType.STRING)
    @GeneratedValue(generator = LightnovelCategoryIdGenerator.class)
    private String id;

    @Field(name = "lightnovels")
    @DocumentReference(lazy = true, lookup = "{'categories' : ?#{#self._id}}")
    @JsonIgnore
    @ReadOnlyProperty
    private List<Lightnovel> lightnovels;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
