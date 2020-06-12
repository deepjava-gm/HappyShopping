
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


**<a name="目录">:notebook:目录(搜索请用ctrl+F)</a>**

- [一：B2C电商项目（第一天、项目搭建、Restful风格、拼音API、txMybatis、品牌增删改查、Swagger）](https://blog.csdn.net/qq_37883866/article/details/106416361)
- [二：B2C电商项目（第二天、项目整体框架的CRUD、跨域解决方案、搭建分布式文件存储FastDFS完成文件上传、搭建微服务网管Gateway解决跨域，微服务网关过滤器过滤器）](https://blog.csdn.net/qq_37883866/article/details/106434561)
- [三：B2C电商项目（第三天、网关限流、Bcrypt密码加密、Base64编码、JWT微服务鉴权）](https://blog.csdn.net/qq_37883866/article/details/106436937)
- [四：B2C电商项目（第四天、品牌与分类关联、根据ID查询商品、商品审核与上下架、商品删除与还原）](https://blog.csdn.net/qq_37883866/article/details/106459925)
- [五：B2C电商项目（第五天、网站首页高可用nginx+lua、Lua基本语法、nginx+lua+redis、OpenResty、nginx限流、canal同步数据解决方案解决首页广告数据缓存更新）](https://blog.csdn.net/qq_37883866/article/details/106471133)
- [六：B2C电商项目（第六天、Elasticsearch商品上架添加索引、商品下架删除索引库、商品搜索、多条件分页搜索、排序、高亮显示）](https://blog.csdn.net/qq_37883866/article/details/106519163)
- [七：B2C电商项目（第七天、SpringBoot整合thymeleaf实现页面静态化、搜索页面渲染、商品详情页、生成静态页）](https://blog.csdn.net/qq_37883866/article/details/106569341)
- [八：B2C电商项目（第八天、用户认证、单点登录、Oauth2认证、项目认证开发、认证服务对接网关、登录页、SpringSecurity 权限控制）](https://blog.csdn.net/qq_37883866/article/details/106586882)
- [九：B2C电商项目（第九天、购物车模块、订单服务对接oauth、微服务之间认证、登录跳转页面配置）](https://blog.csdn.net/qq_37883866/article/details/106640606)
- [十：B2C电商项目（第十天、收件地址管理、地址管理页面渲染、下单业务实现） ](https://blog.csdn.net/qq_37883866/article/details/106664874)
- [十一：B2C电商项目（第十一天、分布式事务解决方案、Seata实现分布式事务、消息队列实现分布式事务、订单服务和用户服务业务代码） ](https://blog.csdn.net/qq_37883866/article/details/106677754)



>>>>>>> origin/master
