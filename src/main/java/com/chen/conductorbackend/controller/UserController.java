package com.chen.conductorbackend.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.conductorbackend.common.BaseResult;
import com.chen.conductorbackend.dto.*;
import com.chen.conductorbackend.entity.Task;
import com.chen.conductorbackend.entity.User;
import com.chen.conductorbackend.entity.UserTask;
import com.chen.conductorbackend.enums.LostStatus;
import com.chen.conductorbackend.enums.Role;
import com.chen.conductorbackend.service.ITaskService;
import com.chen.conductorbackend.service.IUserService;
import com.chen.conductorbackend.service.IUserTaskService;
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
import java.util.List;


/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author chen
 * @since 2021-03-25
 */
@RestController
@RequestMapping("/user")
@Api(description = "队员有关api")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IUserTaskService userTaskService;

    @Autowired
    private ITaskService taskService;
    /**
     * 用户登录
     * @param phone
     * @return
     */
    @GetMapping("/login")
    @ApiOperation(value = "小程序用户登录", notes = "以手机号为依据，判断用户的身份是队员还是普通用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "微信号（直接当作手机号）", dataType = "String")
    })
    public BaseResult login(@RequestParam("phone") String phone) {
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        UserInfoDTO userInfo = new UserInfoDTO();
        String token = null;
        if (user != null) {
            BeanUtils.copyProperties(user, userInfo);
            userInfo.setUid(user.getId());
            userInfo.setAge(Period.between(user.getBirth().toLocalDate(), LocalDate.now()).getYears());
            token = "" + user.getId();
            userInfo.setToken(token);
        } else {
            //新建一条用户记录返回
            log.info("系统里无该队员，新建一条用户记录返回");
            userInfo.setRole(Role.ORDINARY_USER.getCode());
            userInfo.setAge(20);
            userInfo.setPhone("18888888888");
            userInfo.setUid(-1);
            userInfo.setGender("1");
            userInfo.setAddress("无地址");
            userInfo.setUsername("普通成员");
            //对于普通成员，token统一为1
            token = "-1";
            userInfo.setToken(token);
        }
        //将uid当作token写入redis
        if (redisUtil.hasKey(token)) {
            redisUtil.expire(token, 24 * 60 * 60);
        } else {
            redisUtil.set(token, token, 24 * 60 * 60);
        }
        return BaseResult.successWithData(userInfo);
    }

    /**
     * 根据用户Id获取基本信息
     * @param uid
     * @param token
     * @return
     */
    @GetMapping("check/{uid}")
    @ApiOperation(value = "获取一名用户的简要信息", notes = "根据用户id获取用户的简要信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", dataType = "Integer")
    })
    public BaseResult getUserInfoByUid(@PathVariable("uid") Integer uid, @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("用户未登录");
            return BaseResult.failWithCodeAndMsg(1, "用户未登录");
        }

        User user = userService.getById(uid);
        if (user == null) {
            log.warn("无法找到该用户");
            return BaseResult.failWithCodeAndMsg(1, "无法找到该Id的用户");
        } else {
            UserInfoDTO userInfo = new UserInfoDTO();
            BeanUtils.copyProperties(user, userInfo);
            userInfo.setUid(user.getId());
            userInfo.setAge(Period.between(user.getBirth().toLocalDate(), LocalDate.now()).getYears());
            return BaseResult.successWithData(userInfo);
        }
    }


    /**
     * 更新队员的地址信息
     * @param updateLocationDTO
     * @param token
     * @return
     */
    @PostMapping("/updateLocation")
    @ApiOperation(value = "更新队员的实时位置", notes = "更新队员的地址（经纬度）")
    public BaseResult updateLocation(@RequestBody UpdateLocationDTO updateLocationDTO, @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("用户未登录");
            return BaseResult.failWithCodeAndMsg(1, "用户未登录");
        }
        //如果登录的是普通用户，则无权限
        if ("-1".equals(token)) {
            log.warn("用户无权限");
            return BaseResult.failWithCodeAndMsg(1, "无权限");
        }

        User user = userService.getById(updateLocationDTO.getUid());
        if (user == null) {
            log.warn("无法找到该Id用户");
            return BaseResult.failWithCodeAndMsg(1, "无法找到该用户id");
        }

        //修改用户的经纬度信息
        user.setLatitude(updateLocationDTO.getLatitude());
        user.setLongitude(updateLocationDTO.getLongitude());
        user.setGmtModified(System.currentTimeMillis());
        userService.updateById(user);
        log.info("修改地理位置成功");
        return BaseResult.success();
    }


    /**
     * 队员受理一条救援请求
     * @param requestHandleDTO
     * @param token
     * @return
     */
    @PostMapping("/accept")
    @ApiOperation(value = "队员受理一条任务", notes = "队员受理一条任务")
    public BaseResult acceptTask(@RequestBody RequestHandleDTO requestHandleDTO,
                                 @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("用户未登录");
            return BaseResult.failWithCodeAndMsg(1, "用户未登录");
        }
        //如果登录的是普通用户，则无权限
        if ("-1".equals(token)) {
            log.warn("用户无权限");
            return BaseResult.failWithCodeAndMsg(1, "无权限");
        }

        Integer uid = requestHandleDTO.getUid();
        Integer requestId = requestHandleDTO.getRequestId();


        //如果Uid在用户表中找不到，则受理失败
        if (userService.getById(uid) == null) {
            log.info("受理任务失败");
            return BaseResult.failWithCodeAndMsg(1, "当前用户不存在，受理任务失败");
        }

        //如果任务处于完成或超时状态或找不到该任务则受理失败
        Task task = taskService.getById(requestId);
        if (task == null || task.getLostStatus() == LostStatus.FINISHED.getStatus()
        || task.getLostStatus() == LostStatus.OVERDUE.getStatus()) {
            log.info("受理任务失败");
            return BaseResult.failWithCodeAndMsg(1, "任务无法受理，受理任务失败");
        }
        //如果自己已经受理过该任务，也受理失败
        UserTask userTaskRelation = new UserTask();
        userTaskRelation.setUid(uid);
        userTaskRelation.setRid(requestId);
        userTaskRelation.setGmtCreate(System.currentTimeMillis());
        userTaskRelation.setGmtModified(System.currentTimeMillis());
        QueryWrapper<UserTask> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid)
                .eq("rid", requestId);
        if (userTaskService.getOne(wrapper) != null) {
            log.info("用户"+uid+"已经受理过任务"+requestId);
            return BaseResult.failWithCodeAndMsg(1, "当前用户已受理过该任务，请勿重复受理");
        }


        //修改状态为进行中
        task.setLostStatus(LostStatus.IN_PROGRESS.getStatus());
        task.setGmtModified(System.currentTimeMillis());

        //task表记录的救援人数自增1
        task.setRescueNum(task.getRescueNum() + 1);

        //将task更新到数据库中
        taskService.updateById(task);

        //在关系表中新建一项
        userTaskService.save(userTaskRelation);

        log.info("队员受理任务成功");

        //返回这条受理的任务信息
        TaskReturnDTO taskReturnDTO = new TaskReturnDTO();
        BeanUtils.copyProperties(task, taskReturnDTO);
        taskReturnDTO.setRequestId(task.getId());
        taskReturnDTO.setLostAge(Period.between(task.getLostBirth().toLocalDate(), LocalDate.now()).getYears());
        taskReturnDTO.setLostStatus(LostStatus.nameOf(task.getLostStatus()));
        return BaseResult.successWithData(taskReturnDTO);
    }


    /**
     * 队员完成一项任务
     * @param requestHandleDTO
     * @param token
     * @return
     */
    @PostMapping("/complete")
    @ApiOperation(value = "队员完成一条任务", notes = "队员完成一条任务")
    public BaseResult complete(@RequestBody RequestHandleDTO requestHandleDTO,
                               @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("用户未登录");
            return BaseResult.failWithCodeAndMsg(1, "用户未登录");
        }
        //如果登录的是普通用户，则无权限
        if ("-1".equals(token)) {
            log.warn("用户无权限");
            return BaseResult.failWithCodeAndMsg(1, "无权限");
        }


        Integer uid = requestHandleDTO.getUid();
        Integer requestId = requestHandleDTO.getRequestId();

        //如果Uid在用户表中找不到，则失败
        if (userService.getById(uid) == null) {
            log.info("完成任务失败");
            return BaseResult.failWithCodeAndMsg(1, "当前用户不存在，完成任务失败");
        }

        //完成任务的条件：任务存在、关系表中存在记录、任务状态为进行中
        Task task = taskService.getById(requestId);
        if (task == null) {
            log.info("任务为空，无法完成任务");
            return BaseResult.failWithCodeAndMsg(1, "任务不存在，无法完成");
        }
        UserTask userTask = new UserTask();
        userTask.setUid(uid);
        userTask.setRid(requestId);
        QueryWrapper<UserTask> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid)
                .eq("rid", requestId);
        if (userTaskService.getOne(wrapper) != null
        && task.getLostStatus() == LostStatus.IN_PROGRESS.getStatus()) {
            task.setLostStatus(LostStatus.FINISHED.getStatus());
            task.setGmtModified(System.currentTimeMillis());
            taskService.updateById(task);

            //返回这条受理的任务信息
            TaskReturnDTO taskReturnDTO = new TaskReturnDTO();
            BeanUtils.copyProperties(task, taskReturnDTO);
            taskReturnDTO.setRequestId(task.getId());
            taskReturnDTO.setLostAge(Period.between(task.getLostBirth().toLocalDate(), LocalDate.now()).getYears());
            taskReturnDTO.setLostStatus(LostStatus.nameOf(task.getLostStatus()));

            return BaseResult.successWithData(taskReturnDTO);
        } else {
            log.info("任务完成失败");
            return BaseResult.failWithCodeAndMsg(1, "当前任务无法完成");
        }
    }

    /**
     * 获得和uid编号队员的队友列表（接手相同任务且任务的状态为进行中，那么这两个人就是队友）
     * @param uid
     * @param token
     * @return
     */
    @GetMapping("/partner/{uid}")
    @ApiOperation(value = "寻找队友", notes = "获得和uid编号队员的队友列表（接手相同任务且任务的状态为进行中，那么这两个人就是队友）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", dataType = "Integer")
    })
    public BaseResult getPartners(@PathVariable("uid") Integer uid,
                                  @RequestHeader("Authorization") String token) {
        if (!redisUtil.hasKey(token)) {
            log.warn("用户未登录");
            return BaseResult.failWithCodeAndMsg(1, "用户未登录");
        }
        //如果登录的是普通用户，则无权限
        if ("-1".equals(token)) {
            log.warn("用户无权限");
            return BaseResult.failWithCodeAndMsg(1, "无权限");
        }


        List<UserReturnDTO> userList = userService.listPartnersByUid(uid);

        return BaseResult.successWithData(userList);


    }
}

