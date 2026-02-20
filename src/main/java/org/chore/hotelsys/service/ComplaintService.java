package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.Complaint;
import org.chore.hotelsys.mapper.ComplaintMapper;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService extends ServiceImpl<ComplaintMapper, Complaint> {
}

