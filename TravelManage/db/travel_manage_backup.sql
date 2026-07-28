-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: travel_manage
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `homestay`
--

DROP TABLE IF EXISTS `homestay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `homestay` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `address` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `region` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `score` double DEFAULT NULL,
  `type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `capacity` int DEFAULT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `image_url` text COLLATE utf8mb4_general_ci,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `homestay`
--

LOCK TABLES `homestay` WRITE;
/*!40000 ALTER TABLE `homestay` DISABLE KEYS */;
INSERT INTO `homestay` VALUES (1,'泉州西街古厝民宿',188.00,'泉州市鲤城区西街215号','泉州',4.8,'大床房',2,'位于西街核心地段，步行可达开元寺、钟楼，古香古色装修，温馨舒适','https://img0.baidu.com/it/u=602621368,2813334557&fm=253&fmt=auto&app=138&f=JPEG','2026-07-28 15:31:07','2026-07-28 15:31:07'),(2,'泉州清源山观景民宿',238.00,'泉州市丰泽区清源山景区脚下','泉州',4.7,'双床房',4,'靠山而建，空气清新，视野开阔，适合家庭出游、朋友结伴','https://img2.baidu.com/it/u=2075533268,3600996517&fm=253&fmt=auto&app=138&f=JPEG','2026-07-28 15:31:07','2026-07-28 15:31:07'),(3,'厦门曾厝垵临海民宿',298.00,'厦门市思明区曾厝垵教堂街','厦门',4.6,'大床房',2,'出门即是海边，文艺小清新风格，近环岛路、白城沙滩','https://img1.baidu.com/it/u=1023296717,3681212409&fm=253&fmt=auto&app=138&f=JPEG','2026-07-28 15:31:07','2026-07-28 15:31:07'),(4,'福州三坊七巷精品民宿',168.00,'福州市鼓楼区南后街128号','福州',4.5,'亲子房',3,'位于三坊七巷文化街区，古风装修，交通便利，适合亲子出行','https://img2.baidu.com/it/u=2288014266,3181112481&fm=253&fmt=auto&app=138&f=JPEG','2026-07-28 15:31:07','2026-07-28 15:31:07'),(5,'漳州东山岛海景民宿',218.00,'漳州市东山县铜陵镇团结路','漳州',4.9,'海景房',4,'直面东山岛海景，日落超美，房间干净整洁，老板热情','https://img0.baidu.com/it/u=304221447,204211447&fm=253&fmt=auto&app=138&f=JPEG','2026-07-28 15:31:07','2026-07-28 15:31:07');
/*!40000 ALTER TABLE `homestay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant`
--

DROP TABLE IF EXISTS `restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `address` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `region` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `score` double DEFAULT NULL,
  `type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sales` int DEFAULT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `image_url` text COLLATE utf8mb4_general_ci,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant`
--

LOCK TABLES `restaurant` WRITE;
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
INSERT INTO `restaurant` VALUES (1,'老成都川菜馆',88.00,'成都市锦江区总府路23号','成都',5,'川菜',320,'正宗川味老店，招牌麻婆豆腐、水煮鱼，味道地道，人气火爆','https://img0.baidu.com/it/u=2090304685,256529607&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=751','2026-07-28 11:37:25','2026-07-28 11:37:52'),(2,'广州早茶名店',68.00,'广州市天河区天河路128号','广州',4.7,'粤菜',285,'经典粤式早茶，虾饺、烧卖、凤爪样样精品，环境雅致','https://img2.baidu.com/it/u=2633924686,1420014709&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=750','2026-07-28 11:37:25','2026-07-28 11:37:25'),(3,'长沙辣椒炒肉馆',58.00,'长沙市天心区黄兴中路55号','长沙',4.6,'湘菜',410,'地道湖南口味，辣椒炒肉、剁椒鱼头是招牌，下饭神器','https://img2.baidu.com/it/u=3240587558,3928522300&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=751','2026-07-28 11:37:25','2026-07-28 11:37:25'),(4,'外滩法式餐厅',268.00,'上海市黄浦区外滩金融中心8楼','上海',4.9,'西餐',156,'高端法式料理，牛排、鹅肝、红酒搭配，适合约会宴请','https://img1.baidu.com/it/u=3040426240,3719111009&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=750','2026-07-28 11:37:25','2026-07-28 11:37:25'),(5,'北京全聚德分店',188.00,'北京市东城区王府井大街18号','北京',4.8,'北京菜',520,'正宗北京烤鸭，皮脆肉嫩，卷饼吃法经典，游客必吃','https://img0.baidu.com/it/u=2266116477,3180114702&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=751','2026-07-28 11:37:25','2026-07-28 11:37:25');
/*!40000 ALTER TABLE `restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scenic_spot`
--

DROP TABLE IF EXISTS `scenic_spot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scenic_spot` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '景点名称',
  `price` decimal(10,2) NOT NULL COMMENT '票价',
  `address` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '所在地',
  `region` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '地区（如：江苏-南京-玄武区）',
  `popularity` double NOT NULL DEFAULT '0' COMMENT '热度（0-1）',
  `level` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '等级（如：5A景区）',
  `visit_count` int NOT NULL DEFAULT '0' COMMENT '游量',
  `image_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片路径',
  `history` text COLLATE utf8mb4_general_ci COMMENT '文化历史',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scenic_spot`
--

LOCK TABLES `scenic_spot` WRITE;
/*!40000 ALTER TABLE `scenic_spot` DISABLE KEYS */;
INSERT INTO `scenic_spot` VALUES (1,'南京总统府',28.00,'江苏省南京市玄武区长江路292号','江苏-南京-玄武区',0.6,'4A景区',1888,NULL,'了解中国近代史，切记','2026-07-24 22:49:24','2026-07-28 15:41:49'),(2,'秦始皇陵博物院',150.00,'陕西省西安市临潼区秦俑路','陕西-西安-临潼区',0.88,'5A景区',9291,'https://example.com/qin.jpg','世界第八大奇迹...','2026-07-24 22:49:24','2026-07-24 22:49:24');
/*!40000 ALTER TABLE `scenic_spot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(60) NOT NULL,
  `role` varchar(10) NOT NULL DEFAULT 'user' COMMENT '角色：user-普通用户，admin-管理员',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint DEFAULT '1' COMMENT '状态：1启用 0禁用',
  `last_login_time` datetime DEFAULT NULL,
  `phone` varchar(11) NOT NULL COMMENT 'phone',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (4,'333','$2a$10$mR/uVmjdu6guNhX3fCRdAOGSV29UL9GtSNCbMkrSCp5zDemKSzaMa','user','2026-07-27 09:38:26',1,'2026-07-28 16:29:35','15634768976'),(5,'admin','$2a$10$tdYbGw2jdup0z2MP48KjkOra4jQtolTue/4zbBJKnUukQrdL9GkzO','admin','2026-07-27 10:03:44',1,'2026-07-28 16:14:20','13576539874'),(6,'111','$2a$10$pW/alOiWWAGyS7Hsv9sQ8eSjddb94kpjl5Xw30R5tCb2M9bUpX8y.','user','2026-07-27 10:29:02',1,'2026-07-28 15:55:11','13476538798'),(7,'testuser01','$2a$10$G/91/q9w6Wuj4.FfecI8Q.sU7UGFRVcrYlXmLYY9jJxyXFeYCK9bC','user','2026-07-27 10:31:06',1,'2026-07-27 10:34:11','13800138000'),(8,'222','$2a$10$Z/6KISyfYdreGw4hTEsSP.qfhK5yzbnon59KMcDnOhozfd1ZUKTrq','user','2026-07-27 10:39:16',1,NULL,'15387653462'),(9,'444','$2a$10$Gx89BmOWm9xyTx3Y8PIdFOTmR3x5WTx6TFcPSv3.rkZxFYZp179uW','user','2026-07-28 09:33:06',1,NULL,'15634874658');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-28 17:09:04
