package com.chen.conductorbackend.mapper;

import com.chen.conductorbackend.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 救援请求表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2021-03-25
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

}
