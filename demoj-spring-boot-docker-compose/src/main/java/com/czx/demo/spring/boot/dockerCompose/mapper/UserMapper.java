package com.czx.demo.spring.boot.dockerCompose.mapper;

import com.czx.demo.spring.boot.dockerCompose.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface UserMapper {
    List<User> selectAll();
    int insert(User user);
    int deleteAll();
}
