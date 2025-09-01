package com.github.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    // 启用
    ENABLE(1),
    // 禁用
    DISABLED(0);

    private final Integer value;
}
