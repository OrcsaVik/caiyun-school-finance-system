package com.github.caiyun.auth.model.vo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Vik
 * @date 2025-08-28 18:50
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertUserReqVO {

    @NotNull
    private Long userId;

    @NotEmpty
    private String username;


    private String password;

    private String realName;

    @NotBlank(message = "手机号不为空")
    private String phone;

}
