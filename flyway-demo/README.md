## 概念

### 什么是Flyway

Flyway 是一个能对数据库变更做版本控制的工具。

### 为什么要用Flyway

在多人开发的项目中，我们都习惯了使用 SVN 或者 Git 来对代码做版本控制，主要的目的就是为了解决多人开发代码冲突和版本回退的问题。

其实，数据库的变更也需要版本控制，在日常开发中，我们经常会遇到下面的问题：

1. 自己写的 SQL 忘了在所有环境执行；
2. 别人写的 SQL 我们不能确定是否都在所有环境执行过了；
3. 有人修改了已经执行过的 SQL，期望再次执行；
4. 需要新增环境做数据迁移；
5. 每次发版需要手动控制先发 DB 版本，再发布应用版本；
6. 其它场景...

有了Flyway，这些问题都能得到很好的解决。

<!-- more -->

### Flyway的特点

* 简单。非常容易安装和学习，同时迁移的方式也很容易被开发者接受。
* 转移。Flyway 专注于搞数据库迁移、版本控制而并没有其它副作用。
* 强大。专为连续交付而设计。让Flyway在应用程序启动时迁移数据库。

### Flyway的工作机制

Flyway 需要在 DB 中先创建一个 metadata 表 (缺省表名为 `flyway_schema_history`), 在该表中保存着每次 migration （迁移）的记录, 记录包含 migration 脚本的版本号和 SQL 脚本的 checksum 值。

Flyway 扫描文件系统或应用程序的类路径读取 DDL 和 DML 以进行迁移。  
根据metadata 表进行检查迁移。  
**如果脚本声明的版本号小于或等于标记为当前版本的版本号之一，将忽略它们**。  
其余迁移是待处理迁移：可用，但未应用。最后按版本号对它们进行排序并按顺序执行 并将执行结果写入 metadata 表。

## 最佳实践

通过 [Aliyun Java Initializr](https://start.aliyun.com/bootstrap.html) 创建项目

![依赖](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/04/20220416100653-89c795.png?x-oss-process=style/yuantu_shuiyin)

我使用的是 PostgreSQL，创建一个库。

> create database test_flyway;

在`src/main/resources`目录下面新建`db.migration`文件夹，默认情况下，该目录下的.sql 文件就算是需要被 flyway 做版本控制的数据库 SQL 语句。

但是此处的 SQL 语句命名需要遵从一定的规范，否则运行的时候 flyway 会报错。命名规则主要有两种：

* **仅需被执行一次的 SQL** 。文件命名以大写`V`字开头，后面可跟上`0-9`数字，数字之间可用`.`或`_`分隔开，然后再以**两个下划线**分隔，后面为文件名，最后以`.sql`结尾。例：`V1.0.0__init.sql`

* **可重复运行的SQL**。只要脚本内容发生了变化，项目启动时脚本就会被重新执行。文件名以大写`R`字开头，后面再以两个下划线分隔后面为文件名，最后以`.sql`结尾。

项目配置。

```yaml
spring:
  application:
    name: flyway-test
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://192.168.1.56:5432/test_flyway?&ssl=false&stringtype=unspecified
    username: postgres
    password: postgres
server:
  port: 8080
```

初次启动，报错。因为无任何内容。

![初次启动，报错](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/04/20220416102645-c2a0f3.png?x-oss-process=style/yuantu_shuiyin)

### 仅需被执行一次的

文件命名以大写`V`字开头，后面可跟上`0-9`数字，数字之间可用`.`或`_`分隔开，然后再以**两个下划线**分隔，后面为文件名，最后以`.sql`结尾。例：`V1.0.0__init.sql`

创建第一个脚本：`V1.0.0_init.sql`

```sql
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
```
![目录结构](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/04/20220426145200-3f8d93.png?x-oss-process=style/yuantu_shuiyin)

我在 `db.migration` 下还创建了一个 1.0.0 的文件夹，这个不会影响 flyway 对 sql 的识别，可以自行取名和分类。

运行成功。

![运行成功](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/04/20220426145647-1671cc.png?x-oss-process=style/yuantu_shuiyin)

创建了`flyway_schema_history`表，并成功执行一个脚本。

![数据库内容](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/04/20220426145954-9e8553.png?x-oss-process=style/yuantu_shuiyin)

`flyway_schema_history`表中记录的是脚本信息和脚本执行状态。

![flyway_schema_history](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/04/20220426145919-9db8f9.png?x-oss-process=style/yuantu_shuiyin)

有了这条记录，下次再启动项目，`1.0.0/V1.0.0__init.sql` 这个脚本文件就不会执行了，因为系统知道这个脚本已经执行过了。  
如果你还想让 `1.0.0/V1.0.0__init.sql`脚本再执行一遍，需要手动删除`flyway_schema_history`  表中的对应记录，那么项目启动时，这个脚本就会被执行了。

### 可重复执行的脚本

只要脚本内容发生了变化，项目启动时脚本就会被重新执行。文件名以大写`R`字开头，后面再以两个下划线分隔后面为文件名，最后以`.sql`结尾。

接着在 `db/migration/1.0.0/`下创建`R__init_user.sql`

```sql
insert into tb_user(username, nickname, age) VALUES ('admin','admin',18);
insert into tb_user(username, nickname, age) VALUES ('sys','sys',28);
```

重新启动项目，运行成功。  
数据库`tb_user`表中新增两条数据。  
`flyway_schema_history`新增了一条记录，但 version 字段上为 null。

![flyway_schema_history](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/04/20220426151708-8b7aab.png?x-oss-process=style/yuantu_shuiyin)

重新运行程序第二遍，并未发生变化。

重新修改`R__init_user.sql` 脚本。

```sql
insert into tb_user(username, nickname, age) VALUES ('test','test',18);
```
重新运行程序，脚本执行成功，数据变更成功。

![tb_user](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/04/20220426152833-f7de31.png?x-oss-process=style/yuantu_shuiyin)


## 错误验证

### 刻意修改仅需被执行一次的脚本

当直接在`V1.0.0__init.sql`脚本上进行修改时。重新启动程序会进行报错。

![报错](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/04/20220426153132-e37ecd.png?x-oss-process=style/yuantu_shuiyin)

原因：会检查数据库中对应脚本的`checksum`字段，发现不对应则说明脚本进行了修改，所以阻止运行。

将修改的内容还原，则可重新运行。  
或者删除`flyway_schema_history`表中`V1.0.0__init.sql`脚本对应的记录，则可重新执行`V1.0.0__init.sql`脚本。


### 可重复执行的脚本若存在错误

刻意在`R__init_user.sql`脚本中添加一条错误的脚本，让其`id`与库中已存在的数据冲突。

顺便验证数据是否会回滚。

```sql
insert into tb_user(username, nickname, age) VALUES ('test1','test1',18);
insert into tb_user(username, nickname, age) VALUES ('test2','test2',28);
insert into tb_user(id, username, nickname, age) VALUES (1, 'test2','test2',28);
```

从报错信息中能很明显的看出来问题，直接给出了具体的语句和错误原因。  
然后上面的语句也未成功添加。

![报错信息](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/04/20220426154032-2e57e6.png?x-oss-process=style/yuantu_shuiyin)


## 配置

1. 生产务必禁 spring.flyway.cleanDisabled=false 。
2. spring.flyway.outOfOrder 取值 生产上使用 true，开发中使用 false。
3. 多个系统公用一个 数据库 schema 时配置spring.flyway.table 为不同的系统设置不同的 metadata 表名而不使用缺省值 `flyway_schema_history` 。
4. 开发版本号尽量根据团队来进行多层次的命名避免混乱。比如 `V1.0.1__ProjectName_{feat|fix}_Description.sql` ，这种命名同时也可以获取更多脚本的开发者和相关功能的信息。

```yaml
# flyway 配置
spring:
  flyway:
    # 启用或禁用 flyway
    enabled: true
    # flyway 的 clean 命令会删除指定 schema 下的所有 table, 生产务必禁掉。这个默认值是 false 理论上作为默认配置是不科学的。
    clean-disabled: true
    # SQL 脚本的目录,多个路径使用逗号分隔 默认值 classpath:db/migration
    locations: classpath:db/migration
    #  metadata 版本控制信息表 默认 flyway_schema_history
    table: flyway_schema_history
    # 如果没有 flyway_schema_history 这个 metadata 表， 在执行 flyway migrate 命令之前, 必须先执行 flyway baseline 命令
    # 设置为 true 后 flyway 将在需要 baseline 的时候, 自动执行一次 baseline。
    baseline-on-migrate: true
    # 指定 baseline 的版本号,默认值为 1, 低于该版本号的 SQL 文件, migrate 时会被忽略
    baseline-version: 1
    # 字符编码 默认 UTF-8
    encoding: UTF-8
    # 是否允许不按顺序迁移 开发建议 false  生产建议 ture
    out-of-order: false
    # 需要 flyway 管控的 schema list,这里我们配置为flyway  缺省的话, 使用spring.datasource.url 配置的那个 schema,
    # 可以指定多个schema, 但仅会在第一个schema下建立 metadata 表, 也仅在第一个schema应用migration sql 脚本.
    # 但flyway Clean 命令会依次在这些schema下都执行一遍. 所以 确保生产 spring.flyway.clean-disabled 为 true
    schemas: flyway
    # 执行迁移时是否自动调用验证   当你的 版本不符合逻辑 比如 你先执行了 DML 而没有 对应的DDL 会抛出异常
    validate-on-migrate: true
```

配置全路径`org.springframework.boot.autoconfigure.flyway.FlywayProperties`

```yaml
flyway.baseline-description对执行迁移时基准版本的描述.
flyway.baseline-on-migrate当迁移时发现目标schema非空，而且带有没有元数据的表时，是否自动执行基准迁移，默认false.
flyway.baseline-version开始执行基准迁移时对现有的schema的版本打标签，默认值为1.
flyway.check-location检查迁移脚本的位置是否存在，默认false.
flyway.clean-on-validation-error当发现校验错误时是否自动调用clean，默认false.
flyway.enabled是否开启flywary，默认true.
flyway.encoding设置迁移时的编码，默认UTF-8.
flyway.ignore-failed-future-migration当读取元数据表时是否忽略错误的迁移，默认false.
flyway.init-sqls当初始化好连接时要执行的SQL.
flyway.locations迁移脚本的位置，默认db/migration.
flyway.out-of-order是否允许无序的迁移，默认false.
flyway.password目标数据库的密码.
flyway.placeholder-prefix设置每个placeholder的前缀，默认${.
flyway.placeholder-replacementplaceholders是否要被替换，默认true.
flyway.placeholder-suffix设置每个placeholder的后缀，默认}.
flyway.placeholders.[placeholder name]设置placeholder的value
flyway.schemas设定需要flywary迁移的schema，大小写敏感，默认为连接默认的schema.
flyway.sql-migration-prefix迁移文件的前缀，默认为V.
flyway.sql-migration-separator迁移脚本的文件名分隔符，默认__
flyway.sql-migration-suffix迁移脚本的后缀，默认为.sql
flyway.tableflyway使用的元数据表名，默认为schema_version
flyway.target迁移时使用的目标版本，默认为latest version
flyway.url迁移时使用的JDBC URL，如果没有指定的话，将使用配置的主数据源
flyway.user迁移数据库的用户名
flyway.validate-on-migrate迁移时是否校验，默认为true.
```

## 总结

`V`开头的脚本，内容不变运行，语句并不会执行，内容修改再运行，程序启动失败！

`R`开头的脚本，内容不变运行，语句并不会执行，内容修改过再运行，**会将文件中的所有语句执行**！

每次程序启动会检验目前项目中的 sql 文件名称和文件夹名是否与数据库中存储的对应。  
如果不对应则抛出相应的错误，程序启动失败。

`R`开头的脚本，更像是**当前版本的一个临时补充的区域，为了及时修改当下紧急更新的内容**。


源代码地址：
> https://github.com/rcbb-cc/fast-start-guide


## 参考

[flyway常用配置_Spring Boot 2 实战：使用 Flyway 管理你数据库的版本变更](https://blog.csdn.net/weixin_42300167/article/details/112417080)