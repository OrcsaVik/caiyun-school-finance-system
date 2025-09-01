package com.github.caiyun.auth.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Vik
 * @date 2025-08-29 12:43
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegsitryByAccountVO {

    private String account;
    private String password; // 注册时通常需要密码
}
