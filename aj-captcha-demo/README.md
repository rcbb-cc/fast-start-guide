---
title: 行为验证码(AJ-Captcha快速入门)
date: 2022-08-05 10:51:45
permalink: /pages/d57d55/
categories:
- 文章
- Spring
  tags:
- 行为验证码
---

## 行为验证码

行为验证码采用嵌入式集成方式，接入方便，安全，高效。  
抛弃了传统字符型验证码展示-填写字符-比对答案的流程，采用验证码展示-采集用户行为-分析用户行为流程，用户只需要产生指定的行为轨迹，不需要键盘手动输入，极大优化了传统验证码用户体验不佳的问题。  
同时，快速、准确的返回人机判定结果。

![滑动拼图](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/07/blockPuzzle-d35b55.png?x-oss-process=style/yuantu_shuiyin)

<!-- more -->


![点选文字](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/07/clickWord-b61690.png?x-oss-process=style/yuantu_shuiyin)

## AJ-Captcha

anji-plus / AJ-Captcha

Githu地址：
https://github.com/anji-plus/captcha

码云地址：
https://gitee.com/anji-plus/captcha

文档地址：
https://captcha.anji-plus.com/#/doc

## 最佳实践

环境：SpringBoot 2.3.7.RELEASE、Maven

~~~xml
<dependency>
    <groupId>com.github.anji-plus</groupId>
    <artifactId>captcha-spring-boot-starter</artifactId>
    <version>1.2.7</version>
</dependency>
~~~

引入的依赖中，存在 CaptchaController，有默认的实现方式，如果不自定义相关内容，基本不用编写代码。

![CaptchaController](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/07/20210720111739-94e00e.png?x-oss-process=style/yuantu_shuiyin)

默认的效果。

![captcha效果](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/07/20210720112137-fd80ca.png?x-oss-process=style/yuantu_shuiyin)

官方的前端 html 代码我也 copy 了一份过来，放在项目下了，方便自己验证。

![captcha html](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/07/20210720112258-728401.png?x-oss-process=style/yuantu_shuiyin)

官方前端 Demo 代码，是非常全的。前端代码使用的什么框架，直接看官方 Demo 就行了。

![官方前端 Demo 代码](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/07/20210720112420-34f66a.png?x-oss-process=style/yuantu_shuiyin)

## 修改图片资源

修改验证码的图片资源。

**注意目录结构**（因为看源码可发现，在初始化图片时，它在硬编码上固定了路径地址）  
**注意图片大小**（这个与前端展示的大小有关，默认的图片大小为：宽度：311 像素，高度：155 像素）

![固定了路径地址](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/07/20210720113235-daed3f.png?x-oss-process=style/yuantu_shuiyin)

使用项目路径，加个水印。

```yaml
spring:
  application:
    name: aj-captcha-demo
server:
  port: 9696

aj:
  captcha:
    # 支持全路径
#    jigsaw: C:/Users/Desktop/images/jigsaw
#    pic-click: C:/Users/Desktop/images/pic-click
    # 支持项目路径以classpath:开头,取resource目录下路径,例：classpath:images/jigsaw
    jigsaw: classpath:images/jigsaw
    pic-click: classpath:images/pic-click
    # 水印
    water-mark: www.rcbb.cc
```

![修改图片](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/07/20210720112741-468f7d.png?x-oss-process=style/yuantu_shuiyin)

自定义图片和水印的效果。

![效果](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2021/07/20210720114021-acfa70.png?x-oss-process=style/yuantu_shuiyin)

源代码地址：
> https://github.com/rcbb-cc/fast-start-guide