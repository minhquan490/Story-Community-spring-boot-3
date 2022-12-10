package com.story.community.core.resource.entities.customer;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.story.community.core.common.annotation.CacheStorage;
import com.story.community.core.common.annotation.GeneratedValue;
import com.story.community.core.resource.entities.AbstractEntity;
import com.story.community.core.resource.entities.manga.Manga;
import com.story.community.core.resource.entities.music.Music;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "customer", language = "vi")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@CacheStorage(name = "customer")
public class Customer extends AbstractEntity {

    @Id
    @Field(name = "_id", targetType = FieldType.STRING)
    @GeneratedValue
    private String id;

    @Field(name = "first_name", targetType = FieldType.STRING)
    private String firstName;

    @Field(name = "last_name", targetType = FieldType.STRING)
    private String lastName;

    @Indexed(unique = true, name = "idx_customer_email")
    @Field(name = "email", targetType = FieldType.STRING)
    private String email;

    @Field(name = "avatar_url", targetType = FieldType.STRING)
    private String avatarUrl;

    @Field(name = "gender")
    private Gender gender;

    @Field(name = "birth_date", targetType = FieldType.DATE_TIME)
    private LocalDate birthDate;

    @Field(name = "role")
    private Role role;

    @Indexed(unique = true, name = "idx_customer_refresh-token")
    @Field(name = "refresh_token", targetType = FieldType.STRING)
    private String refreshToken;

    @Field(name = "music")
    @ReadOnlyProperty
    @DocumentReference(lazy = true, lookup = "{'owner':?#{#self._id}}")
    @JsonIgnore
    private List<Music> musics;

    @Field(name = "manga")
    @ReadOnlyProperty
    @DocumentReference(lazy = true, lookup = "{'owner':?#{#self._id}}")
    @JsonIgnore
    private List<Manga> mangas;

    @Field(name = "account")
    @DocumentReference(lazy = true, lookup = "{'owner:?#{self._id}'}")
    @ReadOnlyProperty
    private Account account;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
