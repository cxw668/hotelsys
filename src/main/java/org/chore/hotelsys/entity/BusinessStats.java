package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 经营数据统计实体
 */
@Data
@TableName("business_stats")
public class BusinessStats {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 统计日期
     */
    private LocalDate date;

    private BigDecimal totalRevenue;

    private BigDecimal roomRevenue;

    private BigDecimal foodRevenue;

    private BigDecimal otherRevenue;

    private BigDecimal occupancyRate;

    private BigDecimal revPAR;

    private BigDecimal adr;

    private Integer checkIns;

    private Integer checkOuts;

    private Integer cancellations;

    private Integer guestCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
