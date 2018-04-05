CREATE TABLE `properties` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `key` varchar(255) DEFAULT NULL COMMENT '配置项KEY',
  `value` varchar(255) DEFAULT NULL COMMENT '配置项值',
  `application` varchar(255) DEFAULT NULL COMMENT '应用名称',
  `profile` varchar(255) DEFAULT NULL COMMENT '环境,如dev,test,prod',
  `label` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='配置中心表,配置文件按照{application}-{profile}.yml或者{application}-{profile}.properties格式命名';