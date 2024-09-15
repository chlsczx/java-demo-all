package com.czx.demoj.spring.lombok.entity;

import com.czx.demoj.spring.lombok.LombokTestApp;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;

@Data
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "with", toBuilder = true)
public class BaseEntity {

    @NonNull
    private final Long id;

    @JsonCreator
    public static BaseEntity deserialize(@JsonProperty("id") Long id) {
        return BaseEntity.builder().withId(id).build();
    }

    public String toJsonString() throws JsonProcessingException {
        return LombokTestApp.objectMapper.writeValueAsString(this);
    }
}
