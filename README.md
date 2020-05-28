# HappyShopping
# 电商项目

## 项目介绍
 本项目支持用户在线浏览商品，在线搜索商品，并且可以将喜欢的商品加入购物车从而下单购买商品，同时支持线上支付，支付模式支持支付宝、微信。用户还可以参与低价商品秒杀。
 
## 项目架构
本项目采用了微服务架构，微服务技术采用了SpringCloud技术栈，各个微服务站点基于SpringBoot构建，并采用SpringCloud Gateway将各个微服务的功能串联起来，形成一整套系统，同时在微服务网关Gateway中采用过滤和限流策略，实施对微服务进行保护和权限认证操作。项目采用了SpringSecurity OAuth2.0解决了各个微服务之间的单点登录和用户授权。采用了当前非常热门的Seata来解决微服务与微服务之间的分布式事务。采用了Elasticsearch解决了海量商品的实时检索。数据存储采用了MySQL，并结合Canal实现数据同步操作，利用Redis做数据缓存操作。各个微服务之间采用RabbitMQ实现异步通信。我们采用了OpenResty集成的Nginx来控制微服务最外层的大量并发，利用Keepalived+Nginx来解决Nginx单点故障问题。


# 第一天
## 一、项目架构
### 技术架构
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200529001716758.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM3ODgzODY2,size_16,color_FFFFFF,t_70)
### 系统架构图
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200529001755831.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM3ODgzODY2,size_16,color_FFFFFF,t_70)

### 工程结构关系图

![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-gRuKejdB-1590682056675)(img/工程结构.png)\]](https://img-blog.csdnimg.cn/20200529000930882.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM3ODgzODY2,size_16,color_FFFFFF,t_70)
### IDEA项目结构图
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200529001919419.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM3ODgzODY2,size_16,color_FFFFFF,t_70)
**结构说明：**
- changgou_gateway
网关模块，根据网站的规模和需要，可以将综合逻辑相关的服务用网关路由组合到一起。在这里还可以做鉴权和限流相关操作。

- changgou_service
微服务模块，该模块用于存放所有独立的微服务工程。

- changgou_service_api
对应工程的 JavaBean、Feign、以及Hystrix配置，该工程主要对外提供依赖。

- changgou_transaction_fescar
分布式事务模块，将分布式事务抽取到该工程中，任何工程如需要使用分布式事务，只需依赖该工程即可。

- changgou_web
web 服务工程，对应功能模块如需要调用多个微服务，可以将他们写入到该模块中，例如网站
后台、网站前台等


#### 1.1、父工程

>  进行工程聚合，把通用依赖配置好

a、创建maven工程

b、配置pom里的依赖

#### 1.2、定义二级父工程（pom）
a、changgou_service:下面放的是提供各个服务的微服务--服务提供者

b、changgou_service_api:服务提供者相对应的实体类和feign接口，一个服务提供者对应于一个api

b、changgou_web:提供视图（页面）的微服务

c、changou_gateway：商家后台网关，消费者前台网关

#### 1.3、注册中心

a、创建注册中心模块

b、引入坐标

c、创建引导类，定义启用注册中心注解

d、application.yml配置文件配置

#### 1.4、公共模块定义

changgou_common:通用代码

changgou_common_db：数据库通用配置，changgou_common_db依赖changgou_common

#### 1.5、构建商品微服务

a、在changgou_service下创建changgou_service_goods

b、在changgou_service_api下创建changgou_service_goods_api

c、changgou_service_goods要通过坐标依赖changgou_service_goods_api

d、定义changgou_service_goods配置文件application.yml

e、创建changgou_service_goods启动引导类


#### 1.6、接口定义
接口定义包含4个点：

1、接口请求的url--http://129.87.57.91/sendMessage

2、请求方式post、get、delete。。。

3、入参（传给后台的参数） phone：110

4、出参（后端向前端响应的参数结果） code：200


## 二、前后端分离规范Restful风格

```
/**
 * 1、 请求的URL， 遵从Restful风格,不建议url里有动词 put /add/user
 * 2、 请求的方式：
 *     * 查询 get
 *     * 删除 delete
 *     * 修改 put
 *     * 新增 post
 *
 * 3、方法的返回值
 *     * 要求：无论是后端提供的 CRUD的所有的接口， 必须要 给前端提示信息【Result】Class（是否成功、状态码、提示信息、扩展Object）
 *     * 新增、删除、修改  Result
 *     * 查询
 *          * 不带分页 List，   封装一个 包装类（提示信息对象和List）
 *          * 带分页（总记录数和当前页的List集合  ---> PageResult）  Result
 *     * 统一要求是 JSON数据 {}  : @ResponseBody  调用 response对象中的 write方法，并且是在 context-type
 * 4、方法参数
 *     * 查询 get
 *          * url/{id} 或者是 url?id=1
 *          * 带条件查询
 *     * 删除 delete
 *          * url/{id} 或者是 url?id=1
 *     * 修改 put
 *          * JSON 数据 { }    @RequestBody  JSON --》 JAVA Bean
 *          * id   @PathVariable
 *     * 新增 post
 *          * JSON 数据 { }    @RequestBody  JSON --》 JAVA Bean
 *          * XXXDto
 * 5、方法名称（可选）
 *     * 查询 get
 *          * find/get/select XXX
 *            如：findById  findAll findPage
 *     * 删除 delete
 *          * deleteXXX
 *     * 修改 put
 *          * update/edit
 *     * 新增 post
 *          * add/insert/save
 */
```

如何定义前后端分离接口？
1. 请求的url 
2. 请求的方式（操作）
3. 返回的数据格式
4. 参数的格式
5. （前端无关）方法的名称


## 三、 汉语转拼音 pinyin4j  API
品牌管理中首字母添加问题，我们可以使用 pinyin4j 来将汉字转拼音
（1） 在common工程中添加依赖
```xml
<!--pinyin4j -->
<dependency>
    <groupId>com.belerweb</groupId>
    <artifactId>pinyin4j</artifactId>
    <version>2.5.0</version>
</dependency>
```
（2） 编写工具类

```java
package com.changgou.common.util;

/**
 * @Description:拼音工具类
 * @Version: V1.0
 */

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtils {

    /**
     * 将文字转为汉语拼音
     *
     * @param chineselanguage 要转成拼音的中文
     */
    public String toHanyuPinyin(String chineselanguage) {
        char[] cl_chars = chineselanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部小写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        try {
            for (int i = 0; i < cl_chars.length; i++) {
                if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
                } else {// 如果字符不是中文,则不转换
                    hanyupinyin += cl_chars[i];
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin;
    }

    /**
     * 汉字的首字母 转成大写
     * @param ChineseLanguage
     * @return
     */
    public static String getFirstLettersUp(String ChineseLanguage) {
        return getFirstLetters(ChineseLanguage, HanyuPinyinCaseType.UPPERCASE);
    }

    public static String getFirstLettersLo(String ChineseLanguage) {
        return getFirstLetters(ChineseLanguage, HanyuPinyinCaseType.LOWERCASE);
    }

    public static String getFirstLetters(String ChineseLanguage, HanyuPinyinCaseType caseType) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(caseType);// 输出拼音全部大写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        try {
            for (int i = 0; i < cl_chars.length; i++) {
                String str = String.valueOf(cl_chars[i]);
                if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0].substring(0, 1);
                } else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
                    hanyupinyin += cl_chars[i];
                } else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母
                    hanyupinyin += cl_chars[i];
                } else {// 否则不转换
                    hanyupinyin += cl_chars[i];//如果是标点符号的话，带着
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin;
    }

    public static String getPinyinString(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部大写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        try {
            for (int i = 0; i < cl_chars.length; i++) {
                String str = String.valueOf(cl_chars[i]);
                if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
                } else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
                    hanyupinyin += cl_chars[i];
                } else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母

                    hanyupinyin += cl_chars[i];
                } else {// 否则不转换
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin;
    }

    /**
     * 取第一个汉字的第一个字符
     *
     * @return String
     * @throws
     * @Title: getFirstLetter
     * @Description: TODO
     */
    public static String getFirstLetter(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 输出拼音全部大写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        try {
            String str = String.valueOf(cl_chars[0]);
            if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(cl_chars[0], defaultFormat)[0].substring(0, 1);
                ;
            } else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
                hanyupinyin += cl_chars[0];
            } else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母

                hanyupinyin += cl_chars[0];
            } else {// 否则不转换

            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin;
    }

    public static void main(String[] args) {
        PinYinUtils pinYinUtils = new PinYinUtils();
        System.out.println(pinYinUtils.toHanyuPinyin("黑马程序员"));
//        System.out.println(  PinYinUtils.getFirstLetter("三星") );
        System.out.println(  PinYinUtils.getFirstLetters("三星",HanyuPinyinCaseType.UPPERCASE) );
    }
}
```

（3） 修改品牌管理添加方法逻辑

```java
/**
 * 品牌新增
 * @param brand
 */
@Override
@Transactional
public void add(Brand brand) {

    String name = brand.getName();
    if (StringUtils.isBlank(name)) {
        throw new RuntimeException("参数非法");
    }

    // 1 判断 填写 品牌首字母
    String letter = brand.getLetter();
    if (StringUtils.isBlank(letter)) {  // 没传参数
        letter = PinYinUtils.getFirstLetter(name);  //  name ? null
    } else {
        // 转成大小存储到数据库
       letter = letter.toUpperCase();
    }
    brand.setLetter(letter);

    brandMapper.insertSelective(brand);
}
```

## 四、txMybatis通用Mapper API
#### 4.1、第一步添加相应注解
- 在实体类上添加 @Table(name = "tb_brand") 注解
- 在主键字段上添加 @Id 注解
@Table和@Id都是JPA注解，@Table用于配置表与实体类的映射关系，@Id用于标识主键属性

#### 4.2、第二步创建 **Mapper接口继承Mapper<实体类> 接口
注意包别导错了
例：
```java
import com.changgou.goods.pojo.Brand;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {
}
```
继承了Mapper接口，就自动实现了增删改查的常用方法。

#### 4.3、在服务实现类导入**Mapper接口 实例
例：

```java
package com.changgou.goods.service.impl;

import com.changgou.common.util.PinYinUtils;
import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-05-28 19:40
 */

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;


    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Brand> findAll() {

        return brandMapper.selectAll();
    }


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增品牌
     *
     * @param brand
     */
    @Override
    public void add(Brand brand) {


        String name = brand.getName();
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("参数非法");
        }

        // 1 判断 填写 品牌首字母
        String letter = brand.getLetter();
        if (StringUtils.isBlank(letter)) {  // 没传参数
            letter = PinYinUtils.getFirstLetter(name);  //  name ? null
        } else {
            // 转成大小存储到数据库
            letter = letter.toUpperCase();
        }
        brand.setLetter(letter);


        brandMapper.insertSelective(brand);
    }

    /**
     * 修改品牌
     *
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    /**
     * 删除品牌
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);

    }

    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    @Override
    public List<Brand> findList(Map<String, Object> searchMap) {

        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
//            品牌名称
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }

//            品牌的首字母
            if (searchMap.get("letter") != null && !"".equals(searchMap.get("letter"))) {
                criteria.andEqualTo("letter", searchMap.get("letter"));
            }

        }

        return brandMapper.selectByExample(example);
    }


    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Brand> findPage(int page, int size) {

        PageHelper.startPage(page, size);

        return (Page<Brand>) brandMapper.selectAll();
    }

    /**
     * 多条件分页查询
     *
     * @param searchMap 查询条件
     * @param page      页码
     * @param size      页的大小
     * @return 分页结果
     */
    @Override
    public Page<Brand> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        if (searchMap != null) {
//           品牌名称
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }

//           品牌首字母
            if (searchMap.get("letter") != null && !"".equals(searchMap.get("letter"))) {
                criteria.andEqualTo("letter", searchMap.get("letter"));
            }
        }

        return (Page<Brand>) brandMapper.selectByExample(example);
    }


}

```

### 注意updateByPrimaryKeySelective() 与updateByPrimaryKey()方法的区别
- updateByPrimaryKeySelective() 接收的参数为对应于数据库的实体类对象，利用字段的自动匹配进行更新表的操作，如果传入obj对象中的某个属性值为null，则不进行数据库对应字段的更新。
- updateByPrimaryKey() 与updateByPrimaryKeySelective的区别在于，如果传入的obj对象中某个属性值为null，会将对应的数据库字段赋值为null。
### insertSelective()与insertSelective()区别
insertSelective--有选择性的保存数据 
比如User里面有三个字段:id name age password
 
```java
User u=new user();
u.setName("bill");
mapper.insertSelective(u);
 
insertSelective执行对应的sql语句的时候，只插入对应的name字段
sql语句如下：
insert into tb_user (name) value ("bill")
insert则是每个字段都要添加一遍
insert into tb_user (id,name,age,password) value(null,"bill",null,null);
```

#### 4.4、pagehelper-spring-boot-starter   SpringBoot集成的pagehelper分页插件的使用

**在yml文件内配置pageHelper**

```yml
#分页插件
pagehelper:
  helper-dialect: mysql   # mysql数据库
  reasonable: true   # 负数页码显示第一页  超出最大页码显示最后一页
  support-methods-arguments: true
  params: count=countSql
```


**1、导入坐标**
```xml
 <!--mybatis分页插件-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.2.3</version>
        </dependency>
```
**2、Controller层代码**

```java
@GetMapping(value = "/search/{page}/{size}")
  @ApiOperation("分页")
  public Result findPage(@PathVariable("page") int page, @PathVariable("size") int size){
      Page<Brand> pageList = brandService.findPage(page, size);
      PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
      return new Result(true,StatusCode.OK,"查询成功",pageResult);
  }

```
**3、服务实现类代码**

```java
 /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Brand> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Brand>) brandMapper.selectAll();
    }
```




## 五、前后端分离组件 Swagger
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200529001651297.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM3ODgzODY2,size_16,color_FFFFFF,t_70)

+ 1、在changgou_service_api工程的pom文件里引入swagger坐标

```xml
			 <!--swagger-->
       <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>
```

+ 2、在changgou_service_goods工程定义swagger配置类

```java
package com.changgou.service.goods.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.changgou"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("畅购商城API文档")
                .description("畅购商城API文档-商品管理")
                .version("1.0")
                .build();
    }

}
```

+ 3、在com.changgou.service.goods.controller.BrandController中添加swagger注解

```java
package com.changgou.goods.controller;

import com.changgou.common.pojo.PageResult;
import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.Page;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.net.SocketFlow;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-05-28 19:24
 * Brand
 */
@RestController
@RequestMapping("/brand")
@Api(value = "品牌管理",tags = "品牌管理" )
public class BrandController {

    @Autowired
    private BrandService brandService;


    /**
     * 查询所有
     *
     * @return
     */
    @ApiOperation("查询所有的品牌")
    @GetMapping
    public Result findAll() {

        List<Brand> brandList = brandService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", brandList);

    }


    /**
     * 根据id查询品牌数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "主键",paramType = "path",required=true,dataType="int",allowEmptyValue=false)
    })
    public Result findById(@PathVariable Integer id) {
        Brand brand = brandService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", brand);
    }


    /**
     * 添加
     *
     * @param brand
     * @return
     */
    @PostMapping
    @ApiOperation("添加品牌")
    public Result findById(@RequestBody Brand brand) {
        brandService.add(brand);
        return new Result(true, StatusCode.OK, "添加成功");
    }


    /**
     * 修改品牌数据
     * @param brand
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    @ApiOperation("修改品牌")
    public Result update(@RequestBody Brand brand,@PathVariable Integer id){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true, StatusCode.OK,"修改成功");
    }


    /**
     * 根据id删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation("根据id删除品牌数据")
    public Result delete(@PathVariable Integer id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }


    /**
     * 多条件搜索品牌数据
     * @param searchMap  品牌名称 品牌首字母
     * @return
     */
    @GetMapping(value = "/search")
    @ApiOperation("多条件搜索品牌数据")
    public Result findList(@RequestParam Map searchMap){
        List<Brand> list = brandService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    /**
     * 分页
     * @param page
     * @param size
     * @return
     */
  @GetMapping(value = "/search/{page}/{size}")
  @ApiOperation("分页")
  public Result findPage(@PathVariable("page") int page, @PathVariable("size") int size){
      Page<Brand> pageList = brandService.findPage(page, size);
      PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
      return new Result(true,StatusCode.OK,"查询成功",pageResult);
  }


    /**
     * 多条件分页查询
     * @param page
     * @param size
     * @return
     */
  @GetMapping(value = "/searchPage/{page}/{size}")
  @ApiOperation("多条件分页查询")
  public Result findPage(@RequestParam Map searchMap ,@PathVariable("page") int page, @PathVariable("size") int size){
      Page<Brand> pageList = brandService.findPage(searchMap,page, size);
      PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
      return new Result(true,StatusCode.OK,"查询成功",pageResult);
  }
}

```

+ 4、定义com.changgou.goods.pojo.Brand实体的swagger注解

```java
package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "tb_brand")
@ApiModel("品牌实体")
public class Brand implements Serializable {
    @Id
    @ApiModelProperty("品牌id")
    private Integer id;//品牌id
    private String name;//品牌名称
    private String image;//品牌图片地址
    private String letter;//品牌的首字母
    private Integer seq;//排序

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}

```

 5.访问swagger页面

启动goods服务，访问 http://localhost:9011/swagger-ui.html

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200529001510133.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM3ODgzODY2,size_16,color_FFFFFF,t_70)
## 六、定义全局异常处理类
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200529004638648.png)

```java
package com.changgou.goods.handler;

import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }
}

```

