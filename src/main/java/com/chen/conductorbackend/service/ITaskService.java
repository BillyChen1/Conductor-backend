package com.chen.conductorbackend.service;

import com.chen.conductorbackend.dto.TaskReturnDTO;
import com.chen.conductorbackend.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 * 救援请求表 服务类
 * </p>
 *
 * @author chen
 * @since 2021-03-25
 */
public interface ITaskService extends IService<Task> {
    List<TaskReturnDTO> listTasksByUidAndStatus(Integer uid, Integer status);
}