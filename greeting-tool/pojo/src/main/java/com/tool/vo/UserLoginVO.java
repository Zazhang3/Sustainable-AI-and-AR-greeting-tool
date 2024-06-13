package com.tool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Return data for user login")
public class UserLoginVO implements Serializable {

    @ApiModelProperty("Primary Key")
    private Long id;

    @ApiModelProperty("Username")
    private String username;

    @ApiModelProperty("jwt")
    private String token;

}
