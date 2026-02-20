package org.chore.hotelsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.chore.hotelsys.entity.PricingPolicy;

@Mapper
public interface PricingPolicyMapper extends BaseMapper<PricingPolicy> {
}

