package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "orders", autoResultMap = true)
public class Order {
    @TableId(type = IdType.INPUT)
    private String id;
    private Long reservationId;
    private Long customerId;
    private String customerName;
    private String roomNum;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String status;
    private String paymentMethod;
    private BigDecimal totalAmount;
    private BigDecimal deposit;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<ExtraCharge> extraCharges;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Data
    public static class ExtraCharge {
        private String item;
        private BigDecimal amount;
    }
}

