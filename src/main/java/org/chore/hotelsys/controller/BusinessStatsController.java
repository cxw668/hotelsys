package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.BusinessStats;
import org.chore.hotelsys.service.BusinessStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 经营数据统计控制器
 */
@RestController
@RequestMapping("/api/stats")
public class BusinessStatsController {

    @Autowired
    private BusinessStatsService businessStatsService;

    /**
     * 查询指定日期经营数据
     */
    @GetMapping("/query-by-date")
    public R<BusinessStats> queryByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        BusinessStats stats = businessStatsService.getByDate(date);
        return R.success("查询成功", stats);
    }

    /**
     * 新增/更新经营数据
     */
    @PostMapping("/save-or-update")
    public R<Void> saveOrUpdate(@RequestBody BusinessStats stats) {
        if (stats.getStatDate() == null) {
            return R.error("统计日期不能为空");
        }
        boolean success = businessStatsService.saveOrUpdateByDate(stats);
        return success ? R.success("数据保存成功", null) : R.error("数据保存失败");
    }
}
