package com.tool.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("user info to be updated")
public class UserUpdateDTO {

    @ApiModelProperty
    private Long id;

    @ApiModelProperty
    private String username;

    @ApiModelProperty
    private String password;

}
