# spring-boot-klock-starter

基于 Redis 的分布式锁 spring-boot starter 组件，使得项目拥有分布式锁能力变得异常简单。

[klock-demo Github地址](https://github.com/rcbb-cc/fast-start-guide)

<!-- more -->

## 快速开始

SpringBoot 项目接入。

### 核心依赖

```xml
<dependency>
    <groupId>cn.keking</groupId>
    <artifactId>spring-boot-klock-starter</artifactId>
    <version>1.4-RELEASE</version>
</dependency>
```

### 核心配置

```yaml
spring:
  klock:
    address: 127.0.0.1:6379 #redis链接地址
    password: 2021 #redis密码
    database: 1 #redis数据索引
    waitTime: 60 #获取锁最长阻塞时间（默认：60，单位：秒）
    leaseTime: 60 #已获取锁后自动释放时间（默认：60，单位：秒）
#    cluster-server:
#      node-addresses: #redis集群配置 如 127.0.0.1:7000,127.0.0.1:7001，127.0.0.1:7002
```

spring.klock.address 和 spring.klock.cluster-server.node-addresses 选其一即可

### 使用

使用非常简单，在需要加分布式锁的方法上，添加注解 @Klock 即可。

```java
@Klock
@Override
public List<User> list(String userName) {
    log.info("[<{}> 进入UserService.list() 成功获取锁]", userName);

    try {
        Thread.sleep(10 * 1000);
    } catch (Exception e) {}

    List<User> list = new ArrayList<>();
    list.add(new User().setId(1).setName("Jack").setAge(18));
    list.add(new User().setId(2).setName("Tom").setAge(20));
    log.info("[<{}> UserService.list() 执行完毕，释放锁]", userName);
    return list;
}
```

## @Klock 注解参数说明

```java
/**
 * @author kl
 * @since 2017/12/29
 * Content :加锁注解
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Klock {
    /**
     * 锁的名称 （对应redis的key值。默认为：类名+方法名）
     */
    String name() default "";
    /**
     * 锁类型（目前支持可重入锁，公平锁，读写锁。默认为：公平锁）
     */
    LockType lockType() default LockType.Reentrant;
    /**
     * 尝试加锁，最多等待时间（获取锁最长等待时间。默认为：60s。可通过spring.klock.waitTime统一配置）
     */
    long waitTime() default Long.MIN_VALUE;
    /**
     *上锁以后xxx秒自动解锁（获得锁后，自动释放锁的时间。默认为：60s。可通过spring.klock.leaseTime统一配置）
     */
    long leaseTime() default Long.MIN_VALUE;

    /**
     * 自定义业务key
     */
     String [] keys() default {};

     /**
     * 加锁超时的处理策略，可配置为不做处理、快速失败、阻塞等待的处理策略，默认策略为不做处理
     */
     LockTimeoutStrategy lockTimeoutStrategy() default LockTimeoutStrategy.NO_OPERATION;

    /**
     * 自定义加锁超时的处理策略，需指定自定义处理的方法的方法名，并保持入参一致。
     */
     String customLockTimeoutStrategy() default "";

     /**
     * 释放锁时，持有的锁已超时的处理策略，可配置为不做处理、快速失败的处理策略，默认策略为不做处理
     */
     ReleaseTimeoutStrategy releaseTimeoutStrategy() default ReleaseTimeoutStrategy.NO_OPERATION;

    /**
     * 自定义释放锁时，需指定自定义处理的方法的方法名，并保持入参一致。
     */
     String customReleaseTimeoutStrategy() default "";
}
```

## 测试使用

源码地址：https://github.com/rcbb-cc/klock-demo

### 方法级加锁

核心代码如下图，最简单的使用，获取用户列表数据。

Service 实现代码中会睡眠 10s。

![核心代码](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/06/20210610153219-5e80ab.png?x-oss-process=style/yuantu_shuiyin)

让 zhangsan、lisi 同一时间调用。

> http://127.0.0.1:8081/user?userName=lisi

> http://127.0.0.1:8081/user?userName=zhangsan

zhangsan、lisi 几乎同时进入 Controller 中，但只有一个用户能进入的 Service 的方法中，当进入的这个用户执行完毕后，后面的用户才能进入。

![执行结果](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/06/20210610153732-26a6e5.png?x-oss-process=style/yuantu_shuiyin)

通过 Redis 可视化工具可以看到，有一个`lock.cc.rcbb.klock.test.service.UserServiceImpl.list`类名+方法名的 key，过期时间为默认的 60s。
![Redis显示结果](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/06/20210610154410-3382ba.png?x-oss-process=style/yuantu_shuiyin)

### 针对用户/变量级加锁

核心代码如下图，根据 userId 获取该用户信息。

关键点：`@Klock(keys = {"#userId"})` key 根据 userId 产生。

![根据userId获取该用户信](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/06/20210610171646-075866.png?x-oss-process=style/yuantu_shuiyin)

请求一：

> http://127.0.0.1:8081/user/1

请求二：

> http://127.0.0.1:8081/user/2

请求三：

> http://127.0.0.1:8081/user/2

表现结果：请求一和请求二可同时进入 Service，但请求二和请求三无法同时进入 Service。

![Redis显示结果](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/06/20210610172551-d07ec7.png?x-oss-process=style/yuantu_shuiyin)