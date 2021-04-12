package com.chen.conductorbackend.service.impl;

import com.chen.conductorbackend.dto.TaskReturnDTO;
import com.chen.conductorbackend.entity.Task;
import com.chen.conductorbackend.enums.LostStatus;
import com.chen.conductorbackend.mapper.TaskMapper;
import com.chen.conductorbackend.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 救援请求表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2021-03-25
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<TaskReturnDTO> listTasksByUidAndStatus(Integer uid, Integer status) {
        List<Task> tasks = taskMapper.listTasksByUidAndStatus(uid, status);
        List<TaskReturnDTO> res = new ArrayList<>();
        if (tasks.isEmpty()) {
            return res;
        }
        tasks.stream().forEach(task -> {
            TaskReturnDTO taskReturnDTO = new TaskReturnDTO();
            BeanUtils.copyProperties(task, taskReturnDTO);
            taskReturnDTO.setRequestId(task.getId());
            //设置年龄
            taskReturnDTO.setLostAge(Period.between(task.getLostBirth().toLocalDate(), LocalDate.now()).getYears());
            //设置救援状态
            taskReturnDTO.setLostStatus(LostStatus.nameOf(task.getLostStatus()));
            res.add(taskReturnDTO);
        });
        return res;
    }
}
