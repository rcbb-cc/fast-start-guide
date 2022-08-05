create table tb_user
(
    id          bigserial primary key,
    username    varchar(30) not null,
    nickname    varchar(30),
    age         smallint,
    create_time bigint,
    update_time bigint
);

comment on table tb_user is '用户表';
comment on column tb_user.id is '用户ID';
comment on column tb_user.username is '用户名';
comment on column tb_user.nickname is '用户昵称';
comment on column tb_user.age is '年龄';
comment on column tb_user.create_time is '创建时间';
comment on column tb_user.update_time is '更新时间';
