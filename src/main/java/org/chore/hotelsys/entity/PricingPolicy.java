package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("pricing_policy")
public class PricingPolicy {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String roomType;
    private BigDecimal basePrice;
    private BigDecimal weekendMultiplier;
    private BigDecimal holidayMultiplier;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer priority;
    private String status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
