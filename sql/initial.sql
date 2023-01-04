CREATE TABLE `base_server_state_log`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `server_name` varchar(50) DEFAULT NULL COMMENT '服务名称',
    `state`       varchar(15) DEFAULT NULL COMMENT '服务器最新状态',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='服务器状态变化日志表';

CREATE TABLE `base_node_heartbeat`
(
    `id`                    bigint(20) NOT NULL AUTO_INCREMENT,
    `server_name`           varchar(50) DEFAULT NULL COMMENT '服务名称',
    `latest_heartbeat_time` datetime    DEFAULT NULL COMMENT '最新心跳时间',
    `create_time`           datetime NOT NULL COMMENT '创建时间',
    `update_time`           datetime NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='节点心跳信息';

CREATE TABLE `base_node_heartbeat_log`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `server_name` varchar(50) DEFAULT NULL COMMENT '服务名称',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='节点心跳历史日志表';

CREATE TABLE `iov_config`
(
    `id`                    bigint(20) NOT NULL AUTO_INCREMENT,
    `iov_type`           varchar(15) DEFAULT NULL COMMENT 'iov类型',
    `config_info` varchar(200)    DEFAULT NULL COMMENT '配置信息',
    `create_time`           datetime NOT NULL COMMENT '创建时间',
    `update_time`           datetime NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='Iov配置表';
