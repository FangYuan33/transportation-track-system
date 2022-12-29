CREATE TABLE `base_server_state_log`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `server_name` varchar(50) DEFAULT NULL,
    `state`       varchar(15) DEFAULT NULL COMMENT '服务器最新状态',
    `create_time` datetime    NOT NULL COMMENT '创建时间',
    `update_time` datetime    NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='服务器状态变化日志表';

