package com.tool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Return data for get email")
public class UserEmailVO {

    @ApiModelProperty("Username")
    private String username;

    @ApiModelProperty("Verification Code")
    private String verificationCode;

}
