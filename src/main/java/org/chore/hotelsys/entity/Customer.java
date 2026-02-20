package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "customer", autoResultMap = true)
public class Customer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String gender;
    private String idCard;
    private String phone;
    private String email;
    private String level;
    private BigDecimal totalConsumption;
    private Integer stayCount;
    private LocalDate lastStayDate;
    private String source;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
