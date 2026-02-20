package org.chore.hotelsys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chore.hotelsys.entity.Inventory;
import org.chore.hotelsys.mapper.InventoryMapper;
import org.springframework.stereotype.Service;

@Service
public class InventoryService extends ServiceImpl<InventoryMapper, Inventory> {
}

