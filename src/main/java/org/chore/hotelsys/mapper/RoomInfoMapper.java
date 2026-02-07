package org.chore.hotelsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.chore.hotelsys.entity.RoomInfo;

/**
 * 客房信息 Mapper
 */
@Mapper
public interface RoomInfoMapper extends BaseMapper<RoomInfo> {
}
