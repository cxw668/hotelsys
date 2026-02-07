package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 客房基础信息实体
 */
@Data
@TableName("room_info")
public class RoomInfo {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String roomNum;

    private String roomType;

    /**
     * 房态：0-空闲 1-入住 2-维修
     */
    private Integer roomStatus;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
