CREATE TABLE `t_stock_demo`(
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `product_no` varchar(64) NULL,
  `shop_code` varchar(255) NULL,
  `pre_stock` int(3) DEFAULT 0,
  `ava_stock` int(3) DEFAULT 0,
  `total_stock` int(3) DEFAULT 0 ,
  `update_time` timestamp(0) NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `t_order_demo`(
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) NULL,
  `shop_code` varchar(255) NULL,
  `product_no` varchar(64) DEFAULT 0,
  `product_num` int(3) DEFAULT 0 ,
  `status` int(3) NULL,
  `update_time` timestamp(0) NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `t_stock_change_log_demo`(
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `product_no` varchar(64) NULL,
  `shop_code` varchar(255) NULL,
	`order_no`  varchar(255) NULL,
	`opt_type` varchar(255) NULL,
  `pre_stock_after` int(3) DEFAULT 0,
  `ava_stock_after` int(3) DEFAULT 0,
  `total_stock_after` int(3) DEFAULT 0 ,
	`pre_stock_before` int(3) DEFAULT 0,
  `ava_stock_before` int(3) DEFAULT 0,
  `total_stock_before` int(3) DEFAULT 0 ,
  `update_time` timestamp(0) NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;
