package com.tool.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("user login info model")
public class UserLoginDTO implements Serializable {

    @ApiModelProperty
    private String username;

    @ApiModelProperty("password")
    private String password;

}
