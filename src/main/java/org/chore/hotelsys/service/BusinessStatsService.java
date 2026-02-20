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
                .eq(BusinessStats::getDate, date));
    }

    public boolean saveOrUpdateByDate(BusinessStats stats) {
        BusinessStats existing = getByDate(stats.getDate());
        if (existing != null) {
            stats.setId(existing.getId());
            return this.updateById(stats);
        } else {
            return this.save(stats);
        }
    }

    public java.util.List<BusinessStats> listByDate(LocalDate date) {
        return this.list(new LambdaQueryWrapper<BusinessStats>()
                .eq(BusinessStats::getDate, date));
    }

    public java.util.List<BusinessStats> listRecentDays(int days) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(days - 1);
        return this.list(new LambdaQueryWrapper<BusinessStats>()
                .ge(BusinessStats::getDate, start)
                .le(BusinessStats::getDate, end));
    }
}
