package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.Checkout;
import org.chore.hotelsys.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkouts")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @GetMapping
    public R<List<Checkout>> list() {
        return R.success("查询成功", checkoutService.list());
    }

    @GetMapping("/{id}")
    public R<Checkout> getById(@PathVariable String id) {
        return R.success("查询成功", checkoutService.getById(id));
    }

    @PostMapping
    public R<Checkout> create(@RequestBody Checkout checkout) {
        boolean saved = checkoutService.save(checkout);
        return saved ? R.success("创建成功", checkout) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable String id, @RequestBody Checkout checkout) {
        checkout.setId(id);
        boolean updated = checkoutService.updateById(checkout);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable String id) {
        boolean removed = checkoutService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }
}
