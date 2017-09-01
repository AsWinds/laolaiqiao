/*
Navicat MySQL Data Transfer

Source Server         : 121.40.187.122-laolaiqiao_app_dev
Source Server Version : 50624
Source Host           : 121.40.187.122:3306
Source Database       : laolaiqiao_app_dev

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-03-29 10:49:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `contact` varchar(255) NOT NULL,
  `detail` text NOT NULL,
  `end_date` datetime NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `start_date` datetime NOT NULL,
  `img_url` varchar(255) NOT NULL,
  `publisher_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_art_team
-- ----------------------------
DROP TABLE IF EXISTS `t_art_team`;
CREATE TABLE `t_art_team` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `last_update_time` datetime NOT NULL COMMENT '更新时间',
  `version` int(11) NOT NULL COMMENT '更新次数',
  `detail` varchar(255) DEFAULT NULL COMMENT '团队介绍',
  `leader_id` bigint(20) DEFAULT NULL COMMENT '团队的团长ID',
  `location` varchar(255) DEFAULT NULL COMMENT '团队所属区域',
  `name` varchar(255) NOT NULL COMMENT '团队名称',
  `fans_amount` int(11) NOT NULL DEFAULT '0' COMMENT '粉丝数量、关注量',
  `like_amount` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数量',
  `img_url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_art_team_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='团队';

-- ----------------------------
-- Table structure for t_fans
-- ----------------------------
DROP TABLE IF EXISTS `t_fans`;
CREATE TABLE `t_fans` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `category` int(1) NOT NULL DEFAULT '0' COMMENT '类别\n0：个人；1：团队',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID。类别是团队时，为空',
  `team_id` bigint(20) DEFAULT NULL COMMENT '团队。类别是个人时，为空',
  `fans_id` bigint(20) NOT NULL COMMENT '个人或者团队的粉丝',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='团队或者个人的粉丝（关注）表';

-- ----------------------------
-- Table structure for t_like_star
-- ----------------------------
DROP TABLE IF EXISTS `t_like_star`;
CREATE TABLE `t_like_star` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `last_update_time` datetime NOT NULL COMMENT '更新时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '更新次数',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `type` int(1) NOT NULL COMMENT '0：点赞，1：收藏',
  `video_id` bigint(20) NOT NULL COMMENT '视频ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid_vid_type` (`user_id`,`type`,`video_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户对视频的点赞、收藏';

-- ----------------------------
-- Table structure for t_rember_me_token
-- ----------------------------
DROP TABLE IF EXISTS `t_rember_me_token`;
CREATE TABLE `t_rember_me_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `last_update_time` datetime NOT NULL COMMENT '更新时间',
  `version` int(11) NOT NULL COMMENT '版本',
  `expire_at` datetime NOT NULL COMMENT '过期时间',
  `token` varchar(255) NOT NULL COMMENT 'Token',
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='登录Token表';

-- ----------------------------
-- Table structure for t_sms_code
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_code`;
CREATE TABLE `t_sms_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `last_update_time` datetime NOT NULL COMMENT '更新时间',
  `version` int(11) NOT NULL COMMENT '更新次数',
  `expire_date` datetime NOT NULL COMMENT '过期时间',
  `phone` varchar(255) NOT NULL COMMENT '手机号码',
  `sms_code` varchar(255) NOT NULL COMMENT '短信验证码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='短信表';

-- ----------------------------
-- Table structure for t_sys_img
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_img`;
CREATE TABLE `t_sys_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `bucket` varchar(255) NOT NULL,
  `ext` varchar(255) NOT NULL,
  `stored_key` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_img_stored_key` (`stored_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_uploaded_video
-- ----------------------------
DROP TABLE IF EXISTS `t_uploaded_video`;
CREATE TABLE `t_uploaded_video` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `last_update_time` datetime NOT NULL COMMENT '更新时间',
  `version` int(11) NOT NULL COMMENT '更新次数',
  `category` int(11) NOT NULL COMMENT '类别：0个人；1：团队',
  `name` varchar(255) NOT NULL COMMENT '视频名称',
  `upload_user_id` bigint(20) NOT NULL COMMENT '上传者ID',
  `url` varchar(255) NOT NULL COMMENT '视频在云服务器上的URL地址',
  `owner_team_id` bigint(20) DEFAULT NULL COMMENT '视频上传者ID',
  `like_amount` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `visit_amount` int(11) NOT NULL DEFAULT '0' COMMENT '视频浏览量',
  `thumbnail_url` varchar(255) NOT NULL COMMENT '视频缩略图',
  `uvt_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uvt_id` (`uvt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户上传的视频';

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `last_update_time` datetime NOT NULL COMMENT '更新时间',
  `version` int(11) NOT NULL COMMENT '更新次数',
  `address` varchar(255) DEFAULT NULL COMMENT '用户地址',
  `name` varchar(255) NOT NULL COMMENT '用户名',
  `phone` varchar(255) NOT NULL COMMENT '手机号码',
  `role` int(11) NOT NULL COMMENT '角色(后台)',
  `team_id` bigint(20) DEFAULT NULL COMMENT '用户所属的团队ID',
  `user_image` varchar(255) NOT NULL COMMENT '用户头像',
  `is_disabled` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否禁用。0：正常；1：禁用',
  `is_leader` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否团长',
  `fans_amount` int(11) NOT NULL DEFAULT '0' COMMENT '粉丝数量',
  `follow_amount` int(11) NOT NULL DEFAULT '0' COMMENT '关注数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for t_user_request
-- ----------------------------
DROP TABLE IF EXISTS `t_user_request`;
CREATE TABLE `t_user_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `last_update_time` datetime NOT NULL COMMENT '修改时间',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户请求';

-- ----------------------------
-- Table structure for t_video_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_video_comment`;
CREATE TABLE `t_video_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `video_id` bigint(20) NOT NULL COMMENT '视频ID',
  `is_valid` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否合法',
  `user_id` bigint(20) NOT NULL COMMENT '评论者ID',
  `comment` varchar(500) NOT NULL COMMENT '评论',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户对视频的评论';

-- ----------------------------
-- Table structure for t_video_upload_token
-- ----------------------------
DROP TABLE IF EXISTS `t_video_upload_token`;
CREATE TABLE `t_video_upload_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `expire_time` bigint(20) NOT NULL,
  `ext` varchar(255) DEFAULT NULL,
  `file_hash` varchar(255) DEFAULT NULL,
  `origin_name` varchar(255) DEFAULT NULL,
  `owner_team_id` bigint(20) DEFAULT NULL,
  `stored_key` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `upload_complete` bit(1) NOT NULL,
  `bucket` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `audit_user_id` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_stored_key` (`stored_key`),
  KEY `index_stored_key` (`stored_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
