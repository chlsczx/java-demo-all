package com.czx.demo.spring.boot.dockerCompose.entity;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Alias("user")
public class User {
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private Integer age;

    public User(Integer id, @NonNull String name, @NonNull Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
