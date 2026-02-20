package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.PricingPolicy;
import org.chore.hotelsys.service.PricingPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing-policies")
public class PricingPolicyController {

    @Autowired
    private PricingPolicyService pricingPolicyService;

    @GetMapping
    public R<List<PricingPolicy>> list() {
        return R.success("查询成功", pricingPolicyService.list());
    }

    @GetMapping("/{id}")
    public R<PricingPolicy> getById(@PathVariable Long id) {
        return R.success("查询成功", pricingPolicyService.getById(id));
    }

    @PostMapping
    public R<PricingPolicy> create(@RequestBody PricingPolicy pricingPolicy) {
        boolean saved = pricingPolicyService.save(pricingPolicy);
        return saved ? R.success("创建成功", pricingPolicy) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody PricingPolicy pricingPolicy) {
        pricingPolicy.setId(id);
        boolean updated = pricingPolicyService.updateById(pricingPolicy);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean removed = pricingPolicyService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }
}

