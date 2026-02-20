package org.chore.hotelsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.chore.hotelsys.entity.Membership;

@Mapper
public interface MembershipMapper extends BaseMapper<Membership> {
}

