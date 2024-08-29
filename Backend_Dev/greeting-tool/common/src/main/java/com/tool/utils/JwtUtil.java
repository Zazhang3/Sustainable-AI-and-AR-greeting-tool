package com.tool.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * Generate jwt token
     * Use fixed secret key for private key
     * @param secretKey
     * @param ttlMillis
     * @param claims
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        //Use Hs256 algorithm
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //Generate JWT time
        long expMillis = System.currentTimeMillis()+ttlMillis;
        Date exp = new Date(expMillis);

        //set JWT body
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                .setExpiration(exp);

        return jwtBuilder.compact();
    }

    /**
     * Decrypt the token
     * @param secretKey
     * @param token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        //Get default JWT Parser
        Claims claims = Jwts.parser()
                //Set the secret key for signing
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                //Set the JWT to be parsed
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
