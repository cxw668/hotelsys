package org.chore.hotelsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.chore.hotelsys.entity.BusinessStats;

/**
 * 经营数据统计 Mapper
 */
@Mapper
public interface BusinessStatsMapper extends BaseMapper<BusinessStats> {
}
