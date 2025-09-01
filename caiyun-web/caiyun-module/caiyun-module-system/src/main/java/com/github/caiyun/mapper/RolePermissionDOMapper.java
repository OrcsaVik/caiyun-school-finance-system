package com.github.caiyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.caiyun.auth.domain.dataobject.RolePermissionDO;

public interface RolePermissionDOMapper extends BaseMapper<RolePermissionDO> {
    int deleteByPrimaryKey(Long id);

    int insert(RolePermissionDO record);

    int insertSelective(RolePermissionDO record);

    RolePermissionDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RolePermissionDO record);

    int updateByPrimaryKey(RolePermissionDO record);
}