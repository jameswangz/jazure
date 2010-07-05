/*
Navicat MySQL Data Transfer
Source Host     : 10.200.107.19:3306
Source Database : jazure
Target Host     : 10.200.107.19:3306
Target Database : jazure
Date: 2009-7-22 14:48:44
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for jazure_task
-- ----------------------------
DROP TABLE IF EXISTS `jazure_task`;
CREATE TABLE `jazure_task` (
  `id` varchar(50) NOT NULL default '',
  `correlation_id` varchar(50),
  `sequence_size` int default 0,
  `sequence_number` int default 0,
  `project_configuration` longblob,
  `job_config` longblob,
  `job_id` varchar(255) default NULL,
  `task` longblob,
  `type` varchar(255) default NULL,
  `status` varchar(20) default NULL,
  `grid_worker` varchar(100) default NULL,
  `result` longblob,
  `result_queue` varchar(255) default NULL,
  `task_queue` varchar(255) default NULL,
  `created_time` datetime default NULL,
  `last_modified_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jazure_task_param
-- ----------------------------
DROP TABLE IF EXISTS `jazure_task_param`;
CREATE TABLE `jazure_task_param` (
  `task_id` varchar(50) NOT NULL,
  `param_key` varchar(100) NOT NULL,
  `param_value` text,
  PRIMARY KEY  (`task_id`,`param_key`),
  CONSTRAINT `fk_param_task` FOREIGN KEY (`task_id`) REFERENCES `jazure_task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jazure_task_param
-- ----------------------------

DROP TABLE IF EXISTS `jazure_task_result`;
CREATE TABLE `jazure_task_result` (
  `task_id` varchar(50) NOT NULL,
  `result_key` varchar(100) NOT NULL,
  `result_value` text,
  PRIMARY KEY  (`task_id`,`result_key`),
  CONSTRAINT `fk_result_task` FOREIGN KEY (`task_id`) REFERENCES `jazure_task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jazure_task_result
-- ----------------------------
