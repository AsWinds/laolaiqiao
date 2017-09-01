# t_fans
ALTER TABLE t_fans ADD UNIQUE KEY `uk_f_t_u_id` (`fans_id`, `team_id`, `user_id`);
ALTER TABLE t_fans ADD INDEX idx_f_t_u_id (fans_id, team_id, user_id);

# t_video_upload_token
ALTER TABLE t_video_upload_token DROP INDEX index_stored_key;
ALTER TABLE t_video_upload_token DROP KEY UK_stored_key;

# t_video_upload_token
ALTER TABLE t_video_upload_token ADD UNIQUE KEY uk_b_sk (bucket, stored_key);
ALTER TABLE t_video_upload_token ADD INDEX idx_b_sk (bucket, stored_key);



-- ----------------------------
-- Table structure for t_sys_img
-- ----------------------------
CREATE TABLE `t_sys_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `bucket` varchar(255) NOT NULL,
  `expire_time` bigint(20) DEFAULT NULL,
  `origin_name` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `stored_key` varchar(255) NOT NULL,
  `upload_complete` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_b_sk` (`bucket`,`stored_key`),
  KEY `idx_b_sk` (`bucket`,`stored_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_user_album
-- ----------------------------
CREATE TABLE `t_user_album` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_user_img
-- ----------------------------
CREATE TABLE `t_user_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `bucket` varchar(255) NOT NULL,
  `expire_time` bigint(20) DEFAULT NULL,
  `origin_name` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `stored_key` varchar(255) NOT NULL,
  `upload_complete` bit(1) NOT NULL,
  `album_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_b_sk` (`bucket`,`stored_key`),
  KEY `idx_b_sk` (`bucket`,`stored_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/** 2017年3月31日14:17:23 */
CREATE TABLE `t_user_join_team` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `created_at` bigint(20) NOT NULL,
    `last_update_time` bigint(20) DEFAULT NULL,
    `version` int(11) DEFAULT '0',
    `user_id` bigint(20) NOT NULL COMMENT '申请的用户ID',
    `team_id` bigint(20) NOT NULL COMMENT '被申请加入的团队ID',
    `status` varchar(45) NOT NULL COMMENT '申请状态。\n‘auditing’:审核中；\n‘rejected’:被拒绝；\n‘approved’：审核通过',
    `audit_user_id` bigint(20) DEFAULT NULL COMMENT '负责审核的用户ID;一般为团长',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户申请加入团队的请求';

/** 2017年3月31日17:31:14 */
ALTER TABLE `t_user`
ADD COLUMN `province` VARCHAR(45) NULL COMMENT '省' AFTER `follow_amount`,
ADD COLUMN `city` VARCHAR(200) NULL COMMENT '市' AFTER `province`,
ADD COLUMN `district` VARCHAR(200) NULL COMMENT '区/县；行政区' AFTER `city`,
ADD COLUMN `street` VARCHAR(200) NULL COMMENT '街道/镇' AFTER `district`;
ALTER TABLE `t_user`
CHANGE COLUMN `address` `address` VARCHAR(255) NULL DEFAULT NULL COMMENT '用户地址；省+市+区+街道' ;



/** TODO 最后交给测试之前，要将后台管理的数据导进来  added by song-jj 2017年3月30日13:43:06 */
