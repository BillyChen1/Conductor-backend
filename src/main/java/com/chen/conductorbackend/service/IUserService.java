package com.chen.conductorbackend.service;

import com.chen.conductorbackend.dto.UserReturnDTO;
import com.chen.conductorbackend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author chen
 * @since 2021-03-25
 */
public interface IUserService extends IService<User> {
    /**
     * 获取所有队员信息
     * @return
     */
    List<UserReturnDTO> listAllUserInfo();

    /**
     * 根据uid获取其队友列表
     * @param uid
     * @return
     */
    List<UserReturnDTO> listPartnersByUid(Integer uid);

    /**
     * 根据phone获取user
     * @param phone
     * @return
     */
    User getByPhone(String phone);
}
