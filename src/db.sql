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
    `ztype`		int		DEFAULT '0'	COMMENT 'zigbee type: 0 swith, 1 water pump, 2 pressure senseor',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '最后一次操作时间',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'zigbee表';


DROP TABLE IF EXISTS `Switch`;
CREATE TABLE `Switch` (
    `id`		int   	AUTO_INCREMENT	COMMENT 'id',
    `zid`		int		COMMENT 'zigbee id',
    `name`		VARCHAR(255)		COMMENT '开关i名称',
    `state`		int   	NOT NULL   DEFAULT '0' 	COMMENT '开关状态',
    `tid`		int		DEFAULT '0' COMMENT 'task id',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '最后一次操作时间',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT '开关表';

DROP TABLE IF EXISTS `Task`;
CREATE TABLE `Task` (
    `id`		int   	AUTO_INCREMENT	COMMENT 'id',
    `tname`		varchar(100)	DEFAULT '' COMMENT '任务名称',
    `start_time`	VARCHAR(100)		COMMENT '开始时间',
    `stop_time`		VARCHAR(100)		COMMENT '结束时间',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '最后一次操作时间',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT '开关表';


DROP TABLE IF EXISTS `Historydata`;
CREATE TABLE `Historydata` (
    `id`			int   			AUTO_INCREMENT	COMMENT 'id',
    `gid`			int 		COMMENT '对应gprs id',
    `voltage`		int   	DEFAULT '0' 	COMMENT '电压',
	`temperature` 	int		DEFAULT '0' 	COMMENT '温度',
	`humidity` 		int		DEFAULT '0' 	COMMENT '湿度',
	`update_time` varchar(30)  COMMENT '最后一次操作时间',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'historydata';

DROP TABLE IF EXISTS `Turngroup`;
CREATE TABLE `Turngroup` (
    `id`			int   		AUTO_INCREMENT	COMMENT 'id',
    `grpid`			int 		COMMENT 'group id',
    `sname`			VARCHAR(255)   		COMMENT 'switch name',
	`update_time`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '最后一次操作时间',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'task group';

DROP TABLE IF EXISTS `Turntask`;
CREATE TABLE `Turntask` (
    `id`			int   		AUTO_INCREMENT	COMMENT 'id',
    `ttid`			int 		COMMENT 'turntask id',
    `ttname`		varchar(50) COMMENT 'turn task name',
    `sysname`		varchar(50) COMMENT 'system name',
    `grpid`			int   		COMMENT 'group id',
    `start_time`	varchar(30) 	COMMENT 'hh:mm',
    `end_time`		varchar(30) 	COMMENT 'hh:mm',
    `token`			int		DEFAULT '0' COMMENT '1 executable 0  unexecutable' ,
    `state`			int 	DEFAULT '0' COMMENT '1 execute 0 not execute',
    `duration`		varchar(50)		COMMENT 'length of time',
	`execute_date`   varchar(30) COMMENT 'yyyy-mm-dd',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'turn task';

DROP TABLE IF EXISTS `Pool`;
CREATE TABLE `Pool` (
    `id`		int   		AUTO_INCREMENT	COMMENT 'id',
    `name`		varchar(50)		COMMENT 'pool name',
    `state`		int 	DEFAULT '0'	COMMENT '1 open 0 closed',
     PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'Pool';

DROP TABLE IF EXISTS `Pressure`;
CREATE TABLE `Pressure` (
    `id`		int   	AUTO_INCREMENT	COMMENT 'id',
    `sid`		int		COMMENT 'switch id',
    `pvalue`	int		COMMENT 'pressure value',
    `update_time`	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '更新时间',
     PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'Pressure';