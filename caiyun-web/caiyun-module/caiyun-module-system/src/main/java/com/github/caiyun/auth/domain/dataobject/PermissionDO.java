package com.github.caiyun.auth.domain.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_permission")
public class PermissionDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;

    private String name;

    private String permissionCode;

    private Byte permissionType;

    private String path;

    private String component;

    private String icon;

    private Integer sort;

    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}