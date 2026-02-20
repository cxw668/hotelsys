package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.Customer;
import org.chore.hotelsys.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public R<List<Customer>> list() {
        return R.success("查询成功", customerService.list());
    }

    @GetMapping("/{id}")
    public R<Customer> getById(@PathVariable Long id) {
        return R.success("查询成功", customerService.getById(id));
    }

    @PostMapping
    public R<Customer> create(@RequestBody Customer customer) {
        boolean saved = customerService.save(customer);
        return saved ? R.success("创建成功", customer) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        boolean updated = customerService.updateById(customer);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean removed = customerService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }
}

