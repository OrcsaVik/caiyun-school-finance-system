package com.github.caiyun.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.caiyun.auth.domain.dataobject.UserDO;

public interface UserDOMapper extends BaseMapper<UserDO> {
    int deleteByPrimaryKey(Long id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);

    default UserDO selectByPhone(String phone) {

        phone = phone.trim();

        // 2. 构造查询条件
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);

        // 3. 执行查询并返回结果
        // 如果 phone 字段有唯一索引/约束，selectOne 是安全的
        return this.selectOne(queryWrapper);
    }

    // 2. 使用 QueryWrapper 构建查询条件
    default UserDO selectByAccount(String account) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return this.selectOne(queryWrapper);
    }
}
