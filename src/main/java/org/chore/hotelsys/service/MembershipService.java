package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.Membership;
import org.chore.hotelsys.mapper.MembershipMapper;
import org.springframework.stereotype.Service;

@Service
public class MembershipService extends ServiceImpl<MembershipMapper, Membership> {
}

