package com.chen.conductorbackend.mapper;

import com.chen.conductorbackend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

}
