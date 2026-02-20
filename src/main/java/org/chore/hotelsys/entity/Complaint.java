package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("complaint")
public class Complaint {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private String roomNum;
    private String type;
    private String content;
    private LocalDateTime reportTime;
    private String status;
    private String result;
    private String handler;
    private Integer score;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
