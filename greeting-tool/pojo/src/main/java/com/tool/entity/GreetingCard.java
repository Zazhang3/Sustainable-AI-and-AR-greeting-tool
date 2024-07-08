package com.tool.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GreetingCard implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long user_id;

    private String card_id;

    private String postcode;

    private String emoji_id;

    private String animation_id;

    private String create_time;
}
