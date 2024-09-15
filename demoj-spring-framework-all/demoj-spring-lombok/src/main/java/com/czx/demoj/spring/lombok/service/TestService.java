package com.czx.demoj.spring.lombok.service;

import com.czx.demoj.spring.lombok.anno.Audited;
import com.czx.demoj.spring.lombok.entity.ImmutableAuditedEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TestService {

    @NonNull
    @Audited
    public ImmutableAuditedEntity logThenReturn(ImmutableAuditedEntity entity)
            throws JsonProcessingException {
        return entity;
    }

    @Audited
    public void doNothing() {
    }

}
