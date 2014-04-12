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
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '���һ�β���ʱ��',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT 'zigbee��';


DROP TABLE IF EXISTS `Switch`;
CREATE TABLE `Switch` (
    `id`		int   	AUTO_INCREMENT	COMMENT 'id',
    `zid`		int		COMMENT 'zigbee id',
    `name`		VARCHAR(255)		COMMENT '����i����',
    `state`		int   	NOT NULL   DEFAULT '0' 	COMMENT '����״̬',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '���һ�β���ʱ��',
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT '���ر�';