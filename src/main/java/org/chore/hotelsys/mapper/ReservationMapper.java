package org.chore.hotelsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.chore.hotelsys.entity.Reservation;

@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {
}

