package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.BusinessStats;
import org.chore.hotelsys.mapper.BusinessStatsMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 经营数据统计 Service
 */
@Service
public class BusinessStatsService extends ServiceImpl<BusinessStatsMapper, BusinessStats> {

    public BusinessStats getByDate(LocalDate date) {
        return this.getOne(new LambdaQueryWrapper<BusinessStats>()
                .eq(BusinessStats::getStatDate, date));
    }

    public boolean saveOrUpdateByDate(BusinessStats stats) {
        BusinessStats existing = getByDate(stats.getStatDate());
        if (existing != null) {
            stats.setId(existing.getId());
            return this.updateById(stats);
        } else {
            return this.save(stats);
        }
    }
}
