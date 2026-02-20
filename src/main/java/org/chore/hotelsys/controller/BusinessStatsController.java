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

    @GetMapping
    public R<java.util.List<BusinessStats>> list(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            return R.success("查询成功", businessStatsService.list());
        }
        return R.success("查询成功", businessStatsService.listByDate(date));
    }

    @GetMapping("/{id}")
    public R<BusinessStats> getById(@PathVariable Long id) {
        return R.success("查询成功", businessStatsService.getById(id));
    }

    @PostMapping
    public R<Void> create(@RequestBody BusinessStats stats) {
        if (stats.getDate() == null) {
            return R.error("统计日期不能为空");
        }
        boolean success = businessStatsService.save(stats);
        return success ? R.success("数据保存成功", null) : R.error("数据保存失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody BusinessStats stats) {
        stats.setId(id);
        boolean success = businessStatsService.updateById(stats);
        return success ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean success = businessStatsService.removeById(id);
        return success ? R.success("删除成功", null) : R.error("删除失败");
    }

    @GetMapping("/recent-7-days")
    public R<java.util.List<BusinessStats>> recent7Days() {
        return R.success("查询成功", businessStatsService.listRecentDays(7));
    }

    @GetMapping("/query-by-date")
    public R<BusinessStats> legacyQueryByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        BusinessStats stats = businessStatsService.getByDate(date);
        return R.success("查询成功", stats);
    }

    @PostMapping("/save-or-update")
    public R<Void> legacySaveOrUpdate(@RequestBody BusinessStats stats) {
        if (stats.getDate() == null) {
            return R.error("统计日期不能为空");
        }
        boolean success = businessStatsService.saveOrUpdateByDate(stats);
        return success ? R.success("数据保存成功", null) : R.error("数据保存失败");
    }
}
