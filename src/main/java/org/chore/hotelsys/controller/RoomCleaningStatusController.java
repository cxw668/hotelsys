package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.RoomCleaningStatus;
import org.chore.hotelsys.service.RoomCleaningStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-cleaning-statuses")
public class RoomCleaningStatusController {

    @Autowired
    private RoomCleaningStatusService roomCleaningStatusService;

    @GetMapping
    public R<List<RoomCleaningStatus>> list() {
        return R.success("查询成功", roomCleaningStatusService.list());
    }

    @GetMapping("/{id}")
    public R<RoomCleaningStatus> getById(@PathVariable Long id) {
        return R.success("查询成功", roomCleaningStatusService.getById(id));
    }

    @PostMapping
    public R<RoomCleaningStatus> create(@RequestBody RoomCleaningStatus status) {
        boolean saved = roomCleaningStatusService.save(status);
        return saved ? R.success("创建成功", status) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody RoomCleaningStatus status) {
        status.setId(id);
        boolean updated = roomCleaningStatusService.updateById(status);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean removed = roomCleaningStatusService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }
}

