package com.chen.conductorbackend.service.impl;

import com.chen.conductorbackend.dto.TaskReturnDTO;
import com.chen.conductorbackend.dto.UserReturnDTO;
import com.chen.conductorbackend.entity.Task;
import com.chen.conductorbackend.entity.User;
import com.chen.conductorbackend.enums.LostStatus;
import com.chen.conductorbackend.mapper.UserMapper;
import com.chen.conductorbackend.service.IUserService;
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
        List<User> users = userMapper.listAllUserInfo();

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



