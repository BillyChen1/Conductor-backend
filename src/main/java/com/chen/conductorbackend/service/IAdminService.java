package com.chen.conductorbackend.service;

import com.chen.conductorbackend.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chen
 * @since 2021-10-13
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 根据姓名获取管理员
     * @param name
     * @return
     */
    Admin getByName(String name);

    /**
     * 管理员注册
     * @param name
     * @param password
     * @return
     */
    boolean register(String name,String password);
}
