package com.chen.conductorbackend.mapper;

import com.chen.conductorbackend.entity.UserTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 队员-救援任务关系表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2021-04-12
 */
@Mapper
public interface UserTaskMapper extends BaseMapper<UserTask> {

}
