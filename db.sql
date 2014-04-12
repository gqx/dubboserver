DROP TABLE IF EXISTS `answer`;
CREATE TABLE `switch` (
  `sid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `switch_name` varchar(100) DEFAULT NULL COMMENT '开关名字',
  `switch_ip` int(16) DEFAULT NULL COMMENT '开关ip',
  `switch_mac` char(20) DEFAULT NULL COMMENT '开关mac',
  `switch_state` int DEFAULT 0 COMMENT '开关状态0关1开',
  `last_op_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '最后一次操作时间',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=382 DEFAULT CHARSET=utf8 COMMENT='开关表';