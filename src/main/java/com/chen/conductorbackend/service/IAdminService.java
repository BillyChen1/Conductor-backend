package com.chen.conductorbackend.service;

import com.chen.conductorbackend.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
