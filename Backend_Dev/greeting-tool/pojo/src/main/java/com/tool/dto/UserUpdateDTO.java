package com.tool.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("user info to be updated")
public class UserUpdateDTO {

    @ApiModelProperty
    private String username;

    @ApiModelProperty
    private String password;

    @ApiModelProperty
    private String email;

}
