package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.RoomInfo;
import org.chore.hotelsys.mapper.RoomInfoMapper;
import org.springframework.stereotype.Service;

/**
 * 客房信息 Service
 */
@Service
public class RoomInfoService extends ServiceImpl<RoomInfoMapper, RoomInfo> {
}
