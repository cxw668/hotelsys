-- 数据库初始化脚本

-- 1. 客房基础信息表 (room_info)
CREATE TABLE IF NOT EXISTS room_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    room_num VARCHAR(20) NOT NULL COMMENT '房间号',
    room_type VARCHAR(50) NOT NULL DEFAULT '单人间' COMMENT '房型',
    room_status TINYINT NOT NULL DEFAULT 0 COMMENT '房态：0-空闲 1-入住 2-维修',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY idx_room_num (room_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客房基础信息表';

-- 2. 经营数据统计表 (business_stats)
CREATE TABLE IF NOT EXISTS business_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    stat_date DATE NOT NULL COMMENT '统计日期',
    check_in_num INT NOT NULL DEFAULT 0 COMMENT '当日入住房间数',
    revenue DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '当日营收',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_stat_date (stat_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='经营数据统计表';

-- 3. 退房结算表 (checkout)
CREATE TABLE IF NOT EXISTS checkout (
    id VARCHAR(32) PRIMARY KEY COMMENT '主键ID',
    order_id VARCHAR(32) COMMENT '关联订单ID',
    customer_name VARCHAR(50) COMMENT '客户姓名',
    room_num VARCHAR(20) COMMENT '房间号',
    checkout_time DATETIME COMMENT '退房时间',
    total_room_charge DECIMAL(10,2) DEFAULT 0.00 COMMENT '总房费',
    other_charges DECIMAL(10,2) DEFAULT 0.00 COMMENT '其他费用',
    compensation DECIMAL(10,2) DEFAULT 0.00 COMMENT '赔偿金',
    discount DECIMAL(10,2) DEFAULT 0.00 COMMENT '折扣金额',
    final_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '实收金额',
    payment_method VARCHAR(20) COMMENT '支付方式',
    operator VARCHAR(50) COMMENT '操作员',
    status VARCHAR(20) COMMENT '状态',
    remarks VARCHAR(255) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退房结算表';

-- 4. 投诉报修表 (complaint)
CREATE TABLE IF NOT EXISTS complaint (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    customer_id BIGINT COMMENT '客户ID',
    room_num VARCHAR(20) COMMENT '房间号',
    type VARCHAR(20) COMMENT '投诉类型',
    content TEXT COMMENT '投诉内容',
    report_time DATETIME COMMENT '上报时间',
    status VARCHAR(20) COMMENT '处理状态',
    result VARCHAR(255) COMMENT '处理结果',
    handler VARCHAR(50) COMMENT '处理人',
    score INT COMMENT '评分',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投诉报修表';

-- 5. 客户信息表 (customer)
CREATE TABLE IF NOT EXISTS customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) COMMENT '性别',
    id_card VARCHAR(20) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(50) COMMENT '邮箱',
    level VARCHAR(20) COMMENT '会员等级',
    total_consumption DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计消费',
    stay_count INT DEFAULT 0 COMMENT '入住次数',
    last_stay_date DATE COMMENT '最近入住日期',
    source VARCHAR(20) COMMENT '来源',
    tags JSON COMMENT '客户标签',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户信息表';

-- 6. 员工信息表 (employee)
CREATE TABLE IF NOT EXISTS employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    employee_id VARCHAR(20) NOT NULL COMMENT '工号',
    position VARCHAR(50) COMMENT '职位',
    department VARCHAR(50) COMMENT '部门',
    hire_date DATE COMMENT '入职日期',
    phone VARCHAR(20) COMMENT '手机号',
    salary_level VARCHAR(20) COMMENT '薪资等级',
    performance VARCHAR(10) COMMENT '绩效考核',
    roles JSON COMMENT '角色列表',
    status VARCHAR(20) COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY idx_employee_id (employee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息表';

-- 7. 库存物资表 (inventory)
CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '物品名称',
    category VARCHAR(20) COMMENT '分类',
    spec VARCHAR(50) COMMENT '规格',
    unit VARCHAR(10) COMMENT '单位',
    current_stock INT DEFAULT 0 COMMENT '当前库存',
    safety_stock INT DEFAULT 0 COMMENT '安全库存',
    price DECIMAL(10,2) DEFAULT 0.00 COMMENT '单价',
    supplier VARCHAR(100) COMMENT '供应商',
    last_check_time DATE COMMENT '最近盘点时间',
    location VARCHAR(50) COMMENT '存放位置',
    remark VARCHAR(255) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存物资表';

-- 8. 会员等级表 (membership)
CREATE TABLE IF NOT EXISTS membership (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    level_name VARCHAR(20) NOT NULL COMMENT '等级名称',
    discount_rate DECIMAL(3,2) DEFAULT 1.00 COMMENT '折扣率',
    points_multiplier DECIMAL(3,2) DEFAULT 1.00 COMMENT '积分倍率',
    upgrade_threshold INT DEFAULT 0 COMMENT '升级门槛(消费额或积分)',
    benefits JSON COMMENT '权益列表',
    color VARCHAR(20) COMMENT '标识颜色',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员等级表';

-- 9. 订单表 (orders)
CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(32) PRIMARY KEY COMMENT '订单ID',
    reservation_id BIGINT COMMENT '关联预订ID',
    customer_id BIGINT COMMENT '客户ID',
    customer_name VARCHAR(50) COMMENT '客户姓名',
    room_num VARCHAR(20) COMMENT '房间号',
    check_in_time DATETIME COMMENT '入住时间',
    check_out_time DATETIME COMMENT '退房时间',
    status VARCHAR(20) COMMENT '订单状态',
    payment_method VARCHAR(20) COMMENT '支付方式',
    total_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '总金额',
    deposit DECIMAL(10,2) DEFAULT 0.00 COMMENT '押金',
    extra_charges JSON COMMENT '额外费用明细',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 10. 价格策略表 (pricing_policy)
CREATE TABLE IF NOT EXISTS pricing_policy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '策略名称',
    room_type VARCHAR(50) COMMENT '适用房型',
    base_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '基础价格',
    weekend_multiplier DECIMAL(3,2) DEFAULT 1.00 COMMENT '周末倍率',
    holiday_multiplier DECIMAL(3,2) DEFAULT 1.00 COMMENT '节假日倍率',
    start_date DATE COMMENT '生效开始日期',
    end_date DATE COMMENT '生效结束日期',
    priority INT DEFAULT 0 COMMENT '优先级',
    status VARCHAR(20) COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='价格策略表';

-- 11. 客房预订表 (reservation)
CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    customer_id BIGINT COMMENT '客户ID',
    customer_name VARCHAR(50) COMMENT '客户姓名',
    phone VARCHAR(20) COMMENT '联系电话',
    room_type VARCHAR(50) COMMENT '预订房型',
    room_num VARCHAR(20) COMMENT '预订房间号(可选)',
    source VARCHAR(20) COMMENT '订单来源',
    booking_time DATETIME COMMENT '预订时间',
    check_in_date DATE COMMENT '入住日期',
    check_out_date DATE COMMENT '离店日期',
    days INT DEFAULT 1 COMMENT '入住天数',
    total_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '预估总价',
    status VARCHAR(20) COMMENT '预订状态',
    payment_status VARCHAR(20) COMMENT '支付状态',
    remark VARCHAR(255) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客房预订表';

-- 12. 客房清洁状态表 (room_cleaning_status)
CREATE TABLE IF NOT EXISTS room_cleaning_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    room_num VARCHAR(20) NOT NULL COMMENT '房间号',
    status VARCHAR(20) COMMENT '清洁状态',
    cleaner VARCHAR(50) COMMENT '保洁员',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    inspector VARCHAR(50) COMMENT '检查员',
    result VARCHAR(20) COMMENT '检查结果',
    remark VARCHAR(255) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客房清洁状态表';
