DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
    `id`    		int   			AUTO_INCREMENT	COMMENT 'id',
    `password`		VARCHAR(255)		NOT NULL		COMMENT '����',
    `name`	varchar(20)   	NOT NULL    	COMMENT '����',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT '�û���';



DROP TABLE IF EXISTS `Gprs`;
CREATE TABLE `Gprs` (
    `id`			int   			AUTO_INCREMENT	COMMENT 'id',
    `name`			varchar(255)		DEFAULT NULL	COMMENT 'name',
    `mac` 			varchar(255) 		DEFAULT NULL 	COMMENT 'mac',
    `ip` 			varchar(255) 		DEFAULT NULL 	COMMENT 'ip',
    `voltage`		int   	DEFAULT '0' 	COMMENT '��ѹ',
	`temperature` 	int		DEFAULT '0' 	COMMENT '�¶�',
	`humidity` 		int		DEFAULT '0' 	COMMENT 'ʪ��',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '���һ�β���ʱ��',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'gprs��';

DROP TABLE IF EXISTS `Zigbee`;
CREATE TABLE `Zigbee` (
    `id`		int   		AUTO_INCREMENT	COMMENT 'id',
    `name`			varchar(255)		DEFAULT NULL	COMMENT 'name',
    `gid`		int 		COMMENT '��Ӧgprs id',
    `mac` 		varchar(255) 	DEFAULT NULL 	COMMENT 'mac',
    `ztype`		int		DEFAULT '0'	COMMENT 'zigbee type: 0 swith, 1 water pump, 2 pressure senseor',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '���һ�β���ʱ��',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'zigbee��';


DROP TABLE IF EXISTS `Switch`;
CREATE TABLE `Switch` (
    `id`		int   	AUTO_INCREMENT	COMMENT 'id',
    `zid`		int		COMMENT 'zigbee id',
    `name`		VARCHAR(255)		COMMENT '����i����',
    `state`		int   	NOT NULL   DEFAULT '0' 	COMMENT '����״̬',
    `tid`		int		DEFAULT '0' COMMENT 'task id',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '���һ�β���ʱ��',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT '���ر�';

DROP TABLE IF EXISTS `Task`;
CREATE TABLE `Task` (
    `id`		int   	AUTO_INCREMENT	COMMENT 'id',
    `tname`		varchar(100)	DEFAULT '' COMMENT '��������',
    `start_time`	VARCHAR(100)		COMMENT '��ʼʱ��',
    `stop_time`		VARCHAR(100)		COMMENT '����ʱ��',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '���һ�β���ʱ��',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT '���ر�';


DROP TABLE IF EXISTS `Historydata`;
CREATE TABLE `Historydata` (
    `id`			int   			AUTO_INCREMENT	COMMENT 'id',
    `gid`			int 		COMMENT '��Ӧgprs id',
    `voltage`		int   	DEFAULT '0' 	COMMENT '��ѹ',
	`temperature` 	int		DEFAULT '0' 	COMMENT '�¶�',
	`humidity` 		int		DEFAULT '0' 	COMMENT 'ʪ��',
	`update_time` varchar(30)  COMMENT '���һ�β���ʱ��',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'historydata';

DROP TABLE IF EXISTS `Turngroup`;
CREATE TABLE `Turngroup` (
    `id`			int   		AUTO_INCREMENT	COMMENT 'id',
    `grpid`			int 		COMMENT 'group id',
    `sname`			VARCHAR(255)   		COMMENT 'switch name',
	`update_time`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '���һ�β���ʱ��',
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
    `update_time`	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '����ʱ��',
     PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'Pressure';