package com.chen.conductorbackend.controller;

import com.alibaba.fastjson.JSONObject;
import com.chen.conductorbackend.common.BaseResult;
import com.chen.conductorbackend.dto.AdminLoginDTO;
import com.chen.conductorbackend.dto.UserPostDTO;
import com.chen.conductorbackend.dto.UserReturnDTO;
import com.chen.conductorbackend.entity.User;
import com.chen.conductorbackend.enums.LoginType;
import com.chen.conductorbackend.service.IAdminService;
import com.chen.conductorbackend.service.ITaskService;
import com.chen.conductorbackend.service.IUserService;
import com.chen.conductorbackend.shiro.CustomLoginToken;
import com.chen.conductorbackend.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Api(description = "管理员有关api")
@CrossOrigin
@Slf4j
public class AdminController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IUserService userService;
    @Autowired
    private ITaskService taskService;
    @Autowired
    private IAdminService adminService;

    /**
     * 管理员登录
     *
     * @param adminLoginDTO 账号和密码
     * @return code: 0-->success 1-->fail   token
     */
    @PostMapping("/login")
    @ApiOperation(value = "管理员登陆")
    public Object login(@RequestBody AdminLoginDTO adminLoginDTO) {
        String username = adminLoginDTO.getUsername();
        String password = adminLoginDTO.getPassword();

        Subject subject = SecurityUtils.getSubject();
        CustomLoginToken loginToken = new CustomLoginToken(username,password, LoginType.ADMIN_LOGIN);
        try{
            //shiro验证并更新redis缓存
            subject.login(loginToken);
//            if (redisUtil.hasKey(username)) {
//                redisUtil.expire(username, 24 * 60 * 60);
//            } else {
//                redisUtil.set(username, password, 24 * 60 * 60);
//            }
//
//            log.info("管理员登录成功");
            JSONObject token = new JSONObject();
            token.put("token", subject.getSession().getId().toString());
            return BaseResult.successWithData(token);

        }catch (AuthenticationException e){
            log.warn("管理员账号或密码错误");
            return BaseResult.failWithCodeAndMsg(1, "账号或密码错误");
        }
    }

    /**
     * 管理员退出
     * @return
     */
    @GetMapping("/logout")
    @ApiOperation(value = "管理员退出")
    public Object logout(){
        try{
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return BaseResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return BaseResult.failWithCodeAndMsg(1,"退出失败");
        }
    }

    /**
     * 管理员注册
     * @param adminLoginDTO
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "管理员注册")
    public Object register(@RequestBody AdminLoginDTO adminLoginDTO){
        String username = adminLoginDTO.getUsername();
        String password = adminLoginDTO.getPassword();
        if(adminService.register(username,password)){
            return BaseResult.success();
        }
        return BaseResult.failWithCodeAndMsg(1,"注册失败");
    }


    /**
     * 管理员添加新队员
     *
     * @param userInfo 新队员信息
     * @return true or false
     */
    @PostMapping("/user/member")
    @ApiOperation(value = "管理员添加新队员")
    public Object insertAUser(@RequestBody UserPostDTO userInfo) {
        if(userService.register(userInfo)){
            log.info("添加新队员成功");
            return BaseResult.success();
        }
        return BaseResult.failWithCodeAndMsg(1,"添加新队员失败");
    }

    /**
     * 管理员删除队员
     *
     * @param uid   user id
     * @return true or false
     */
    @DeleteMapping("/user/member/{uid}")
    @ApiOperation(value = "管理员删除队员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "删除的用户id", dataType = "Integer")
    })
    public Object deleteUserById(@PathVariable int uid) {

        boolean flag = userService.removeById(uid);
        return BaseResult.success();
        //还需要删除redis缓存
//        if (redisUtil.hasKey(uid + "")) {
//            redisUtil.expire("" + uid, -1);
//        }
//        if (flag) {
//            log.info("删除队员成功");
//            return BaseResult.success();
//        } else {
//            log.warn("删除队员失败");
//            return BaseResult.failWithCodeAndMsg(1, "没有指定id的队员");
//        }
    }

    @PostMapping("/user/member/{uid}")
    @ApiOperation(value = "管理员更新队员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "更新的用户id", dataType = "Integer")
    })
    public Object updateUserInfo(@PathVariable int uid, @RequestBody UserPostDTO userInfo) {


        User user = userService.getById(uid);
        BeanUtils.copyProperties(userInfo, user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //前端传过来的birth不为空的情况下，才使用前端的birth数据
            if (userInfo.getBirth() != null) {
                user.setBirth(new java.sql.Date(sdf.parse(userInfo.getBirth()).getTime()));
            }
            if(userInfo.getPassword()!=null){
                user.changPassword(user.getPassword());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGmtModified(System.currentTimeMillis());

        if (userService.updateById(user)) {
            log.info("编辑队员信息成功");
            return BaseResult.success();
        } else {
            log.info("编辑队员信息失败");
            return BaseResult.failWithCodeAndMsg(1, "编辑队员信息失败");
        }
    }

    /**
     * 管理员获取所有用户的信息，其中每个用户还包含其接手且正在进行的任务列表
     * @return
     */
    @GetMapping("/user/member")
    @ApiOperation(value = "管理员获取所有队员信息")
    public BaseResult listAllUserInfo() {

        List<UserReturnDTO> userList = userService.listAllUserInfo();

        return BaseResult.successWithData(userList);
    }

}
