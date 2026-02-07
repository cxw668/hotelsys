# Hotelsys 项目技术实现文档

## 1. 项目概况
本项目是一个基于 Spring Boot 3.4.2 开发的轻量级酒店管理系统后端，集成了 MyBatis-Plus 3.5.10.1 进行数据持久化，使用 MySQL 8.0 作为数据库。

## 2. 技术栈
- **核心框架**：Spring Boot 3.4.2
- **ORM 框架**：MyBatis-Plus 3.5.10.1
- **数据库**：MySQL 8.0
- **开发工具**：Lombok, Jakarta Validation
- **依赖管理**：Maven

## 3. 核心功能实现

### 3.1 数据库连通性验证
为了确保服务启动时数据库状态正常，实现了 `DatabaseConnectionVerifier` 工具类，在项目启动后自动执行 JDBC 连通性测试并打印数据库版本信息。

### 3.2 统一响应结构
所有接口统一返回 `R<T>` 对象，包含以下字段：
- `code`: 状态码（0 表示成功，500 表示业务错误）
- `message`: 提示信息
- `data`: 具体的业务数据

### 3.3 自动填充
集成了 MyBatis-Plus 的 `MetaObjectHandler`，实现了对 `createTime` 和 `updateTime` 字段的自动填充，减少了手动维护时间戳的代码。

## 4. 接口清单

### 4.1 客房管理 (`/api/room`)
- `GET /list`: 获取所有客房信息列表。
- `POST /update-status`: 修改指定客房的状态（0-空闲, 1-入住, 2-维修）。

### 4.2 经营数据统计 (`/api/stats`)
- `GET /query-by-date`: 按日期查询经营数据。
- `POST /save-or-update`: 保存或更新指定日期的经营统计信息（入住数、营收）。

## 5. 部署与测试
- **配置文件**: `src/main/resources/application.properties`
- **运行方式**: 运行 `HotelsysApplication.java` 中的 `main` 方法。
- **验证**: 启动后查看控制台输出 `[HOTELSYS][DB] 数据库连接成功`。
