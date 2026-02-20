package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.Checkout;
import org.chore.hotelsys.mapper.CheckoutMapper;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService extends ServiceImpl<CheckoutMapper, Checkout> {
}

