package com.tool.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("tool.jwt")
@Data
public class JwtProperties {

    /**
     * Configuration for generating jwt tokens
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

}
