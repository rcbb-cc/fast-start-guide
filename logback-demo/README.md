## 前言

项目中日志系统是必不可少的，目前比较流行的日志框架有 log4j、logback 等，可能大家还不知道，这两个框架的作者是同一个人，Logback 旨在作为流行的 log4j 项目的后续版本，从而恢复 log4j 离开的位置。

另外 slf4j(Simple Logging Facade for Java) 则是一个日志门面框架，提供了日志系统中常用的接口，logback 和 log4j 则对slf4j 进行了实现。

我们本文将讲述如何在 SpringBoot 中应用 logback+slf4j 实现日志的记录。

## 为什么使用logback

- Logback 是log4j 框架的作者开发的新一代日志框架，它效率更高、能够适应诸多的运行环境，同时天然支持 SLF4J。
- Logback 的定制性更加灵活，同时也是 SpringBoot 的内置日志框架。

## 开始使用

## 一、添加依赖

实际开发中我们直接引入`spring-boot-starter-web`依赖即可，因为`spring-boot-starter-web`包含了`spring-boot-starter`。  
而`spring-boot-starter`包含了`spring-boot-starter-logging`，所以我们只需要引入 web 组件即可。

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## 二、默认配置

默认情况下 SpringBoot 将日志输出到控制台，不会写到日志文件。

如果要编写除控制台输出之外的日志文件，则需在 application.properties 中设置 logging.file 或 logging.path 属性。  
注：二者不能同时使用，如若同时使用，则只有 logging.file 生效。

- logging.file=文件名（设置文件，可以是绝对路径，也可以是相对路径。例：logging.file=my.log）

- logging.path=日志文件路径（设置目录，会在该目录下创建spring.log文件，并写入日志内容。例：logging.path=/var/log）

- logging.level.包名=指定包下的日志级别

- logging.pattern.console=日志打印规则

可以看到这种方式配置简单，但是能实现的功能也非常有限，如果想要更复杂的需求，就需要下面的定制化配置了。

## 三、logback-spring.xml详解

SpringBoot 官方推荐优先使用带有`-spring`的文件名作为你的日志配置（如使用`logback-spring.xml`，而不是`logback.xml`），命名为`logback-spring.xml`的日志配置文件，将 xml 放至`src/main/resource`下面。

也可以使用自定义的名称，比如`logback-config.xml`，只需要在 application.properties 文件中使用`logging.config=classpath:logback-config.xml`指定即可。

在讲解 logback-spring.xml之前我们先来了解三个单词：

- Logger（记录器）
- Appenders（附加器）
- Layouts（布局）

Logback 基于三个主要类：Logger，Appender和Layout。

这三种类型的组件协同工作，使开发人员能够根据消息类型和级别记录消息，并在运行时控制这些消息的格式以及报告的位置。

首先给出一个基本的 xml 配置如下：

```
<configuration>
 
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <!-- encoders are assigned the type
         ch.qos.logback.classic.encoder.PatternLayoutEncoder by default -->
    <encoder>
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
  </appender>
 
  <logger name="chapters.configuration" level="INFO"/>
 
  <!-- Strictly speaking, the level attribute is not necessary since -->
  <!-- the level of the root level is set to DEBUG by default.       -->
  <root level="DEBUG">          
    <appender-ref ref="STDOUT" />
  </root>  
  
</configuration>
```

### 3.1、`<configuration>`元素

logback.xml 配置文件的基本结构可以描述为`<configuration>`元素，包含零个或多个`<appender>`元素，后跟零个或多个`<logger>`元素，后跟最多一个`<root>`元素(也可以没有)。

下图说明了这种基本结构：

![configuration元素](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/08/20220816135959-14ab0e.png?x-oss-process=style/yuantu_shuiyin)

### 3.2、`<logger>`元素

`<logger>`元素只接受一个必需的 name 属性，一个可选的 level 属性和一个可选的 additivity 属性，允许值为 true 或 false 。

level 属性的值允许一个不区分大小写的字符串值 TRACE，DEBUG，INFO，WARN，ERROR，ALL 或 OFF。

`<logger>`元素可以包含零个或多个`<appender-ref>`元素。

这样引用的每个 appender 都被添加到指定的 logger 中，logger 元素级别具有继承性。

例1：仅为根记录器分配了级别。此级别值 DEBUG 由其他记录器 X，X.Y和X.Y.Z 继承。

![继承性-例1](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/08/20220816140326-69a802.png?x-oss-process=style/yuantu_shuiyin)

例2：所有记录器都有一个指定的级别值。级别继承不起作用。

![继承性-例2](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/08/20220816140424-2d88f7.png?x-oss-process=style/yuantu_shuiyin)

例3：记录器 root，X 和 X.Y.Z 分别被分配了 DEBUG，INFO 和 ERROR 级别。Logger X.Y 从其父 X 继承其级别值。

![继承性-例3](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/08/20220816140547-5d34fc.png?x-oss-process=style/yuantu_shuiyin)

例4：记录器 root 和 X 分别被分配了 DEBUG 和 INFO 级别。记录器 X.Y 和 X.Y.Z 从其最近的父 X 继承其级别值，该父级具有指定的级别。

![继承性-例4](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/08/20220816140710-ad2eee.png?x-oss-process=style/yuantu_shuiyin)

### 3.3、`<root>`元素

`<root>`元素配置根记录器。  
它支持单个属性，即 level 属性。  
它不允许任何其他属性，因为 additivity 标志不适用于根记录器。  
此外，由于根记录器已被命名为 ROOT ，因此它也不允许使用 name 属性。

level 属性的值可以是不区分大小写的字符串 TRACE，DEBUG，INFO，WARN，ERROR，ALL 或 OFF 之一。

`<root>`元素可以包含零个或多个`<appender-ref>`元素。

这样引用的每个 appender 都被添加到根记录器中。

### 3.4、`<appender>`元素

appender 使用`<appender>`元素配置，该元素采用两个必需属性 name 和 class。  
name 属性指定 appender 的名称，而 class 属性指定要实例化的 appender 类的完全限定名称。

`<appender>`元素可以包含零个或一个`<layout>`元素，零个或多个`<encoder>`元素以及零个或多个`<filter>`元素。

下图说明了常见的结构：

![appender元素](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/08/20220816141417-ab1605.png?x-oss-process=style/yuantu_shuiyin)

重要：在 logback 中，输出目标称为 appender，addAppender 方法将 appender 添加到给定的记录器 logger。  
给定记录器的每个启用的日志记录请求都将转发到该记录器中的所有 appender 以及层次结构中较高的 appender。
换句话说，appender 是从记录器层次结构中附加地继承的。

例如，如果将控制台 appender 添加到根记录器，则所有启用的日志记录请求将至少在控制台上打印。

如果另外将文件追加器添加到记录器（例如L），则对 L 和 L 的子项启用的记录请求将打印在文件和控制台上。  
通过将记录器的 additivity 标志设置为 false，可以覆盖此默认行为，以便不再添加 appender 累积。

Appender 是一个接口，它有许多子接口和实现类，具体如下图所示：

![Appender](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/08/20220816143228-2ae38d.png?x-oss-process=style/yuantu_shuiyin)

其中最重要的两个Appender为：ConsoleAppender 、RollingFileAppender。


#### 3.4.1、ConsoleAppender

ConsoleAppender，如名称所示，将日志输出到控制台上。

#### 3.4.2、RollingFileAppender

RollingFileAppender，是 FileAppender 的一个子类，扩展了 FileAppender，具有翻转日志文件的功能。

例如，RollingFileAppender 可以记录到名为 log.txt 文件的文件，并且一旦满足某个条件，就将其日志记录目标更改为另一个文件。

有两个与 RollingFileAppender 交互的重要子组件。

- RollingPolicy：负责执行翻转所需的操作。
- TriggeringPolicy：将确定是否以及何时发生翻转。

因此，RollingPolicy 负责什么和 TriggeringPolicy 负责什么时候。

作为任何用途，RollingFileAppender 必须同时设置 RollingPolicy 和 TriggeringPolicy。  
但是，如果其 RollingPolicy 也实现了TriggeringPolicy 接口，则只需要显式指定前者。

#### 3.4.3、滚动策略

TimeBasedRollingPolicy：可能是最受欢迎的滚动策略。它根据时间定义翻转策略，例如按天或按月。   
TimeBasedRollingPolicy 承担滚动和触发所述翻转的责任。实际上，TimeBasedTriggeringPolicy 实现了 RollingPolicy 和 TriggeringPolicy 接口。

SizeAndTimeBasedRollingPolicy：有时您可能希望按日期归档文件，但同时限制每个日志文件的大小，特别是如果后处理工具对日志文件施加大小限制。  
为了满足此要求，logback 提供了 SizeAndTimeBasedRollingPolicy ，它是 TimeBasedRollingPolicy 的一个子类，实现了基于时间和日志文件大小的翻滚策略。

### 3.5、`<encoder>`元素

encoder 中最重要就是 pattern 属性，它负责控制输出日志的格式，这里给出一个我自己写的示例：

>  <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %highlight(%-5level) --- [%15.15(%thread)] %cyan(%-40.40(%logger{40})) : %msg%n</pattern>

![输出格式](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/08/20220816143827-d6b5b2.png?x-oss-process=style/yuantu_shuiyin)

```
%d{yyyy-MM-dd HH:mm:ss.SSS}：日期
%-5level：日志级别
%highlight()：颜色，info为蓝色，warn为浅红，error为加粗红，debug为黑色
%thread：打印日志的线程
%15.15():如果记录的线程字符长度小于15(第一个)则用空格在左侧补齐,如果字符长度大于15(第二个),则从开头开始截断多余的字符 
%logger：日志输出的类名
%-40.40()：如果记录的logger字符长度小于40(第一个)则用空格在右侧补齐,如果字符长度大于40(第二个),则从开头开始截断多余的字符
%cyan：颜色
%msg：日志输出内容
%n：换行符
```

### 3.6、`<filter>`元素

filter 中最重要的两个过滤器为：LevelFilter、ThresholdFilter。

LevelFilter 根据精确的级别匹配过滤事件。  
如果事件的级别等于配置的级别，则筛选器接受或拒绝该事件，具体取决于 onMatch 和 onMismatch 属性的配置。

例如下面配置将只打印 INFO 级别的日志，其余的全部禁止打印输出：

```
<configuration>
  <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <filter class="ch.qos.logback.classic.filter.LevelFilter">
      <level>INFO</level>
      <onMatch>ACCEPT</onMatch>
      <onMismatch>DENY</onMismatch>
    </filter>
    <encoder>
      <pattern>
        %-4relative [%thread] %-5level %logger{30} - %msg%n
      </pattern>
    </encoder>
  </appender>
  <root level="DEBUG">
    <appender-ref ref="CONSOLE" />
  </root>
</configuration>
```

ThresholdFilter 过滤低于指定阈值的事件。  
对于等于或高于阈值的事件，ThresholdFilter 将在调用其 decision方法时响应 NEUTRAL。

但是，将拒绝级别低于阈值的事件，例如下面的配置将拒绝所有低于 INFO 级别的日志，只输出 INFO 以及以上级别的日志：

```
<configuration>
  <appender name="CONSOLE"
    class="ch.qos.logback.core.ConsoleAppender">
    <!-- deny all events with a level below INFO, that is TRACE and DEBUG -->
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
      <level>INFO</level>
    </filter>
    <encoder>
      <pattern>
        %-4relative [%thread] %-5level %logger{30} - %msg%n
      </pattern>
    </encoder>
  </appender>
  <root level="DEBUG">
    <appender-ref ref="CONSOLE" />
  </root>
</configuration>
```

## 四、详细的logback-spring.xml示例

以上介绍了 xml 中重要的几个元素，下面将我配置的 xml 贴出来以供参考（实现了基于日期和大小翻滚的策略，以及经 INFO 和 ERROR 日志区分输出，还有规范日志输出格式等）

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
 
    <!-- appender是configuration的子节点，是负责写日志的组件。 -->
    <!-- ConsoleAppender：把日志输出到控制台 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <!-- 默认情况下，每个日志事件都会立即刷新到基础输出流。 这种默认方法更安全，因为如果应用程序在没有正确关闭appender的情况下退出，则日志事件不会丢失。
         但是，为了显着增加日志记录吞吐量，您可能希望将immediateFlush属性设置为false -->
        <!--<immediateFlush>true</immediateFlush>-->
        <encoder>
            <!-- %37():如果字符没有37个字符长度,则左侧用空格补齐 -->
            <!-- %-37():如果字符没有37个字符长度,则右侧用空格补齐 -->
            <!-- %15.15():如果记录的线程字符长度小于15(第一个)则用空格在左侧补齐,如果字符长度大于15(第二个),则从开头开始截断多余的字符 -->
            <!-- %-40.40():如果记录的logger字符长度小于40(第一个)则用空格在右侧补齐,如果字符长度大于40(第二个),则从开头开始截断多余的字符 -->
            <!-- %msg：日志打印详情 -->
            <!-- %n:换行符 -->
            <!-- %highlight():转换说明符以粗体红色显示其级别为ERROR的事件，红色为WARN，BLUE为INFO，以及其他级别的默认颜色。 -->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %highlight(%-5level) --- [%15.15(%thread)] %cyan(%-40.40(%logger{40})) : %msg%n</pattern>
            <!-- 控制台也要使用UTF-8，不要使用GBK，否则会中文乱码 -->
            <charset>UTF-8</charset>
        </encoder>
    </appender>
 
    <!-- info 日志-->
    <!-- RollingFileAppender：滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件 -->
    <!-- 以下的大概意思是：1.先按日期存日志，日期变了，将前一天的日志文件名重命名为XXX%日期%索引，新的日志仍然是project_info.log -->
    <!--             2.如果日期没有发生变化，但是当前日志的文件大小超过10MB时，对当前日志进行分割 重命名-->
    <appender name="info_log" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--日志文件路径和名称-->
        <File>logs/project_info.log</File>
        <!--是否追加到文件末尾,默认为true-->
        <append>true</append>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>DENY</onMatch><!-- 如果命中ERROR就禁止这条日志 -->
            <onMismatch>ACCEPT</onMismatch><!-- 如果没有命中就使用这条规则 -->
        </filter>
        <!--有两个与RollingFileAppender交互的重要子组件。 第一个RollingFileAppender子组件，即RollingPolicy:负责执行翻转所需的操作。
         RollingFileAppender的第二个子组件，即TriggeringPolicy:将确定是否以及何时发生翻转。 因此，RollingPolicy负责什么和TriggeringPolicy负责什么时候.
        作为任何用途，RollingFileAppender必须同时设置RollingPolicy和TriggeringPolicy,但是，如果其RollingPolicy也实现了TriggeringPolicy接口，则只需要显式指定前者。-->
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- 日志文件的名字会根据fileNamePattern的值，每隔一段时间改变一次 -->
            <!-- 文件名：logs/project_info.2017-12-05.0.log -->
            <!-- 注意：SizeAndTimeBasedRollingPolicy中 ％i和％d令牌都是强制性的，必须存在，要不会报错 -->
            <fileNamePattern>logs/project_info.%d.%i.log</fileNamePattern>
            <!-- 每产生一个日志文件，该日志文件的保存期限为30天, ps:maxHistory的单位是根据fileNamePattern中的翻转策略自动推算出来的,例如上面选用了yyyy-MM-dd,则单位为天
            如果上面选用了yyyy-MM,则单位为月,另外上面的单位默认为yyyy-MM-dd-->
            <maxHistory>30</maxHistory>
            <!-- 每个日志文件到10mb的时候开始切分，最多保留30天，但最大到20GB，哪怕没到30天也要删除多余的日志 -->
            <totalSizeCap>20GB</totalSizeCap>
            <!-- maxFileSize:这是活动文件的大小，默认值是10MB，测试时可改成5KB看效果 -->
            <maxFileSize>10MB</maxFileSize>
        </rollingPolicy>
        <!--编码器-->
        <encoder>
            <!-- pattern节点，用来设置日志的输入格式 ps:日志文件中没有设置颜色,否则颜色部分会有ESC[0:39em等乱码-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level --- [%15.15(%thread)] %-40.40(%logger{40}) : %msg%n</pattern>
            <!-- 记录日志的编码:此处设置字符集 - -->
            <charset>UTF-8</charset>
        </encoder>
    </appender>
 
    <!-- error 日志-->
    <!-- RollingFileAppender：滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件 -->
    <!-- 以下的大概意思是：1.先按日期存日志，日期变了，将前一天的日志文件名重命名为XXX%日期%索引，新的日志仍然是project_error.log -->
    <!--             2.如果日期没有发生变化，但是当前日志的文件大小超过10MB时，对当前日志进行分割 重命名-->
    <appender name="error_log" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--日志文件路径和名称-->
        <File>logs/project_error.log</File>
        <!--是否追加到文件末尾,默认为true-->
        <append>true</append>
        <!-- ThresholdFilter过滤低于指定阈值的事件。 对于等于或高于阈值的事件，ThresholdFilter将在调用其decision（）方法时响应NEUTRAL。 但是，将拒绝级别低于阈值的事件 -->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level><!-- 低于ERROR级别的日志（debug,info）将被拒绝，等于或者高于ERROR的级别将相应NEUTRAL -->
        </filter>
        <!--有两个与RollingFileAppender交互的重要子组件。 第一个RollingFileAppender子组件，即RollingPolicy:负责执行翻转所需的操作。
        RollingFileAppender的第二个子组件，即TriggeringPolicy:将确定是否以及何时发生翻转。 因此，RollingPolicy负责什么和TriggeringPolicy负责什么时候.
       作为任何用途，RollingFileAppender必须同时设置RollingPolicy和TriggeringPolicy,但是，如果其RollingPolicy也实现了TriggeringPolicy接口，则只需要显式指定前者。-->
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- 活动文件的名字会根据fileNamePattern的值，每隔一段时间改变一次 -->
            <!-- 文件名：logs/project_error.2017-12-05.0.log -->
            <!-- 注意：SizeAndTimeBasedRollingPolicy中 ％i和％d令牌都是强制性的，必须存在，要不会报错 -->
            <fileNamePattern>logs/project_error.%d.%i.log</fileNamePattern>
            <!-- 每产生一个日志文件，该日志文件的保存期限为30天, ps:maxHistory的单位是根据fileNamePattern中的翻转策略自动推算出来的,例如上面选用了yyyy-MM-dd,则单位为天
            如果上面选用了yyyy-MM,则单位为月,另外上面的单位默认为yyyy-MM-dd-->
            <maxHistory>30</maxHistory>
            <!-- 每个日志文件到10mb的时候开始切分，最多保留30天，但最大到20GB，哪怕没到30天也要删除多余的日志 -->
            <totalSizeCap>20GB</totalSizeCap>
            <!-- maxFileSize:这是活动文件的大小，默认值是10MB，测试时可改成5KB看效果 -->
            <maxFileSize>10MB</maxFileSize>
        </rollingPolicy>
        <!--编码器-->
        <encoder>
            <!-- pattern节点，用来设置日志的输入格式 ps:日志文件中没有设置颜色,否则颜色部分会有ESC[0:39em等乱码-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level --- [%15.15(%thread)] %-40.40(%logger{40}) : %msg%n</pattern>
            <!-- 记录日志的编码:此处设置字符集 - -->
            <charset>UTF-8</charset>
        </encoder>
    </appender>
 
    <!--给定记录器的每个启用的日志记录请求都将转发到该记录器中的所有appender以及层次结构中较高的appender（不用在意level值）。
    换句话说，appender是从记录器层次结构中附加地继承的。
    例如，如果将控制台appender添加到根记录器，则所有启用的日志记录请求将至少在控制台上打印。
    如果另外将文件追加器添加到记录器（例如L），则对L和L'子项启用的记录请求将打印在文件和控制台上。
    通过将记录器的additivity标志设置为false，可以覆盖此默认行为，以便不再添加appender累积-->
    <!-- configuration中最多允许一个root，别的logger如果没有设置级别则从父级别root继承 -->
    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
 
    <!-- 指定项目中某个包，当有日志操作行为时的日志记录级别 -->
    <!-- 级别依次为【从高到低】：FATAL > ERROR > WARN > INFO > DEBUG > TRACE  -->
    <logger name="com.sailing.springbootmybatis" level="INFO">
        <appender-ref ref="info_log" />
        <appender-ref ref="error_log" />
    </logger>
 
    <!-- 利用logback输入mybatis的sql日志，
    注意：如果不加 additivity="false" 则此logger会将输出转发到自身以及祖先的logger中，就会出现日志文件中sql重复打印-->
    <logger name="com.sailing.springbootmybatis.mapper" level="DEBUG" additivity="false">
        <appender-ref ref="info_log" />
        <appender-ref ref="error_log" />
    </logger>
 
    <!-- additivity=false代表禁止默认累计的行为，即com.atomikos中的日志只会记录到日志文件中，不会输出层次级别更高的任何appender-->
    <logger name="com.atomikos" level="INFO" additivity="false">
        <appender-ref ref="info_log" />
        <appender-ref ref="error_log" />
    </logger>
 
</configuration>
```

## 五、附加内容

错误 log 输出方式：

```
Object entry = new SomeObject(); 
logger.debug("The entry is " + entry);
```

上面看起来没什么问题，但是会存在构造消息参数的成本，即将 entry 转换成字符串相加。

并且无论是否记录消息，都是如此，即：那怕日志级别为 INFO，也会执行括号里面的操作，但是日志不会输出。

优化后的写法：首先对设置的日志级别进行了判断，如果为debug模式，才进行参数的构造，对第一种写法进行了改善。

```
if(logger.isDebugEnabled()) { 
    Object entry = new SomeObject(); 
    logger.debug("The entry is " + entry);
}
```


不过还有最好的写法，使用占位符：

```
Object entry = new SomeObject(); 
logger.debug("The entry is {}.", entry);
```

只有在评估是否记录之后，并且只有在决策是肯定的情况下，记录器实现才会格式化消息并将“{}”对替换为条目的字符串值。

换句话说，当禁用日志语句时，此表单不会产生参数构造的成本。

**logback 作者进行测试得出：第一种和第三种写法将产生完全相同的输出。  
但是，在禁用日志记录语句的情况下，第三个变体将比第一个变体优于至少30倍。**

如果有多个参数，写法如下：

> logger.debug("The new entry is {}. It replaces {}.", entry, oldEntry);

如果需要传递三个或更多参数，则还可以使用Object [] 变体：

```
Object[] paramArray = {newVal, below, above};
logger.debug("Value {} was inserted between {} and {}.", paramArray); 
```

记录日志的时候我们可能需要在文件中记录下异常的堆栈信息，经过测试，logger.error(e) 不会打印出堆栈信息，正确的写法是：

> logger.error("程序异常, 详细信息:{}", e.getLocalizedMessage() , e);


原文地址：
> white_ice - https://blog.csdn.net/white_ice/article/details/85065219

[logback-demo Github地址](https://github.com/rcbb-cc/fast-start-guide)