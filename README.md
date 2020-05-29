
:sparkling_heart:HappyShopping（B2C电商项目）

<p align="center">
  <a href="#目录"><img src="https://img.shields.io/badge/目录(善用搜索)-read-brightgreen.svg" alt="阅读"></a>
</p>


**<a name="目录">:notebook:目录(搜索请用ctrl+F)</a>**


# HappyShopping
# 电商项目

## 项目介绍
 本项目支持用户在线浏览商品，在线搜索商品，并且可以将喜欢的商品加入购物车从而下单购买商品，同时支持线上支付，支付模式支持支付宝、微信。用户还可以参与低价商品秒杀。
 
## 项目架构
本项目采用了微服务架构，微服务技术采用了SpringCloud技术栈，各个微服务站点基于SpringBoot构建，并采用SpringCloud Gateway将各个微服务的功能串联起来，形成一整套系统，同时在微服务网关Gateway中采用过滤和限流策略，实施对微服务进行保护和权限认证操作。项目采用了SpringSecurity OAuth2.0解决了各个微服务之间的单点登录和用户授权。采用了当前非常热门的Seata来解决微服务与微服务之间的分布式事务。采用了Elasticsearch解决了海量商品的实时检索。数据存储采用了MySQL，并结合Canal实现数据同步操作，利用Redis做数据缓存操作。各个微服务之间采用RabbitMQ实现异步通信。我们采用了OpenResty集成的Nginx来控制微服务最外层的大量并发，利用Keepalived+Nginx来解决Nginx单点故障问题。

- [第一天、项目搭建、Restful风格、拼音API、txMybatis、品牌增删改查、Swagger](https://blog.csdn.net/qq_37883866/article/details/106416361)


