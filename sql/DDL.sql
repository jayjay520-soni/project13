-- =========================================================================
-- 一体式旅游管理系统 — 数据库建表脚本 (schema.sql)
-- 数据库版本要求：MySQL 8.0+，字符集 utf8mb4，排序规则 utf8mb4_general_ci
-- 对应实体类：
--   User           -> `user` 表
--   ScenicSpot     -> `scenic_spot` 表
--   Homestay       -> `homestay` 表
--   Restaurant     -> `restaurant` 表
-- 使用方式：
--   mysql -uroot -p < schema.sql     （如果 travel_manage 库不存在，会自动创建）
-- =========================================================================

-- ---------- 1. 建库 ----------
CREATE DATABASE IF NOT EXISTS `travel_manage`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE `travel_manage`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================================
-- 2. 用户表 user
--    字段对应 com.example.travelmanage.entity.User
-- =========================================================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id`               INT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `username`         VARCHAR(50)  NOT NULL                COMMENT '用户名（登录用，唯一）',
                        `password`         VARCHAR(255) NOT NULL                COMMENT '密码（BCrypt 加密存储，不存明文）',
                        `role`             VARCHAR(20)  NOT NULL DEFAULT 'user' COMMENT '角色：admin-管理员 / user-普通用户',
                        `status`           TINYINT      NOT NULL DEFAULT 1      COMMENT '状态：1-启用 / 0-禁用',
                        `phone`            VARCHAR(20)           DEFAULT NULL   COMMENT '手机号（注册时选填）',
                        `create_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `last_login_time`  DATETIME               DEFAULT NULL   COMMENT '最近一次登录时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_username` (`username`),
                        KEY `idx_role`     (`role`),
                        KEY `idx_status`   (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表（管理员+普通用户）';


-- =========================================================================
-- 3. 景区表 scenic_spot
--    字段对应 com.example.travelmanage.entity.ScenicSpot
-- =========================================================================
DROP TABLE IF EXISTS `scenic_spot`;
CREATE TABLE `scenic_spot` (
                               `id`           INT             NOT NULL AUTO_INCREMENT COMMENT '景区ID',
                               `name`         VARCHAR(100)    NOT NULL                COMMENT '景区名称',
                               `price`        DOUBLE(10,2)    NOT NULL DEFAULT 0.00   COMMENT '门票价格（元）',
                               `address`      VARCHAR(255)    NOT NULL                COMMENT '详细地址',
                               `region`       VARCHAR(50)     NOT NULL                COMMENT '所在地区（如：厦门、泉州、福州）',
                               `popularity`   DOUBLE(3,1)     NOT NULL DEFAULT 0.0    COMMENT '热度：0.0 ~ 10.0',
                               `level`        VARCHAR(10)              DEFAULT NULL   COMMENT '景区等级：如 5A / 4A / 3A',
                               `visit_count`  INT             NOT NULL DEFAULT 0      COMMENT '累计游量（人次）',
                               `image_url`    VARCHAR(500)             DEFAULT NULL   COMMENT '景区主图URL',
                               `history`      TEXT                      DEFAULT NULL   COMMENT '历史/简介（长文本）',
                               `create_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`),
                               KEY `idx_region`  (`region`),
                               KEY `idx_level`   (`level`),
                               KEY `idx_name`    (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='景区表';


-- =========================================================================
-- 4. 民宿表 homestay
--    字段对应 com.example.travelmanage.entity.Homestay
--    注意：price 使用 DECIMAL 对应 Java 的 BigDecimal
-- =========================================================================
DROP TABLE IF EXISTS `homestay`;
CREATE TABLE `homestay` (
                            `id`           INT             NOT NULL AUTO_INCREMENT COMMENT '民宿ID',
                            `name`         VARCHAR(100)    NOT NULL                COMMENT '民宿名称',
                            `price`        DECIMAL(10,2)   NOT NULL DEFAULT 0.00   COMMENT '每晚价格（元，BigDecimal 精度）',
                            `address`      VARCHAR(255)    NOT NULL                COMMENT '详细地址',
                            `region`       VARCHAR(50)     NOT NULL                COMMENT '所在地区',
                            `score`        DOUBLE(2,1)     NOT NULL DEFAULT 0.0    COMMENT '用户评分：0.0 ~ 5.0',
                            `type`         VARCHAR(50)              DEFAULT NULL   COMMENT '房型：大床房 / 亲子房 / 标准间 等',
                            `capacity`     INT             NOT NULL DEFAULT 1      COMMENT '可住人数',
                            `description`  TEXT            NOT NULL                COMMENT '民宿介绍描述',
                            `image_url`    VARCHAR(500)             DEFAULT NULL   COMMENT '民宿主图URL',
                            `create_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            KEY `idx_region` (`region`),
                            KEY `idx_type`   (`type`),
                            KEY `idx_score`  (`score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='民宿/酒店表';


-- =========================================================================
-- 5. 餐厅表 restaurant
--    字段对应 com.example.travelmanage.entity.Restaurant
--    注意：price 使用 DECIMAL 对应 Java 的 BigDecimal
-- =========================================================================
DROP TABLE IF EXISTS `restaurant`;
CREATE TABLE `restaurant` (
                              `id`           INT             NOT NULL AUTO_INCREMENT COMMENT '餐厅ID',
                              `name`         VARCHAR(100)    NOT NULL                COMMENT '餐厅名称',
                              `price`        DECIMAL(10,2)   NOT NULL DEFAULT 0.00   COMMENT '人均消费（元）',
                              `address`      VARCHAR(255)    NOT NULL                COMMENT '详细地址',
                              `region`       VARCHAR(50)     NOT NULL                COMMENT '所在地区',
                              `score`        DOUBLE(2,1)     NOT NULL DEFAULT 0.0    COMMENT '评分：0.0 ~ 5.0',
                              `type`         VARCHAR(50)              DEFAULT NULL   COMMENT '菜系：川菜 / 粤菜 / 闽南菜 / 湘菜 等',
                              `sales`        INT             NOT NULL DEFAULT 0      COMMENT '累计销量/订单数',
                              `description`  TEXT            NOT NULL                COMMENT '餐厅介绍',
                              `image_url`    VARCHAR(500)             DEFAULT NULL   COMMENT '餐厅主图URL',
                              `create_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              KEY `idx_region` (`region`),
                              KEY `idx_type`   (`type`),
                              KEY `idx_sales`  (`sales`),
                              KEY `idx_score`  (`score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='餐厅/美食表';


SET FOREIGN_KEY_CHECKS = 1;

-- =========================================================================
-- 建表脚本结束。接下来请执行 data.sql 填充测试数据，
-- 或者直接执行 init.sql（它 = schema.sql + data.sql 合并的一键版）。
-- =========================================================================
