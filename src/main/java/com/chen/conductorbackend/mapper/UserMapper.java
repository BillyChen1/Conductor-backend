package com.chen.conductorbackend.mapper;

import com.chen.conductorbackend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2021-03-25
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 返回所有的用户，用户中的tasks为该队员接手的且正在进行的任务
     * @return
     */
    List<User> listAllUserInfo();

    /**
     * 获得当前用户的队友
     * @param uid
     * @return
     */
    List<User> listPartnersByUid(@Param("uid") Integer uid);
}
