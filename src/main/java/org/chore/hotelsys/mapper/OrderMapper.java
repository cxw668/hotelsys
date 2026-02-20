package org.chore.hotelsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.chore.hotelsys.entity.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}

