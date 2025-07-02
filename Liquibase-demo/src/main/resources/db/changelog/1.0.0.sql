DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user
(
    id          bigint      NOT NULL COMMENT 'id',
    username    varchar(50) NOT NULL COMMENT '用户名',
    password    varchar(100) COMMENT '密码',
    super_admin tinyint unsigned COMMENT '超级管理员   0：否   1：是',
    status      tinyint COMMENT '状态  0：停用   1：正常',
    create_date datetime COMMENT '创建时间',
    updater     bigint COMMENT '更新者',
    creator     bigint COMMENT '创建者',
    update_date datetime COMMENT '更新时间',
    primary key (id),
    unique key uk_username (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户';

DROP TABLE IF EXISTS sys_user_token;

CREATE TABLE sys_user_token
(
    id          bigint       NOT NULL COMMENT 'id',
    user_id     bigint       NOT NULL COMMENT '用户id',
    token       varchar(100) NOT NULL COMMENT '用户token',
    expire_date datetime COMMENT '过期时间',
    update_date datetime COMMENT '更新时间',
    create_date datetime COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY user_id (user_id),
    UNIQUE KEY token (token)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户Token';
