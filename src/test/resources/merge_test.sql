CREATE TABLE `test_user` (
    `id`                   bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`             varchar(64)  NOT NULL COMMENT '用户名，登陆用',
    `password`             varchar(64)  NOT NULL COMMENT '登陆密码，支持账号、邮箱、手机登陆',
    `nick_name`            varchar(64)           DEFAULT NULL COMMENT 'nick name',
    `email`                varchar(128) NOT NULL COMMENT '邮箱',
    `phone`                varchar(32)           DEFAULT NULL COMMENT '电话',
    `status`               int unsigned NOT NULL DEFAULT '1' COMMENT '状态：1启用，0禁用',
    `locked`               int          NOT NULL DEFAULT '0' COMMENT '状态：1锁定，0未锁定',
    `organization_id`      bigint                DEFAULT NULL COMMENT '组织机构主键Id',
    `allowed_child_data`   int                   DEFAULT '0' COMMENT '是否允许管理下级机构数据',
    `category`             int                   DEFAULT '1' COMMENT '账号类型，1平台账号，2内部账号',
    `is_system_user`       int                   DEFAULT '0' COMMENT '是否系统用户，1是，0否',
    `is_first_login`       int                   DEFAULT '1' COMMENT '是否首次登录，1是，0否',
    `password_update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '密码修改时间',
    `create_time`          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `creator`              bigint unsigned DEFAULT NULL,
    `modifier`             bigint unsigned DEFAULT NULL,
    `deleted`              int          NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_username` (`username`) USING BTREE COMMENT '登录名唯一'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户表';

INSERT INTO `apc_auth_resource` (`id`, `name`, `icon`, `type`, `path`, `component`, `application`, `parent_id`, `remark`, `status`, `sort`, `permission`, `create_time`, `modified_time`, `creator`, `modifier`, `deleted`) VALUES (20000, '匿踪服务', NULL, 'FOLDER', '/hide-api', NULL, 'Serving', NULL, NULL, 1, 0, NULL, '2022-11-10 09:53:36', '2022-11-10 09:53:36', NULL, NULL, 0);
INSERT INTO `apc_auth_resource` (`id`, `name`, `icon`, `type`, `path`, `component`, `application`, `parent_id`, `remark`, `status`, `sort`, `permission`, `create_time`, `modified_time`, `creator`, `modifier`, `deleted`) VALUES (20100, '我的匿踪API', NULL, 'MENU', '/hide-api/list', NULL, 'Serving', 20000, NULL, 1, 0, 'menu:pir:self', '2022-11-10 09:53:36', '2022-11-10 09:53:36', NULL, NULL, 0);
INSERT INTO `apc_auth_resource` (`id`, `name`, `icon`, `type`, `path`, `component`, `application`, `parent_id`, `remark`, `status`, `sort`, `permission`, `create_time`, `modified_time`, `creator`, `modifier`, `deleted`) VALUES (20101, '查询', NULL, 'BUTTON', NULL, NULL, 'Serving', 20100, NULL, 1, 0, 'ser:api:pir:list', '2022-11-10 09:53:36', '2022-11-10 09:53:36', NULL, NULL, 0);

ALTER TABLE `test_user`
    ADD COLUMN `digest` varchar(64) NOT NULL DEFAULT '' COMMENT 'sha256 摘要' after category,
    ADD COLUMN `digest_length` varchar(64) NOT NULL DEFAULT '' COMMENT 'sha256 摘要length';

ALTER TABLE `test_user` MODIFY COLUMN `digest` varchar(40) NOT NULL DEFAULT '' COMMENT 'sha256 摘要(update)';

ALTER TABLE `test_user` CHANGE `digest_length` `digest_size` varchar(64) NOT NULL DEFAULT '' COMMENT 'sha256 摘要length(update)';
ALTER TABLE `test_user` RENAME COLUMN `is_system_user` TO `sys_user`;

ALTER TABLE `test_user` DROP COLUMN `password_update_time`;

ALTER TABLE `test_user`
    ADD KEY `idx_name_status` (`nick_name`,`status`),
    ADD UNIQUE INDEX `unq_kkk` (`phone`),
    DROP INDEX uk_user_username;

CREATE INDEX idx_search_action on `test_user`(`create_time`);
CREATE UNIQUE INDEX unq_email on `test_user`(`email`);

ALTER TABLE `test_user`
    DROP INDEX `idx_name_status`,
    ADD UNIQUE INDEX `unq_test` (`username`,`email`) USING BTREE;

DROP TABLE IF EXISTS aaa;

drop index `unq_kkk` on test_user;