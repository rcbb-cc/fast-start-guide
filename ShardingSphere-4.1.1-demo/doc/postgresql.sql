create table t_order0
(
    order_id    bigint,
    user_id     bigint,
    order_no    varchar(100),
    create_time date,
    primary key (order_id)
);
comment on table t_order0 is '订单表 ';
comment on column t_order0.order_id is '订单流水ID';
comment on column t_order0.user_id is '用户ID';
comment on column t_order0.order_no is '订单编号';
comment on column t_order0.create_time is '创建时间';

create table t_order1
(
    order_id    bigint,
    user_id     bigint,
    order_no    varchar(100),
    create_time date,
    primary key (order_id)
);
comment on table t_order1 is '订单表 ';
comment on column t_order1.order_id is '订单流水ID';
comment on column t_order1.user_id is '用户ID';
comment on column t_order1.order_no is '订单编号';
comment on column t_order1.create_time is '创建时间';

create table t_order2
(
    order_id    bigint,
    user_id     bigint,
    order_no    varchar(100),
    create_time date,
    primary key (order_id)
);
comment on table t_order2 is '订单表 ';
comment on column t_order2.order_id is '订单流水ID';
comment on column t_order2.user_id is '用户ID';
comment on column t_order2.order_no is '订单编号';
comment on column t_order2.create_time is '创建时间';

create table t_order3
(
    order_id    bigint,
    user_id     bigint,
    order_no    varchar(100),
    create_time date,
    primary key (order_id)
);
comment on table t_order3 is '订单表 ';
comment on column t_order3.order_id is '订单流水ID';
comment on column t_order3.user_id is '用户ID';
comment on column t_order3.order_no is '订单编号';
comment on column t_order3.create_time is '创建时间';


create table t_position_2021_9_16
(
    position_id bigint,
    address     varchar(100),
    loc_date    date,
    primary key (position_id)
);
comment on table t_position_2021_9_16 is '位置表 ';
comment on column t_position_2021_9_16.position_id is '唯一ID';
comment on column t_position_2021_9_16.address is '地址';
comment on column t_position_2021_9_16.loc_date is '定位日期';