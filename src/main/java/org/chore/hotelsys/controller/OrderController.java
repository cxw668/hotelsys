package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.Order;
import org.chore.hotelsys.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public R<List<Order>> list() {
        return R.success("查询成功", orderService.list());
    }

    @GetMapping("/{id}")
    public R<Order> getById(@PathVariable String id) {
        return R.success("查询成功", orderService.getById(id));
    }

    @PostMapping
    public R<Order> create(@RequestBody Order order) {
        boolean saved = orderService.save(order);
        return saved ? R.success("创建成功", order) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable String id, @RequestBody Order order) {
        order.setId(id);
        boolean updated = orderService.updateById(order);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable String id) {
        boolean removed = orderService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }
}
