DROP TABLE IF EXISTS `answer`;
CREATE TABLE `switch` (
  `sid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `switch_name` varchar(100) DEFAULT NULL COMMENT '��������',
  `switch_ip` int(16) DEFAULT NULL COMMENT '����ip',
  `switch_mac` char(20) DEFAULT NULL COMMENT '����mac',
  `switch_state` int DEFAULT 0 COMMENT '����״̬0��1��',
  `last_op_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '���һ�β���ʱ��',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=382 DEFAULT CHARSET=utf8 COMMENT='���ر�';