DROP TABLE IF EXISTS `t_application`;
CREATE TABLE `t_application` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `application` varchar(45) NOT NULL COMMENT '应用',
  `application_name` varchar(45) DEFAULT NULL COMMENT '应用名称',
  `label` varchar(45) NOT NULL DEFAULT 'master',
  `profile` varchar(45) NOT NULL DEFAULT 'default' COMMENT '环境',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `region_id` bigint(16) NOT NULL COMMENT '所属区域',
  PRIMARY KEY (`id`),
  UNIQUE KEY `APPLICATION_UNIQUE_KEY` (`application`,`label`,`profile`),
  KEY `APPLICATION_INDEX` (`application`),
  KEY `LABEL_INDEX` (`label`),
  KEY `PROFILE_INDEX` (`profile`),
  KEY `REGOIN_ID_INDEX` (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COMMENT='应用表';

DROP TABLE IF EXISTS `t_application_config`;
CREATE TABLE `t_application_config` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(16) NOT NULL COMMENT '关联的应用ID',
  `item_key` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '配置项KEY',
  `item_value` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '配置项值',
  `item_desc` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='应用私有配置信息表';

DROP TABLE IF EXISTS `t_application_item_group_relation`;
CREATE TABLE `t_application_item_group_relation` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(16) NOT NULL COMMENT '应用ID',
  `item_group_id` bigint(16) NOT NULL COMMENT '配置组ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='应用和配置项组关系，应用多对多配置项组';

DROP TABLE IF EXISTS `t_client_application`;
CREATE TABLE `t_client_application` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `application` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '应用名称',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0在线，1离线(所有hostinfo离线的时候)',
  `profile` varchar(45) COLLATE utf8mb4_bin DEFAULT 'default' COMMENT '环境',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `APPLICATION_INDEX` (`application`),
  KEY `PROFILE_INDEX` (`profile`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='配置中心客户端应用';

DROP TABLE IF EXISTS `t_client_host_info`;
CREATE TABLE `t_client_host_info` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `client_application_id` bigint(16) NOT NULL COMMENT '所属应用ID',
  `host_ip` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '连接主机IP',
  `host_port` int(11) NOT NULL COMMENT '连接主机开放服务的端口',
  `netty_ip` VARCHAR(45) NULL COMMENT 'netty 连接的IP地址',
  `netty_port` int(11) NULL COMMENT 'netty 连接的PORT',
  `status` int(11) DEFAULT '0' COMMENT '状态，0在线，1离线',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `IP_INDEX` (`host_ip`),
  KEY `PORT_INDEX` (`host_port`),
  KEY `STATUS_INDEX` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='配置中心客户端连接信息';

DROP TABLE IF EXISTS `t_config_item`;
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
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COMMENT='具体配置项表';

DROP TABLE IF EXISTS `t_config_item_group`;
CREATE TABLE `t_config_item_group` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(45) NOT NULL COMMENT '组名称',
  `group_desc` varchar(45) DEFAULT NULL COMMENT '组描述',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COMMENT='配置项组';

DROP TABLE IF EXISTS `t_config_item_group_relation`;
CREATE TABLE `t_config_item_group_relation` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(16) NOT NULL COMMENT '配置项ID',
  `group_id` bigint(16) NOT NULL COMMENT '组ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COMMENT='配置组和配置项的关系表';

DROP TABLE IF EXISTS `t_region`;
CREATE TABLE `t_region` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `region_name` varchar(45) NOT NULL COMMENT '区域名称',
  `region_desc` varchar(45) DEFAULT NULL COMMENT '区域描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COMMENT='区域';

DROP TABLE IF EXISTS `t_server_host_config`;
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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='服务器配置';