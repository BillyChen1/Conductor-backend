package com.chen.conductorbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.conductorbackend.entity.Admin;
import com.chen.conductorbackend.mapper.AdminMapper;
import com.chen.conductorbackend.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.conductorbackend.utils.SaltUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
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

    @Override
    public boolean register(String name, String password) {
        try {
            String salt = SaltUtil.getSalt(8);
            Md5Hash md5Hash =new Md5Hash(password,salt,1024);
            Admin admin = new Admin();
            admin.setName(name);
            admin.setPassword(md5Hash.toHex());
            admin.setSalt(salt);
            admin.setGmtModified(System.currentTimeMillis());
            admin.setGmtCreate(System.currentTimeMillis());
            adminMapper.insert(admin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
