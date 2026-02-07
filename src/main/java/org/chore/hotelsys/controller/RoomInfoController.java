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
@RequestMapping("/api/room")
public class RoomInfoController {

    @Autowired
    private RoomInfoService roomInfoService;

    /**
     * 查询所有客房信息
     */
    @GetMapping("/list")
    public R<List<RoomInfo>> list() {
        return R.success("查询成功", roomInfoService.list());
    }

    /**
     * 修改客房状态
     */
    @PostMapping("/update-status")
    public R<Void> updateStatus(@RequestBody RoomInfo roomInfo) {
        if (roomInfo.getId() == null || roomInfo.getRoomStatus() == null) {
            return R.error("参数缺失");
        }
        boolean updated = roomInfoService.updateById(roomInfo);
        return updated ? R.success("房态修改成功", null) : R.error("房态修改失败");
    }
}
