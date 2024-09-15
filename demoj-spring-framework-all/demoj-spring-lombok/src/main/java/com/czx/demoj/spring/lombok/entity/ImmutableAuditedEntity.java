package com.czx.demoj.spring.lombok.entity;


import com.czx.demoj.spring.lombok.LombokTestApp;
import com.czx.demoj.spring.lombok.support.jackson.deser.CustomZonedDateTimeDeserializer;
import com.czx.demoj.spring.lombok.support.jackson.ser.ZonedDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@Slf4j
public class ImmutableAuditedEntity extends AuditedEntity {

    @NonNull
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    private ZonedDateTime createdAt;

    @Nullable
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    private ZonedDateTime deletedAt;

    @JsonCreator
    public static ImmutableAuditedEntity deserialize(
            @JsonProperty("id") Long id,
            @JsonProperty("createdAt") ZonedDateTime createdAt,
            @JsonProperty("deletedAt") ZonedDateTime deletedAt
    ) throws JsonProcessingException {
        ImmutableAuditedEntity entity = ImmutableAuditedEntity.builder()
                .withId(id)
                .withCreatedAt(createdAt)
                .withDeletedAt(deletedAt)
                .build();

        log.debug("Deserialization result: {}", LombokTestApp.objectMapper.writeValueAsString(entity));

        return entity;
    }


    @Override
    public void convertToTimezone(ZoneId zoneId) {
        this.createdAt = this.createdAt.withZoneSameInstant(zoneId);
        if (this.deletedAt != null) {
            this.deletedAt = this.deletedAt.withZoneSameInstant(zoneId);
        }
    }
}
