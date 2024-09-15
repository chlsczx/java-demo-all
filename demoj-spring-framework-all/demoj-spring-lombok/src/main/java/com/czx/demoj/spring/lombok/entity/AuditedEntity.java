package com.czx.demoj.spring.lombok.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "with", toBuilder = true)
public abstract class AuditedEntity extends BaseEntity{

    public abstract void convertToTimezone(ZoneId zoneId);

}
