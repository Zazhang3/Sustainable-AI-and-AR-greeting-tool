package com.tool.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("user info model")
public class UserDTO implements Serializable {

    @ApiModelProperty
    private String username;

    @ApiModelProperty
    private String password;

    @ApiModelProperty
    private String email;

}
