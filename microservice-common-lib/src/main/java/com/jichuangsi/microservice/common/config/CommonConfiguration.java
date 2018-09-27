package com.jichuangsi.microservice.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses=com.jichuangsi.microservice.common.Main.class)
public class CommonConfiguration {
}

