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
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `iov_type`    varchar(15)  DEFAULT NULL COMMENT 'iov类型',
    `config_info` varchar(200) DEFAULT NULL COMMENT '配置信息',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='Iov配置表';

CREATE TABLE `iov_subscribe_task`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `iov_config_id` bigint(20) NOT NULL COMMENT 'iov配置ID',
    `carrier_code`  varchar(20) NOT NULL COMMENT '承运商编码',
    `server_name`   varchar(50) DEFAULT NULL COMMENT '服务名称',
    `state`         int(2) NOT NULL COMMENT '任务状态: 10: 已启动待分配 20: 已分配 30: 运行中 40: 异常中断 50: 正常停止',
    `create_time`   datetime    NOT NULL COMMENT '创建时间',
    `update_time`   datetime    NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='Iov任务订阅表';

CREATE TABLE `iov_subscribe_task_vehicle`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `task_id`     bigint(20) NOT NULL COMMENT 'iov任务订阅ID',
    `vehicle_no`  varchar(20) NOT NULL COMMENT '车牌号',
    `create_time` datetime    NOT NULL COMMENT '创建时间',
    `update_time` datetime    NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='Iov任务车辆订阅明细表';