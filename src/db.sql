DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
    `id`    		int   			AUTO_INCREMENT	COMMENT 'id',
    `password`		VARCHAR(255)		NOT NULL		COMMENT '密码',
    `name`	varchar(20)   	NOT NULL    	COMMENT '名称',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT '用户表';



DROP TABLE IF EXISTS `Gprs`;
CREATE TABLE `Gprs` (
    `id`			int   			AUTO_INCREMENT	COMMENT 'id',
    `name`			varchar(255)		DEFAULT NULL	COMMENT 'name',
    `mac` 			varchar(255) 		DEFAULT NULL 	COMMENT 'mac',
    `ip` 			varchar(255) 		DEFAULT NULL 	COMMENT 'ip',
    `voltage`		int   	DEFAULT '0' 	COMMENT '电压',
	`temperature` 	int		DEFAULT '0' 	COMMENT '温度',
	`humidity` 		int		DEFAULT '0' 	COMMENT '湿度',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '最后一次操作时间',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'gprs表';

DROP TABLE IF EXISTS `Zigbee`;
CREATE TABLE `Zigbee` (
    `id`		int   		AUTO_INCREMENT	COMMENT 'id',
    `name`			varchar(255)		DEFAULT NULL	COMMENT 'name',
    `gid`		int 		COMMENT '对应gprs id',
    `mac` 		varchar(255) 	DEFAULT NULL 	COMMENT 'mac',	
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '最后一次操作时间',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'zigbee表';


DROP TABLE IF EXISTS `Switch`;
CREATE TABLE `Switch` (
    `id`		int   	AUTO_INCREMENT	COMMENT 'id',
    `zid`		int		COMMENT 'zigbee id',
    `name`		VARCHAR(255)		COMMENT '开关i名称',
    `state`		int   	NOT NULL   DEFAULT '0' 	COMMENT '开关状态',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '最后一次操作时间',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT '开关表';