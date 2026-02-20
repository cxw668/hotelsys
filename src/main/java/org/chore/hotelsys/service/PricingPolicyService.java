package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.PricingPolicy;
import org.chore.hotelsys.mapper.PricingPolicyMapper;
import org.springframework.stereotype.Service;

@Service
public class PricingPolicyService extends ServiceImpl<PricingPolicyMapper, PricingPolicy> {
}

