package com.github.caiyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.caiyun.auth.domain.dataobject.RoleDO;

public interface RoleDOMapper extends BaseMapper<RoleDO> {
    int deleteByPrimaryKey(Long id);

    int insert(RoleDO record);

    int insertSelective(RoleDO record);

    RoleDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleDO record);

    int updateByPrimaryKey(RoleDO record);
}