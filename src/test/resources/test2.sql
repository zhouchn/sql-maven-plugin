CREATE TABLE `apc_auth_user`
(
    `id`                   bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`             varchar(64)  NOT NULL COMMENT '用户名，登陆用',
    `password`             varchar(64)  NOT NULL COMMENT '登陆密码，支持账号、邮箱、手机登陆',
    `nick_name`            varchar(64)           DEFAULT NULL COMMENT 'nick name',
    `email`                varchar(128) NOT NULL COMMENT '邮箱',
    `phone`                varchar(32)           DEFAULT NULL COMMENT '电话',
    `status`               int          NOT NULL DEFAULT '1' COMMENT '状态：1启用，0禁用',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='账户表';
CREATE TABLE `apc_auth_organization`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`          varchar(64) NOT NULL COMMENT '名称',
    `parent_id`     bigint               DEFAULT NULL COMMENT '上级主键Id',
    `remark`        varchar(255)         DEFAULT NULL COMMENT '备注，说明',
    `status`        int         NOT NULL DEFAULT '1' COMMENT '状态：1启用，0禁用',
    `create_time`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `creator`       bigint unsigned DEFAULT NULL,
    `modifier`      bigint unsigned DEFAULT NULL,
    `deleted`       int         NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='组织机构表';