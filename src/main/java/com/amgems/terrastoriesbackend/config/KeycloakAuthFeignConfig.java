package com.amgems.terrastoriesbackend.config;

import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;

@Configuration
public class KeycloakAuthFeignConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public KeycloakAuthFeignConfig(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    public FormEncoder feignFormEncoder() {
        return new FormEncoder(new SpringEncoder(this.messageConverters));
    }
}