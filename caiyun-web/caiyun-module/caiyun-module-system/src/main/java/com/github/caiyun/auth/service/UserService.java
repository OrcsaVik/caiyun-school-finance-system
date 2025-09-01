package com.github.caiyun.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.caiyun.auth.domain.dataobject.UserDO;
import com.github.caiyun.auth.model.vo.user.*;
import com.github.framework.common.response.Response;

public interface UserService extends IService<UserDO> {


    /**
     *
     * 注册信息
     * @param registerUserReqDTO
     * @return
     */
    Response<Long> registerUser(RegisterUserReqDTO registerUserReqDTO);

    Response<Long> registerUserByAccount(RegsitryByAccountVO vo);
}
