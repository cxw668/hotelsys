package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.entity.Employee;
import org.chore.hotelsys.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public R<List<Employee>> list() {
        return R.success("查询成功", employeeService.list());
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        return R.success("查询成功", employeeService.getById(id));
    }

    @PostMapping
    public R<Employee> create(@RequestBody Employee employee) {
        boolean saved = employeeService.save(employee);
        return saved ? R.success("创建成功", employee) : R.error("创建失败");
    }

    @PatchMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        boolean updated = employeeService.updateById(employee);
        return updated ? R.success("更新成功", null) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean removed = employeeService.removeById(id);
        return removed ? R.success("删除成功", null) : R.error("删除失败");
    }
}

