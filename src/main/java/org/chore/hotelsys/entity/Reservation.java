package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("reservation")
public class Reservation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private String customerName;
    private String phone;
    private String roomType;
    private String roomNum;
    private String source;
    private LocalDateTime bookingTime;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer days;
    private BigDecimal totalPrice;
    private String status;
    private String paymentStatus;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
