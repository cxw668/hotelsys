package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("inventory")
public class Inventory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String category;
    private String spec;
    private String unit;
    private Integer currentStock;
    private Integer safetyStock;
    private BigDecimal price;
    private String supplier;
    private LocalDate lastCheckTime;
    private String location;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
