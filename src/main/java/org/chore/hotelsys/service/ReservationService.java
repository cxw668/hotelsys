package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.Reservation;
import org.chore.hotelsys.mapper.ReservationMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationService extends ServiceImpl<ReservationMapper, Reservation> {
}

