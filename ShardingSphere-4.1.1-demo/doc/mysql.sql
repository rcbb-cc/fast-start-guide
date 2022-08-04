create table t_order0
(
    order_id    bigint comment '订单流水ID',
    user_id     bigint comment '用户ID',
    order_no    varchar(100) comment '订单编号',
    create_time date comment '创建时间',
    primary key (order_id)
);

alter table t_order0
    comment '订单表';


create table t_position_2021_9_16
(
    position_id bigint comment '唯一ID',
    address     varchar(100) comment '地址',
    loc_date    date comment '定位日期',
    primary key (position_id)
);

alter table t_position_2021_9_16
    comment '位置表';