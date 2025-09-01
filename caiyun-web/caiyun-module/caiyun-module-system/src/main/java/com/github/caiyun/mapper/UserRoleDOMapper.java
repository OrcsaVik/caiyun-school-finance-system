package com.github.caiyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.caiyun.auth.domain.dataobject.UserRoleDO;

public interface UserRoleDOMapper extends BaseMapper<UserRoleDO> {
    int deleteByPrimaryKey(Long id);

    int insert(UserRoleDO record);

    int insertSelective(UserRoleDO record);

    UserRoleDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserRoleDO record);

    int updateByPrimaryKey(UserRoleDO record);
}