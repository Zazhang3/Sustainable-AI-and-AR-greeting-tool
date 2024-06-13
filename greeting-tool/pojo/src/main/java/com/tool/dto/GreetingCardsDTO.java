package com.tool.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GreetingCardsDTO implements Serializable {
    private Long id;
    private String username;
    private String password;
}
