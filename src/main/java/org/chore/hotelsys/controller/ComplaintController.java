package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.Complaint;
import org.chore.hotelsys.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping
    public R<List<Complaint>> list() {
        return R.success("查询成功", complaintService.list());
    }

    @GetMapping("/{id}")
    public R<Complaint> getById(@PathVariable Long id) {
        return R.success("查询成功", complaintService.getById(id));
    }

    @PostMapping
    public R<Complaint> create(@RequestBody Complaint complaint) {
        boolean saved = complaintService.save(complaint);
        return saved ? R.success("创建成功", complaint) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Complaint complaint) {
        complaint.setId(id);
        boolean updated = complaintService.updateById(complaint);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean removed = complaintService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }
}

