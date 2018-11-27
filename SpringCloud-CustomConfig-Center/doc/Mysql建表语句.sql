-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 192.168.206.210    Database: config_center
-- ------------------------------------------------------
-- Server version	5.7.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_application_config`
--

DROP TABLE IF EXISTS `t_application_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_application_config` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `application` varchar(45) NOT NULL COMMENT '应用',
  `application_name` varchar(45) DEFAULT NULL COMMENT '应用名称',
  `label` varchar(45) NOT NULL DEFAULT 'master',
  `profile` varchar(45) NOT NULL DEFAULT 'default' COMMENT '环境',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `region_id` bigint(16) NOT NULL COMMENT '所属区域',
  PRIMARY KEY (`id`),
  KEY `APPLICATION_INDEX` (`application`),
  KEY `LABEL_INDEX` (`label`),
  KEY `PROFILE_INDEX` (`profile`),
  KEY `REGOIN_ID_INDEX` (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='应用配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_application_config`
--

LOCK TABLES `t_application_config` WRITE;
/*!40000 ALTER TABLE `t_application_config` DISABLE KEYS */;
INSERT INTO `t_application_config` VALUES (1,'winner-config-service','注册中心服务','master','dev','2018-11-23 14:29:47',NULL,1),(2,'config-center-service','注册中心服务','master','dev','2018-11-23 14:29:47',NULL,1);
/*!40000 ALTER TABLE `t_application_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_application_item_group_relation`
--

DROP TABLE IF EXISTS `t_application_item_group_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_application_item_group_relation` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(16) NOT NULL COMMENT '应用ID',
  `item_group_id` bigint(16) NOT NULL COMMENT '配置组ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `FK_APPLICATION_idx` (`application_id`),
  KEY `FK_ITEM_GROUP_idx` (`item_group_id`),
  CONSTRAINT `FK_APPLICATION` FOREIGN KEY (`application_id`) REFERENCES `t_application_config` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ITEM_GROUP` FOREIGN KEY (`item_group_id`) REFERENCES `t_config_item_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='应用和配置项组关系，应用多对多配置项组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_application_item_group_relation`
--

LOCK TABLES `t_application_item_group_relation` WRITE;
/*!40000 ALTER TABLE `t_application_item_group_relation` DISABLE KEYS */;
INSERT INTO `t_application_item_group_relation` VALUES (1,1,1),(2,1,2),(3,2,4),(4,2,5),(5,2,6),(6,2,7);
/*!40000 ALTER TABLE `t_application_item_group_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_config_item`
--

DROP TABLE IF EXISTS `t_config_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_config_item` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `item_key` varchar(256) NOT NULL COMMENT '配置项KEY',
  `item_value` varchar(128) NOT NULL COMMENT '配置项值',
  `item_desc` varchar(128) DEFAULT NULL COMMENT '配置项描述',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0可用,1不可用',
  `item_type` int(1) DEFAULT '0' COMMENT '应用类型，0通用，1开发环境，2测试环境，3生产环境，4其他',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COMMENT='具体配置项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_config_item`
--

LOCK TABLES `t_config_item` WRITE;
/*!40000 ALTER TABLE `t_config_item` DISABLE KEYS */;
INSERT INTO `t_config_item` VALUES (1,'eureka.client.serviceUrl.defaultZone','http://localhost:8888/eureka/','配置中心url','2018-11-23 14:29:47',NULL,0,0),(2,'eureka.instance.hostname','localhost','配置中心hostName','2018-11-23 14:29:47',NULL,0,0),(3,'spring.datasource.url','jdbc:mysql://rm-wz9236i8x4rr80nl3go.mysql.rds.aliyuncs.com:3306/basisdb?useSSL=false','数据库url','2018-11-23 14:29:47',NULL,0,0),(4,'spring.datasource.username','basisuser','数据库登录账号','2018-11-23 14:29:47',NULL,0,0),(5,'spring.datasource.password','Basisuser123','数据库登录密码','2018-11-23 14:29:47',NULL,0,0),(6,'spring.datasource.url','jdbc:mysql://192.168.206.210:3306/config_center?useSSL=false','配置中心服务数据库url','2018-11-23 14:29:47',NULL,0,1),(7,'spring.datasource.username','admin','配置中心服务数据库账号','2018-11-23 14:29:47',NULL,0,1),(8,'spring.datasource.password','Admin@123','配置中心服务数据库密码','2018-11-23 14:29:47',NULL,0,1),(9,'spring.datasource.driver-class-name','com.mysql.jdbc.Driver','数据库驱动','2018-11-23 14:29:47',NULL,0,0),(10,'spring.datasource.type','com.alibaba.druid.pool.DruidDataSource','Springcloud数据类型','2018-11-23 14:29:47',NULL,0,0),(11,'eureka.instance.prefer-ip-address','true',NULL,'2018-11-23 14:29:47',NULL,0,0),(12,'eureka.instance.instance-id','${spring.cloud.client.ipAddress}:${server.port}',NULL,'2018-11-23 14:29:47',NULL,0,0),(13,'eureka.client.register-with-eureka','true',NULL,'2018-11-23 14:29:47',NULL,0,0),(14,'eureka.client.fetch-registry','false',NULL,'2018-11-23 14:29:47',NULL,0,0),(15,'eureka.client.service-url.defaultZone','http://localhost:9000/eureka/','配置中心serviceUrl','2018-11-23 14:29:47',NULL,0,1),(16,'eureka.server.enable-self-preservation','false','注册中心关闭保护机制','2018-11-23 14:29:47',NULL,0,0),(17,'eureka.server.eviction-interval-timer-in-ms','2000','注册中心剔除失效服务间隔','2018-11-23 14:29:47',NULL,0,0);
/*!40000 ALTER TABLE `t_config_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_config_item_group`
--

DROP TABLE IF EXISTS `t_config_item_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_config_item_group` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(45) NOT NULL COMMENT '组名称',
  `group_desc` varchar(45) DEFAULT NULL COMMENT '组描述',
  `creat_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='配置项组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_config_item_group`
--

LOCK TABLES `t_config_item_group` WRITE;
/*!40000 ALTER TABLE `t_config_item_group` DISABLE KEYS */;
INSERT INTO `t_config_item_group` VALUES (1,'eureka','配置中心','2018-11-23 14:29:47',NULL),(2,'databases','数据库配置','2018-11-23 14:29:47',NULL),(4,'eureka_common','配置中心公共配置','2018-11-23 14:29:47',NULL),(5,'databases_common','数据库公共配置','2018-11-23 14:29:47',NULL),(6,'eureka_config_center','配置中心服务注册eureka配置组','2018-11-23 14:29:47',NULL),(7,'databases_config_center','配置中心服务注册数据库配置组','2018-11-23 14:29:47',NULL);
/*!40000 ALTER TABLE `t_config_item_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_config_item_group_relation`
--

DROP TABLE IF EXISTS `t_config_item_group_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_config_item_group_relation` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(16) NOT NULL COMMENT '配置项ID',
  `group_id` bigint(16) NOT NULL COMMENT '组ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `FK_ITEM_ID_idx` (`item_id`),
  KEY `FK_ITEM_GROUP_ID_idx` (`group_id`),
  CONSTRAINT `FK_ITEM_GROUP_ID` FOREIGN KEY (`group_id`) REFERENCES `t_config_item_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ITEM_ID` FOREIGN KEY (`item_id`) REFERENCES `t_config_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COMMENT='配置组和配置项的关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_config_item_group_relation`
--

LOCK TABLES `t_config_item_group_relation` WRITE;
/*!40000 ALTER TABLE `t_config_item_group_relation` DISABLE KEYS */;
INSERT INTO `t_config_item_group_relation` VALUES (1,1,1),(2,2,1),(3,3,2),(4,4,2),(5,5,2),(6,11,4),(7,12,4),(8,13,4),(9,14,4),(10,16,4),(11,17,4),(12,9,5),(13,10,5),(14,15,6),(15,6,7),(16,7,7),(17,8,7);
/*!40000 ALTER TABLE `t_config_item_group_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_region`
--

DROP TABLE IF EXISTS `t_region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_region` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `region_name` varchar(45) NOT NULL COMMENT '区域名称',
  `region_desc` varchar(45) DEFAULT NULL COMMENT '区域描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='区域';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_region`
--

LOCK TABLES `t_region` WRITE;
/*!40000 ALTER TABLE `t_region` DISABLE KEYS */;
INSERT INTO `t_region` VALUES (1,'华南区','华南区域','2018-11-23 14:29:46',NULL);
/*!40000 ALTER TABLE `t_region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_server_host_config`
--

DROP TABLE IF EXISTS `t_server_host_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_server_host_config` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `server_host` varchar(45) NOT NULL COMMENT 'IP地址',
  `server_desc` varchar(45) DEFAULT NULL COMMENT '服务描述',
  `region_id` bigint(16) NOT NULL COMMENT '关联区域',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `host_UNIQUE` (`server_host`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='服务器配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_server_host_config`
--

LOCK TABLES `t_server_host_config` WRITE;
/*!40000 ALTER TABLE `t_server_host_config` DISABLE KEYS */;
INSERT INTO `t_server_host_config` VALUES (1,'172.16.80.194','本地host',1,'2018-11-23 14:29:47',NULL),(2,'127.0.0.1','本地host',1,'2018-11-23 14:29:47',NULL);
/*!40000 ALTER TABLE `t_server_host_config` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-27 14:29:13
