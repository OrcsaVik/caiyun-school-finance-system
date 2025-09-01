package com.github.caiyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.caiyun.auth.domain.dataobject.PermissionDO;

public interface PermissionDOMapper extends BaseMapper<PermissionDO> {
    int deleteByPrimaryKey(Long id);

    int insert(PermissionDO record);

    int insertSelective(PermissionDO record);

    PermissionDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PermissionDO record);

    int updateByPrimaryKey(PermissionDO record);
}