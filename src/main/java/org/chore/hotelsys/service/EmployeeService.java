package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.Employee;
import org.chore.hotelsys.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends ServiceImpl<EmployeeMapper, Employee> {
}

