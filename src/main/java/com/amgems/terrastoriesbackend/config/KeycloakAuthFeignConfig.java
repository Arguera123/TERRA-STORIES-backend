package com.amgems.terrastoriesbackend.config;

import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.http.converter.autoconfigure.ClientHttpMessageConvertersCustomizer;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAuthFeignConfig {

    // 1. Inyectamos los proveedores de personalización de clientes requeridos por la nueva API de Feign
    private final ObjectProvider<ClientHttpMessageConvertersCustomizer> clientCustomizers;
    private final ObjectProvider<HttpMessageConverterCustomizer> cloudCustomizers;

    public KeycloakAuthFeignConfig(
            ObjectProvider<ClientHttpMessageConvertersCustomizer> clientCustomizers,
            ObjectProvider<HttpMessageConverterCustomizer> cloudCustomizers) {
        this.clientCustomizers = clientCustomizers;
        this.cloudCustomizers = cloudCustomizers;
    }

    // 2. Construimos el Bean de FeignHttpMessageConverters pasando los dos parámetros obligatorios
    @Bean
    public FeignHttpMessageConverters feignHttpMessageConverters() {
        return new FeignHttpMessageConverters(this.clientCustomizers, this.cloudCustomizers);
    }

    // 3. El constructor de SpringEncoder ahora requiere un ObjectProvider wrapping
    @Bean
    public FormEncoder feignFormEncoder(ObjectProvider<FeignHttpMessageConverters> feignHttpMessageConvertersProvider) {
        return new FormEncoder(new SpringEncoder(feignHttpMessageConvertersProvider));
    }
}