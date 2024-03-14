package com.artemn.treatment.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    JerseyConfig() {
        packages("com.artemn.treatment.api");
    }
}
