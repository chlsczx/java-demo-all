package com.czx.demo.spring.mvc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;

@Configuration
@PropertySources({
        @PropertySource("classpath:my.properties")
})
public class PropertySourceConfig {

    @Value("${aa.bb}")
    private String aabb;

    @Bean("aabb")
    public String aabb() {
        return this.aabb;
    }

//    @Bean
//    public static PropertySourcesPlaceholderConfigurer properties(ConfigurableEnvironment env) {
//        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
//        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//        yaml.setResources(new ClassPathResource("my.properties"));
//        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
//        return propertySourcesPlaceholderConfigurer;
//    }

}
