package com.chen.conductorbackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.conductorbackend.common.BaseResult;
import com.chen.conductorbackend.dto.TaskPostDTO;
import com.chen.conductorbackend.dto.TaskReturnDTO;
import com.chen.conductorbackend.entity.Task;
import com.chen.conductorbackend.enums.LostStatus;
import com.chen.conductorbackend.service.ITaskService;
import com.chen.conductorbackend.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 救援请求表 前端控制器
 * </p>
 *
 * @author chen
 * @since 2021-03-25
 */
@RestController
@RequestMapping("/task")
@Slf4j
public class TaskController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ITaskService taskService;

    /**
     * 家属提交一条报案信息
     * @param taskPostDTO
     * @return
     */
    @PostMapping("/submit")
    public BaseResult submitTask(@RequestBody TaskPostDTO taskPostDTO, @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("用户未登录");
            return BaseResult.failWithCodeAndMsg(1, "用户未登录");
        }

        Task task = new Task();
        BeanUtils.copyProperties(taskPostDTO, task);
        //设置状态
        task.setLostStatus(LostStatus.UNACCEPTED.getStatus());

        //将yyyy-MM-dd形式的字符串转化为Date对象存入数据库
        LocalDate lostBirth = LocalDate.parse(taskPostDTO.getLostBirth(), DateTimeFormatter.ISO_DATE);
        task.setLostBirth(new java.sql.Date(lostBirth.atStartOfDay(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli()));

        task.setGmtCreate(System.currentTimeMillis());
        task.setGmtModified(System.currentTimeMillis());

        log.info("报案信息插入数据库");

        taskService.save(task);
        return BaseResult.success();
    }


    /**
     * 根据状态以及用户id获取任务列表
     * @param status    希望获取指定状态的任务
     * @param uid   希望获得该用户接手的任务
     * @param token
     * @return
     */
    @GetMapping("")
    public BaseResult listTasksByUidAndStatus(@RequestParam(value = "status", required = false) Integer status,
                                              @RequestParam(value = "uid", required = false) Integer uid,
                                              @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("用户未登录");
            return BaseResult.failWithCodeAndMsg(1, "用户未登录");
        }
        List<TaskReturnDTO> taskList = taskService.listTasksByUidAndStatus(uid, status);
        return BaseResult.successWithData(taskList);
    }

    /**
     * 列出可用的任务 包括未受理0 进行中1 已超时3的
     * @param token
     * @return
     */
    @GetMapping("/available")
    public BaseResult listAvailableTasks(@RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("用户未登录");
            return BaseResult.failWithCodeAndMsg(1, "用户未登录");
        }

        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lost_status", LostStatus.UNACCEPTED.getStatus())
                .or().eq("lost_status", LostStatus.IN_PROGRESS.getStatus())
                .or().eq("lost_status", LostStatus.OVERDUE.getStatus())
                .orderByDesc("gmt_create");
        List<Task> taskList = taskService.list(queryWrapper);
        //将task转化为taskReturnDTO
        List<TaskReturnDTO> taskReturnDTOList = new ArrayList<>();
        for (Task task : taskList) {
            TaskReturnDTO taskReturnDTO = new TaskReturnDTO();
            BeanUtils.copyProperties(task, taskReturnDTO);
            taskReturnDTO.setRequestId(task.getId());
            taskReturnDTO.setLostAge(Period.between(task.getLostBirth().toLocalDate(), LocalDate.now()).getYears());
            taskReturnDTO.setLostStatus(LostStatus.nameOf(task.getLostStatus()));
            taskReturnDTOList.add(taskReturnDTO);
        }

        log.info("成功查看可用任务列表");

        return BaseResult.successWithData(taskReturnDTOList);
    }



}

