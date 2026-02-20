package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.Membership;
import org.chore.hotelsys.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public R<List<Membership>> list() {
        return R.success("查询成功", membershipService.list());
    }

    @GetMapping("/{id}")
    public R<Membership> getById(@PathVariable Long id) {
        return R.success("查询成功", membershipService.getById(id));
    }

    @PostMapping
    public R<Membership> create(@RequestBody Membership membership) {
        boolean saved = membershipService.save(membership);
        return saved ? R.success("创建成功", membership) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Membership membership) {
        membership.setId(id);
        boolean updated = membershipService.updateById(membership);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean removed = membershipService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }
}

