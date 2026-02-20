package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "membership", autoResultMap = true)
public class Membership {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String levelName;
    private BigDecimal discountRate;
    private BigDecimal pointsMultiplier;
    private Integer upgradeThreshold;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> benefits;
    private String color;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
