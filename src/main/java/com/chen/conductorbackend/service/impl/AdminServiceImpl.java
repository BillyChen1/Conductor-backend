package com.chen.conductorbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.conductorbackend.entity.Admin;
import com.chen.conductorbackend.mapper.AdminMapper;
import com.chen.conductorbackend.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chen
 * @since 2021-10-13
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    AdminMapper adminMapper;


    @Override
    public Admin getByName(String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",name);
        return adminMapper.selectOne(queryWrapper);
    }
}
