package com.chen.conductorbackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.conductorbackend.common.BaseResult;
import com.chen.conductorbackend.dto.TaskPostDTO;
import com.chen.conductorbackend.dto.TaskReturnDTO;
import com.chen.conductorbackend.entity.Task;
import com.chen.conductorbackend.enums.LostStatus;
import com.chen.conductorbackend.service.ITaskService;
import com.chen.conductorbackend.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
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
@CrossOrigin
@Api(description = "任务有关api")
public class TaskController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ITaskService taskService;

    /**
     * 查看一条报案信息详情
     *
     * @param requestId task id
     * @return taskReturnDTO
     */
    @GetMapping("/{requestId}")
    @ApiOperation(value = "（管理员可能会用到）查看一条报案信息详情", notes = "根据任务id查看一条报案信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requestId", value = "任务id", required = true, dataType = "Integer")
    })
    public Object getTaskDetailById(@PathVariable int requestId) {


        Task task = taskService.getById(requestId);
        TaskReturnDTO taskReturnDTO = new TaskReturnDTO();
        BeanUtils.copyProperties(task, taskReturnDTO);
        taskReturnDTO.setRequestId(requestId);
        taskReturnDTO.setLostAge(Period.between(task.getLostBirth().toLocalDate(), LocalDate.now()).getYears());
        taskReturnDTO.setLostStatus(LostStatus.nameOf(task.getLostStatus()));

        log.info("查看一条报案信息详情成功");
        return BaseResult.successWithData(taskReturnDTO);
    }

    /**
     * 家属提交一条报案信息
     * @param taskPostDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation(value = "家属提交报案信息", notes = "家属提交报案信息")
    public BaseResult submitTask(@RequestBody TaskPostDTO taskPostDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskPostDTO, task);
        //设置状态
        task.setLostStatus(LostStatus.UNACCEPTED.getStatus());
        task.setRescueNum(0);

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
     * 家属修改报案信息
     * @param taskPostDTO
     * @param requetId
     * @return
     */
    @PostMapping("/submit/{requestId}")
    @ApiOperation(value = "家属编辑修改报案信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requestId", value = "案件任务的id")
    })
    public BaseResult updateTask(@RequestBody TaskPostDTO taskPostDTO,
                                 @PathVariable("requestId") Integer requetId) {

        //从数据库中寻找旧的报案信息
        Task dbTask = taskService.getById(requetId);
        if (dbTask == null) {
            return BaseResult.failWithCodeAndMsg(1, "无法找到报案信息");
        }
        //将提交的表单内容更新到旧的数据记录中
        BeanUtils.copyProperties(taskPostDTO, dbTask);

        //将yyyy-MM-dd形式的字符串转化为Date对象存入数据库
        LocalDate lostBirth = LocalDate.parse(taskPostDTO.getLostBirth(), DateTimeFormatter.ISO_DATE);
        dbTask.setLostBirth(new java.sql.Date(lostBirth.atStartOfDay(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli()));

        dbTask.setGmtModified(System.currentTimeMillis());

        log.info("修改后的报案信息插入数据库");

        taskService.updateById(dbTask);
        return BaseResult.success();
    }


    /**
     * 根据状态以及用户id获取任务列表
     * @param status    希望获取指定状态的任务
     * @param uid   希望获得该用户接手的任务
     * @return
     */
    @GetMapping("")
    @ApiOperation(value = "（管理员可能会用到）根据一定条件获取任务列表", notes = "根据案件的状态以及案件的受理人id，查询满足条件的案件")
    public BaseResult listTasksByUidAndStatus(@RequestParam(value = "status", required = false) Integer status,
                                              @RequestParam(value = "uid", required = false) Integer uid) {

        List<TaskReturnDTO> taskList = taskService.listTasksByUidAndStatus(uid, status);
        return BaseResult.successWithData(taskList);
    }

    /**
     * 列出可用的任务 包括未受理0 进行中1 已超时3的
     * @return
     */
    @GetMapping("/available")
    @ApiOperation(value = "查询系统中可用的任务", notes = "包括未受理、进行中、已超时的任务")
    public BaseResult listAvailableTasks() {

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

