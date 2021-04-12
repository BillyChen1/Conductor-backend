package com.chen.conductorbackend.service.impl;

import com.chen.conductorbackend.entity.UserTask;
import com.chen.conductorbackend.mapper.UserTaskMapper;
import com.chen.conductorbackend.service.IUserTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 队员-救援任务关系表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2021-04-12
 */
@Service
public class UserTaskServiceImpl extends ServiceImpl<UserTaskMapper, UserTask> implements IUserTaskService {

}
