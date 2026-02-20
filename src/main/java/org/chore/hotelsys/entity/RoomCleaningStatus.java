package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("room_cleaning_status")
public class RoomCleaningStatus {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String roomNum;
    private String status;
    private String cleaner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String inspector;
    private String result;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
