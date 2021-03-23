package com.chen.conductorbackend.service.impl;

import com.chen.conductorbackend.entity.User;
import com.chen.conductorbackend.mapper.UserMapper;
import com.chen.conductorbackend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 队员表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2021-03-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
