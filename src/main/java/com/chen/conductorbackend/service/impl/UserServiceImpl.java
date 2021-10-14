package com.chen.conductorbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.conductorbackend.dto.TaskReturnDTO;
import com.chen.conductorbackend.dto.UserPostDTO;
import com.chen.conductorbackend.dto.UserReturnDTO;
import com.chen.conductorbackend.entity.Admin;
import com.chen.conductorbackend.entity.Task;
import com.chen.conductorbackend.entity.User;
import com.chen.conductorbackend.enums.LostStatus;
import com.chen.conductorbackend.mapper.UserMapper;
import com.chen.conductorbackend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.conductorbackend.utils.SaltUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2021-03-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserReturnDTO> listAllUserInfo() {

        //List<User> users = userMapper.listAllUserInfo();
        List<User> users = userMapper.selectList(new QueryWrapper<>());

        List<UserReturnDTO> userDTOList = new ArrayList<>(users.size());
        //将List<User>转化为List<UserReturnDTO>
        usersToUserDTOS(users, userDTOList);
        return userDTOList;
    }

    @Override
    public List<UserReturnDTO> listPartnersByUid(Integer uid) {

        List<User> users = userMapper.listPartnersByUid(uid);
        List<UserReturnDTO> userDTOList = new ArrayList<>(users.size());
        //将List<User>转化为List<UserReturnDTO>
        usersToUserDTOS(users, userDTOList);

        return userDTOList;
    }

    @Override
    public User getByPhone(String phone) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone",phone);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean register(UserPostDTO userInfo) {
        try{
            User user = new User();
            BeanUtils.copyProperties(userInfo, user);
            String salt = SaltUtil.getSalt(8);
            Md5Hash md5Hash =new Md5Hash(userInfo.getPassword(),salt,1024);
            user.setPassword(md5Hash.toHex());
            user.setSalt(salt);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                user.setBirth(new java.sql.Date(sdf.parse(userInfo.getBirth()).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(System.currentTimeMillis());
            user.setRole(0);
            userMapper.insert(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将List<User>转化为List<UserReturnDTO>
     * @param users
     * @param userDTOList
     */
    private void usersToUserDTOS(List<User> users, List<UserReturnDTO> userDTOList) {
        for (User user : users) {
            UserReturnDTO userReturnDTO = new UserReturnDTO();
            BeanUtils.copyProperties(user, userReturnDTO);
            userReturnDTO.setUid(user.getId());
            userReturnDTO.setAge(Period.between(user.getBirth().toLocalDate(), LocalDate.now()).getYears());
            //为userReturnDTO设置List<TaskReturnDTO>
            List<TaskReturnDTO> taskDTOList = new ArrayList<>(user.getTasks().size());
            for (int i = 0; i < user.getTasks().size(); i++) {
                Task task = user.getTasks().get(i);
                TaskReturnDTO taskReturnDTO = new TaskReturnDTO();
                BeanUtils.copyProperties(task, taskReturnDTO);
                taskReturnDTO.setRequestId(task.getId());
                taskReturnDTO.setLostAge(Period.between(task.getLostBirth().toLocalDate(), LocalDate.now()).getYears());
                taskReturnDTO.setLostStatus(LostStatus.nameOf(task.getLostStatus()));

                taskDTOList.add(taskReturnDTO);
            }
            userReturnDTO.setCases(taskDTOList);
            //将当前用户DTO放入结果列表
            userDTOList.add(userReturnDTO);
        }
    }
}



