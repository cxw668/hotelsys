package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.Inventory;
import org.chore.hotelsys.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public R<List<Inventory>> list() {
        return R.success("查询成功", inventoryService.list());
    }

    @GetMapping("/{id}")
    public R<Inventory> getById(@PathVariable Long id) {
        return R.success("查询成功", inventoryService.getById(id));
    }

    @PostMapping
    public R<Inventory> create(@RequestBody Inventory inventory) {
        boolean saved = inventoryService.save(inventory);
        return saved ? R.success("创建成功", inventory) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventory.setId(id);
        boolean updated = inventoryService.updateById(inventory);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean removed = inventoryService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }
}

