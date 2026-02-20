package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("checkout")
public class Checkout {
    @TableId(type = IdType.INPUT)
    private String id;
    private String orderId;
    private String customerName;
    private String roomNum;
    private LocalDateTime checkoutTime;
    private BigDecimal totalRoomCharge;
    private BigDecimal otherCharges;
    private BigDecimal compensation;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String paymentMethod;
    private String operator;
    private String status;
    private String remarks;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
