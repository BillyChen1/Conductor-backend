package com.chen.conductorbackend.mapper;

import com.chen.conductorbackend.dto.TaskReturnDTO;
import com.chen.conductorbackend.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    /**
     * 根据uid和任务状态获取任务列表
     * @param uid
     * @param status
     * @return
     */
    List<Task> listTasksByUidAndStatus(@Param("uid") Integer uid, @Param("status") Integer status);

}
