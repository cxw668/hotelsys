package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.Customer;
import org.chore.hotelsys.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends ServiceImpl<CustomerMapper, Customer> {
}

