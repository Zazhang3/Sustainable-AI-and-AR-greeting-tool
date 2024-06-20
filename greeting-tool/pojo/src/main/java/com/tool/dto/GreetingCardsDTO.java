package com.tool.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Greeting card info model")
public class GreetingCardsDTO implements Serializable {

    @ApiModelProperty
    private Long user_id;

    @ApiModelProperty
    private String card_id;

    @ApiModelProperty
    private String postcode;

    @ApiModelProperty
    private String emoji_id;

    @ApiModelProperty
    private String animation_id;

}
