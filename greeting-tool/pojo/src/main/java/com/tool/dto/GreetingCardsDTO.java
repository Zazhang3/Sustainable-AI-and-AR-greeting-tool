package com.tool.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Greeting card info model")
public class GreetingCardsDTO implements Serializable {

    private Long user_id;

    private String card_id;

    private String postcode;

    private String emoji_id;

    private String animation_id;

}
