package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 客房基础信息实体
 */
@Data
@TableName(value = "room_info", autoResultMap = true)
public class RoomInfo {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String roomNum;

    private String roomType;

    /**
     * 房态：0-空闲 1-入住 2-维修
     */
    private Integer roomStatus;

    private Integer floor;

    private String direction;

    private Boolean hasWindow;

    private Integer area;

    private String bedType;

    private BigDecimal price;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> features;

    private LocalDateTime lastCleaningTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
