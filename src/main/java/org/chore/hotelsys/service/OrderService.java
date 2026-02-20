package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.Order;
import org.chore.hotelsys.mapper.OrderMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {
}

