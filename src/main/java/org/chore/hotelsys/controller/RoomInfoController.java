package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.RoomInfo;
import org.chore.hotelsys.service.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客房管理控制器
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomInfoController {

    @Autowired
    private RoomInfoService roomInfoService;

    @GetMapping("/{id}")
    public R<RoomInfo> getById(@PathVariable Long id) {
        RoomInfo info = roomInfoService.getById(id);
        return R.success("查询成功", info);
    }

    @PostMapping
    public R<RoomInfo> create(@RequestBody RoomInfo roomInfo) {
        boolean saved = roomInfoService.save(roomInfo);
        return saved ? R.success("创建成功", roomInfo) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> updatePartial(@PathVariable Long id, @RequestBody RoomInfo patch) {
        patch.setId(id);
        boolean updated = roomInfoService.updateById(patch);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean removed = roomInfoService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }

    /**
     * 查询所有客房信息
     */
    @GetMapping
    public R<List<RoomInfo>> list() {
        return R.success("查询成功", roomInfoService.list());
    }

    /**
     * 修改客房状态
     */
    @GetMapping("/list")
    public R<List<RoomInfo>> legacyList() { return list(); }
    @PostMapping("/update-status")
    public R<Void> legacyUpdateStatus(@RequestBody RoomInfo roomInfo) {
        if (roomInfo.getId() == null || roomInfo.getRoomStatus() == null) {
            return R.error("参数缺失");
        }
        boolean updated = roomInfoService.updateById(roomInfo);
        return updated ? R.success("房态修改成功", null) : R.error("房态修改失败");
    }
}
