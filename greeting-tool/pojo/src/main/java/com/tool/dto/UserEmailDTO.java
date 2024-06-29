package com.tool.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("user login info model")
public class UserEmailDTO {

    @ApiModelProperty
    private String username;

    @ApiModelProperty
    private String email;
}
