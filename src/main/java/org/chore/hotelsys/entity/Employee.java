package org.chore.hotelsys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "employee", autoResultMap = true)
public class Employee {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String employeeId;
    private String position;
    private String department;
    private LocalDate hireDate;
    private String phone;
    private String salaryLevel;
    private String performance;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> roles;
    private String status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
