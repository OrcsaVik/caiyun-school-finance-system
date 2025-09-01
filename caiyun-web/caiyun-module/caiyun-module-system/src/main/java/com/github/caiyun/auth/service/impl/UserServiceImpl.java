package com.github.caiyun.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.caiyun.auth.constant.RedisKeyConstants;
import com.github.caiyun.auth.constant.RoleConstants;
import com.github.caiyun.auth.domain.dataobject.RoleDO;
import com.github.caiyun.auth.domain.dataobject.UserDO;
import com.github.caiyun.auth.domain.dataobject.UserRoleDO;
import com.github.caiyun.mapper.RoleDOMapper;
import com.github.caiyun.mapper.UserDOMapper;
import com.github.caiyun.mapper.UserRoleDOMapper;
import com.github.caiyun.auth.model.vo.user.RegisterUserReqDTO;
import com.github.caiyun.auth.model.vo.user.RegsitryByAccountVO;
import com.github.caiyun.auth.service.UserService;
import com.github.framework.common.enums.StatusEnum;
import com.github.framework.common.response.Response;
import com.github.framework.common.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Vik
 * @date 2025-08-29 12:02
 * @description
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDOMapper,UserDO> implements UserService {

    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private UserRoleDOMapper userRoleDOMapper;
    @Resource
    private RoleDOMapper roleDOMapper;




    @Transactional(rollbackFor = Exception.class)
    public Response<Long> registerUser(RegisterUserReqDTO registerUserReqDTO) {
        String phone = registerUserReqDTO.getPhone();

        // 先判断该手机号是否已被注册
        UserDO userDO1 = userDOMapper.selectByPhone(phone);

        log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO1));

        // 若已注册，则直接返回用户 ID
        if (Objects.nonNull(userDO1)) {
            return Response.success(userDO1.getId());
        }

        //自增id

        //
        Long userId = redisTemplate.opsForValue().increment(RedisKeyConstants.USER_ID_GENERATOR_KEY);


        UserDO userDO = UserDO.builder()
                .id(userId)
                .username("user" + userId) // 需根据业务生成，此处仅为示例
                .password("default_password") // 需设置密码，此处为占位
                .realName(null)
                .phone(phone)
                .email(null)
                .avatar("/default/avatar.png")
                //状态
                .status(StatusEnum.ENABLE.getValue())
                .lastLoginTime(null)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // 添加入库
        userDOMapper.insert(userDO);



        // 给该用户分配一个默认角色
        UserRoleDO userRoleDO = UserRoleDO.builder()
                .userId(userId)
                .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                .createTime(LocalDateTime.now())
                .build();
        userRoleDOMapper.insert(userRoleDO);

        RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);

        // 将该用户的角色 ID 存入 Redis 中
        List<String> roles = new ArrayList<>(1);
        roles.add(roleDO.getRoleCode());

        String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
        //键值 类似 user:roles:userid 值为roleId
        redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

        return Response.success(userId);
    }



    // UserService 方法
    @Transactional(rollbackFor = Exception.class)
    public Response<Long> registerUserByAccount(RegsitryByAccountVO vo) {
        String account = vo.getAccount();
        String password = vo.getPassword(); // 假设需要密码

        // 先判断该账号是否已被注册
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        UserDO userDO = userDOMapper.selectOne(queryWrapper);

        log.info("==> 用户是否注册, account: {}, userDO: {}", account, JsonUtils.toJsonString(userDO));

        // 若已注册，则直接返回用户 ID
        if (Objects.nonNull(userDO)) {
            return Response.success(userDO.getId());
        }

        // 生成用户 ID
        Long userId = redisTemplate.opsForValue().increment(RedisKeyConstants.USER_ID_GENERATOR_KEY);

        // 构建用户对象
        UserDO newUser = UserDO.builder()
                .id(userId)
                .account(account)
                .username("user" + userId)
                .password(password) // 生产环境需加密
                .realName(null)
                .phone(null)
                .email(null)
                .avatar("/default/avatar.png")
                .status(StatusEnum.ENABLE.getValue())
                .lastLoginTime(null)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // 添加入库
        userDOMapper.insert(newUser);

        // 给该用户分配默认角色
        UserRoleDO userRoleDO = UserRoleDO.builder()
                .userId(userId)
                .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                .createTime(LocalDateTime.now())
                .build();
        userRoleDOMapper.insert(userRoleDO);

        RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);

        // 将用户角色存入 Redis
        List<String> roles = new ArrayList<>(1);
        roles.add(roleDO.getRoleCode());
        String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
        redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

        return Response.success(userId);
    }
}
