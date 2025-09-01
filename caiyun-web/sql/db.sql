
--
-- Table structure for table `payment_item`
--

DROP TABLE IF EXISTS `payment_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_item` (
  `id` varchar(32) NOT NULL COMMENT '项目ID',
  `name` varchar(100) NOT NULL COMMENT '项目名称',
  `category` varchar(50) NOT NULL COMMENT '项目类别：学费、住宿费、书本费、其他费用',
  `amount` decimal(10,2) NOT NULL COMMENT '缴费金额',
  `exempt_amount` decimal(10,2) DEFAULT '0.00' COMMENT '减免金额',
  `actual_amount` decimal(10,2) NOT NULL COMMENT '实际应缴金额',
  `status` varchar(20) NOT NULL COMMENT '缴费状态：DRAFT-草稿，PUBLISHED-已发布，CANCELLED-已取消，EXPIRED-已过期',
  `start_time` datetime NOT NULL COMMENT '缴费开始时间',
  `end_time` datetime NOT NULL COMMENT '缴费截止时间',
  `description` text COMMENT '缴费说明',
  `document_url` varchar(255) DEFAULT NULL COMMENT '缴费说明文档URL',
  `notices` json DEFAULT NULL COMMENT '缴费须知，JSON数组格式',
  `processes` json DEFAULT NULL COMMENT '缴费流程，JSON数组格式',
  `faqs` json DEFAULT NULL COMMENT '常见问题，JSON数组格式',
  `contacts` json DEFAULT NULL COMMENT '联系人信息，JSON数组格式',
  `creator` varchar(32) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(32) DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='缴费项目表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_item`
--

LOCK TABLES `payment_item` WRITE;
/*!40000 ALTER TABLE `payment_item` DISABLE KEYS */;
INSERT INTO `payment_item` VALUES ('1','书本费','书本费',500.00,0.00,500.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学期书本费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0),('10','实验室维护费','其他费用',120.00,0.00,120.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学期实验室维护费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0),('11','学费','学费',5000.00,0.00,5000.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学年学费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0),('2','住宿费','住宿费',1000.00,0.00,1000.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学期住宿费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0),('3','实验材料费','其他费用',200.00,0.00,200.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学期实验材料费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0),('4','保险费','其他费用',100.00,0.00,100.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学期保险费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0),('5','活动费','其他费用',150.00,0.00,150.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学期活动费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0),('6','网络使用费','其他费用',50.00,0.00,50.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学期网络使用费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0),('7','图书馆借阅费','其他费用',30.00,0.00,30.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学期图书馆借阅费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0),('8','体育设施使用费','其他费用',70.00,0.00,70.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学期体育设施使用费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0),('9','校车服务费','其他费用',80.00,0.00,80.00,'IN_PROGRESS','2023-09-01 00:00:00','2023-09-30 23:59:59','本学期校车服务费',NULL,'[\"请按时缴费\"]','[\"在线支付\", \"银行转账\"]','[\"常见问题1\", \"常见问题2\"]','[\"联系人1\", \"联系人2\"]','admin','2023-08-01 00:00:00','admin','2025-08-30 07:26:26',0);
/*!40000 ALTER TABLE `payment_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_order`
--

DROP TABLE IF EXISTS `payment_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_order` (
  `id` varchar(32) NOT NULL COMMENT '订单ID',
  `item_id` varchar(32) NOT NULL COMMENT '缴费项目ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `payment_method` varchar(20) NOT NULL COMMENT '支付方式：ALIPAY-支付宝，WECHAT-微信支付',
  `payment_scene` varchar(20) NOT NULL COMMENT '支付场景：APP-APP支付，H5-H5支付，JSAPI-公众号支付，NATIVE-扫码支付',
  `payment_terminal` varchar(20) NOT NULL COMMENT '支付终端：IOS-苹果，ANDROID-安卓，WEB-网页，WECHAT-微信',
  `status` varchar(20) NOT NULL COMMENT '订单状态：PENDING-待支付，PAID-已支付，CANCELLED-已取消，EXPIRED-已过期，REFUNDING-退款中，REFUNDED-已退款',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '支付流水号',
  `pay_params` json DEFAULT NULL COMMENT '支付参数，JSON格式',
  `expire_time` datetime NOT NULL COMMENT '订单过期时间',
  `refund_status` varchar(20) DEFAULT NULL COMMENT '退款状态：null-未退款，PROCESSING-退款处理中，SUCCESS-退款成功，FAILED-退款失败',
  `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_no` varchar(64) DEFAULT NULL COMMENT '退款流水号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_trade_no` (`trade_no`),
  UNIQUE KEY `uk_refund_no` (`refund_no`),
  KEY `idx_item_id` (`item_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_refund_status` (`refund_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='支付订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_order`
--

LOCK TABLES `payment_order` WRITE;
/*!40000 ALTER TABLE `payment_order` DISABLE KEYS */;
INSERT INTO `payment_order` VALUES ('1','10','10',120.00,'ALIPAY','APP','IOS_APP','PAID','2025-08-30 17:01:24','2025083017012386525028',NULL,'2025-08-30 17:09:25',NULL,NULL,NULL,NULL,'校园卡充值','2025-08-30 07:31:43','2025-08-30 08:08:06',0),('10','1','10',800.00,'WECHAT','JSAPI','WECHAT','PAID','2025-09-02 14:15:00','TRADE202509020002','{\"body\": \"2025学年住宿费\", \"subject\": \"住宿费缴纳\"}','2025-09-09 14:15:00','SUCCESS',121.00,'2025-08-30 17:43:26','R2025083017432664077685','2025学年住宿费缴纳','2025-09-02 13:45:00','2025-09-01 03:48:58',0),('2','2','10',50.00,'ALIPAY','APP','IOS','PAID','2025-09-05 11:10:00','TRADE202509050005','{\"body\": \"校园卡充值50元\", \"subject\": \"校园卡充值\"}','2025-09-12 11:10:00','IN_PROGRESS',0.00,'2025-09-05 00:00:00','REFUND202509050000','校园卡充值','2025-09-05 10:40:00','2025-09-01 03:47:31',0),('3','3','10',300.00,'ALIPAY','NATIVE','WEB','PAID','2025-09-03 09:45:00','TRADE202509030003','{\"body\": \"2025学年教材费\", \"subject\": \"教材费缴纳\"}','2025-09-10 09:45:00','IN_PROGRESS',0.00,'2025-09-03 00:00:00','REFUND202509030000','2025学年教材费缴纳','2025-09-03 09:15:00','2025-09-01 03:48:58',0),('6','11','10',2500.00,'ALIPAY','APP','IOS','PENDING','2025-09-01 10:30:00','TRADE202509010001','{\"body\": \"2025学年学费\", \"subject\": \"学费缴纳\"}','2025-09-08 10:30:00','NOT_STARTED',0.00,'2025-09-01 00:00:00','REFUND202509010000','2025学年学费缴纳','2025-09-01 10:00:00','2025-09-01 03:48:58',0),('7','2','10',120.00,'WECHAT','H5','ANDROID','PAID','2025-09-04 16:20:00','TRADE202509040004','{\"body\": \"2025学年体检费\", \"subject\": \"体检费缴纳\"}','2025-09-11 16:20:00','IN_PROGRESS',0.00,'2025-09-04 00:00:00','REFUND202509040000','2025学年体检费缴纳','2025-09-04 16:00:00','2025-09-01 03:47:31',0);
/*!40000 ALTER TABLE `payment_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refund_record`
--

DROP TABLE IF EXISTS `refund_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refund_record` (
  `id` varchar(32) NOT NULL COMMENT '退款ID',
  `order_id` varchar(32) NOT NULL COMMENT '订单ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '退款金额',
  `status` varchar(20) NOT NULL COMMENT '退款状态：PROCESSING-退款处理中，SUCCESS-退款成功，FAILED-退款失败',
  `reason` varchar(255) NOT NULL COMMENT '退款原因',
  `description` text COMMENT '退款说明',
  `refund_no` varchar(64) NOT NULL COMMENT '退款流水号',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `fail_reason` varchar(255) DEFAULT NULL COMMENT '失败原因',
  `expected_arrival_time` datetime DEFAULT NULL COMMENT '预计退款到账时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refund_no` (`refund_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='退款记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refund_record`
--

LOCK TABLES `refund_record` WRITE;
/*!40000 ALTER TABLE `refund_record` DISABLE KEYS */;
INSERT INTO `refund_record` VALUES ('R2025083017432695199466','33','10',1.00,'SUCCESS','不想要','','R2025083017432664077685','2025-08-30 17:43:26',NULL,NULL,'2025-08-30 08:19:24','2025-09-01 03:43:52',0),('REFID20250901001','1','10',2500.00,'SUCCESS','学生退学申请','因学生办理退学手续，申请退还2025学年学费','REFUNDREC20250901001','2025-09-02 15:30:00','','2025-09-05 18:00:00','2025-09-02 14:00:00','2025-08-30 08:09:33',0),('REFID20250902002','32','10',800.00,'PROCESSING','住宿调整（换宿舍，费用多缴）','因学生调整宿舍，原住宿费多缴，申请退款','REFUNDREC20250902002','2025-09-03 00:00:00','退款处理中，暂未失败','2025-09-06 12:00:00','2025-09-03 10:00:00','2025-08-30 08:09:33',0),('REFID20250903003','95','10',300.00,'FAILED','教材版本错误，申请退款','学生反馈教材版本与教学要求不符，申请退款','REFUNDREC20250903003','2025-09-04 00:00:00','财务系统接口异常，暂无法完成退款','2025-09-07 10:00:00','2025-09-04 09:00:00','2025-08-30 08:09:33',0),('REFID20250904004','33','10',120.00,'SUCCESS','系统重复扣费（体检费）','学生已完成体检，系统误操作重复扣费，申请退款','REFUNDREC20250904004','2025-09-05 16:45:00','','2025-09-08 17:00:00','2025-09-05 15:30:00','2025-08-30 08:09:33',0);
/*!40000 ALTER TABLE `refund_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `parent_id` bigint DEFAULT NULL COMMENT '父权限ID',
  `name` varchar(50) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(50) NOT NULL COMMENT '权限编码',
  `permission_type` tinyint(1) NOT NULL COMMENT '权限类型：1-菜单，2-按钮',
  `path` varchar(255) DEFAULT NULL COMMENT '路由路径',
  `component` varchar(255) DEFAULT NULL COMMENT '前端组件',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` VALUES (1,NULL,'所有权限','all:privilege',1,'/all-privilege','all/PrivilegeView','star',0,1,'2025-08-29 03:38:30','2025-08-29 03:38:30');
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (2,'系统管理员','ADMIN','拥有系统所有权限的超级管理员',1,'2025-08-29 03:38:30','2025-08-29 03:38:30'),(3,'普通用户','USER','默认的普通用户角色，无特殊权限',1,'2025-08-29 03:42:36','2025-08-29 03:42:36');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission` VALUES (1,2,1,'2025-08-29 03:38:30');
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `account` varchar(50) DEFAULT NULL COMMENT '账号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (10,'admin','25263303123','123456','超级管理员','13800138000','admin@yourcompany.com','/avatar/admin.png',1,NULL,'2025-08-29 03:38:30','2025-08-30 06:28:33'),(11,'JAVA',NULL,'user1','测试用户1','13800138001','user1@demo.com','/avatar/user.png',1,NULL,'2025-08-29 03:42:36','2025-08-29 03:44:41'),(12,'RUBY',NULL,'user2','测试用户2','13800138002','user2@demo.com','/avatar/user.png',1,NULL,'2025-08-29 03:42:36','2025-08-29 03:44:41'),(13,'CCC',NULL,'user3','测试用户3','13800138003','user3@demo.com','/avatar/user.png',1,NULL,'2025-08-29 03:42:36','2025-08-29 03:44:41'),(14,'阿联都',NULL,'user4','测试用户4','13800138004','user4@demo.com','/avatar/user.png',1,NULL,'2025-08-29 03:42:36','2025-08-29 03:44:41'),(15,'破搜东',NULL,'user5','测试用户5','13800138005','user5@demo.com','/avatar/user.png',1,NULL,'2025-08-29 03:42:36','2025-08-29 03:44:41'),(16,'戴坤',NULL,'user6','测试用户6','13800138006','user6@demo.com','/avatar/user.png',1,NULL,'2025-08-29 03:42:36','2025-08-29 03:44:41'),(17,'阿虎',NULL,'user7','测试用户7','13800138007','user7@demo.com','/avatar/user.png',1,NULL,'2025-08-29 03:42:36','2025-08-29 03:44:41'),(18,'阿狸',NULL,'user8','测试用户8','13800138008','user8@demo.com','/avatar/user.png',1,NULL,'2025-08-29 03:42:36','2025-08-29 03:44:41'),(19,'小美',NULL,'user9','测试用户9','13800138009','user9@demo.com','/avatar/user.png',1,NULL,'2025-08-29 03:42:36','2025-08-29 03:44:41'),(20,'小帅',NULL,'user10','测试用户10','13800138010','user10@demo.com','/avatar/user.png',1,NULL,'2025-08-29 03:42:36','2025-08-29 03:44:41');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (2,10,2,'2025-08-29 03:38:30'),(3,11,3,'2025-08-29 03:42:36'),(4,20,3,'2025-08-29 03:42:36'),(5,12,3,'2025-08-29 03:42:36'),(6,13,3,'2025-08-29 03:42:36'),(7,14,3,'2025-08-29 03:42:36'),(8,15,3,'2025-08-29 03:42:36'),(9,16,3,'2025-08-29 03:42:36'),(10,17,3,'2025-08-29 03:42:36'),(11,18,3,'2025-08-29 03:42:36'),(12,19,3,'2025-08-29 03:42:36');
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
