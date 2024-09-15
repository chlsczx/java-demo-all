package com.czx.demoj.spring.lombok.controller;

import com.czx.demoj.spring.lombok.LombokTestApp;
import com.czx.demoj.spring.lombok.entity.ImmutableAuditedEntity;
import com.czx.demoj.spring.lombok.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class TestImmutableEntityController {

    ObjectMapper objectMapper = LombokTestApp.objectMapper;

    @Resource
    private TestService testService;

    @RequestMapping(method = RequestMethod.PUT, path = "/test")

    public ResponseEntity<ObjectNode> test(
            @RequestParam(name = "timezone", required = false) String timezone,
            @RequestBody ObjectNode entityNode
    ) throws JsonProcessingException {
        entityNode.set("id", objectMapper.convertValue(123, JsonNode.class));
        ImmutableAuditedEntity entity = objectMapper.convertValue(entityNode, ImmutableAuditedEntity.class);

        log.info("{}", entity);
        log.warn("getCreatedAt: {}", entity.getCreatedAt());
        testService.doNothing();
        return ResponseEntity.ok(objectMapper.valueToTree(testService.logThenReturn(entity)));
    }
}
