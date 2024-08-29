package com.tool.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tool.jwt")
@Data
public class JwtProperties {

    /**
     * Configuration for generating jwt tokens
     */
    private String adminSecretKey;

    private long adminTtl;

    private String adminTokenName;

    private String userSecretKey;

    private long userTtl;

    private String userTokenName;

}
