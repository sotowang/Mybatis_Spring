# Mybatis从入门到精通

## Spring 集成Mybatis

* 添加一个简单页面 index.jsp

下面这个简单页面中为了使用 jstl ， 专门增加了显示服务器时间的功能。在 webapp 中新建
JSP 页面，文件名为 index. jsp ， 文件内容如下。

```jsp
<%@ page import="java.util.Date" %>
<%@ page language="java" contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF8">
    <title>Index</title>
</head>
<body>
<p>
    Hello Spring MVC!
</p>
<p>
    <%
        Date now = new Date();
    %>
    服务器时间：<fmt:formatDate value="<%=now%>" pattern="yyyy-MM-dd HH:mm:ss"/>
</p>
</body>
</html>
```

* 添加Spring MVC依赖

* 添加Spring XML配置文件

  在 src/main/resources 中新增 applicationContext.xml 文件，内容如下 。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd">

     <context:component-scan base-package="com.soto.spring.service">
     <context:exclude-filter type="annotation" 			           expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
    <bean id="dataSource" class="org.apache.ibatis.datasource.pooled.PooledDataSource">
        <property name="driver" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://localhost:3306/mybatis"></property>
        <property name="username" value="root"></property>
        <property name="password" value="123456"></property>
    </bean>
    
</beans>
```

代码中的第一个 component-scan 用于配置 Spring 自动扫描类，通过 base-package 属性来设置要扫描的包名。包名支持 Ant 通配符，包名中的女匹配 0 或者任意数量的字符，这里的配置可以匹配如tk.mybatis.web.service.impl 和 tk.mybatis.simple.service.impl这样的包。第二个 bean 配置了一个数据库连接池，使用了最基本的 4 项属性进行配置。

* 添加Spring MVC配置文件

在 src/main/resources 中新增 mybatis-sevlet.xml 文件，内容如下。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/mvc
       http://www.springframework.org/schema/mvc/spring-mvc.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <mvc:annotation-driven/>

    <mvc:resources mapping="/static/**" location="static/"/>

    <context:component-scan base-package="tk.mybatis.*.controller"/>

    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

</beans>
```

这是一个最简单的配置，各项配置简单说明如下。

```
mvc : annotation-driven 启用 Controller 注解支持 。
mvc:resources 配置了 一个简单的静态资源映射规则。
context : component - scan 扫描 controller 包下的类 。
InternalResourceViewResolver 将视图名映射为 URL 文件 。
```

* 配置web.xml	

  集成 Spring 和 Spring MVC 后，需要在 web.xml 中进行相应的配置。对于 Spring 来说，需
  要增加如下配置。

  ```xml
  <!--1.启动Spring的容器-->
      <context-param>
          <param-name>contextConfigLocation</param-name>
          <param-value>classpath:applicationContext_1.xml</param-value>
      </context-param>
      <listener>
          <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
      </listener>
  ```

  这个配置用于在 Web 容器启动时根据 contextConfigLocation 配置的路径读取 Spring的配置文件，然后启动 Spring。

  

  针对 Spring MVC，需要增加如下配置。

```xml
 <!--2.SpringMVC的前端控制器，拦截所有请求-->

    <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:mybatis-servlet.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

为了避免编码不一致，通常还需要增加如下的编码过滤器配置。

```xml
 <!--3.字符编码过滤器   放在所有过滤器之前-->
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceRequestEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
        <init-param>
            <param-name>forceResponseEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

* 增加一个简单的Controller示例

将 index. jsp 移动到 src/main/webapp/WEB-INF/jsp 目 录中。增加 tk.mybatis.web.controller 包，然后新建 IndexController 类 ， 该类代码如下 。

```java
@Controller
public class IndexController {
    
    
    @RequestMapping(value = {"","/index"})
    public ModelAndView dicts() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("now", new Date());
        return mv;
    }
}

```

再对 index.jsp 页面中的 body 部分做如下修改。

```jsp
<p>
    <%
        Date now = new Date();
    %>
    服务器时间：<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/>
</p>
```

经过 以上这么多步的操作后 ， 基本的 Spring 和 Spring MVC 就集成完了。重启 Tomcat 然后
访问地址 http ://localhost: 8080/mybatis-spri吨，浏览器就会显示如下内容。

Hello Spring MVC !
服务器时间： 2017-02-07 22 : “ : 58

* 集成Mybatis

需要注意 的是，在 9.1 节创建基本项目的时候就己经添加了 MyBatis 的依赖，这一节主要是介绍与 mybatis-spring 相关的内容。经过 9 .2 节的集成后，我们 己经准备了好了 一个 Spring 和SpringMVC 的基础环境，从这一节开始 ， 便可以按照如下步骤集成 MyBatis 了。

1. 在 pom.xml 中添加 mybatis-spring 侬赖

```xml
 <!--Mybatis整合Spring的适配包-->
    <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis-spring -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>1.3.1</version>
    </dependency>
```

2 . 配置 SqlSessionFactoryBean

在 MyBatis-Spring 中， SqlSessionFactoryBean 是用于创建 Sq l SessionFactory 的。在 Spring 配置文件 applicationContext.xml 中配置这个工厂类， 代码如下。

```xml
<!--==================================配置MyBatis整合================================-->
    <bean id="SqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--指定MyBatis全局配置文件位置-->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <property name="dataSource" ref="dataSource"/>
        <!--指定mybatis  mapper文件-->
        <property name="mapperLocations" value="classpath:mapper/*.xml"/>
        
        <property name="typeAliasesPackage" value="com.soto.**.model"/>

    </bean>

```

* configLocation ： 用于配置 mybatis 配置 XML 的路径 ， 除了数据源外， 对 MyBatis 的各种配直仍然可以通过这种方式进行，并且配置 MyBatis sett i ngs 时只能使用这种方式。上面配置的 mybatis-config.xml 位于 src/main/resources 目录下 ， 配置文件 内容如下 。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <setting name="logImpl" value="LOG4J"/>
        <setting name="cacheEnabled" value="true"/>
        <setting name="aggressiveLazyLoading" value="false"/>
    </settings>

    <typeAliases>
        <package name="com.soto.spring.bean"/>
    </typeAliases>

    <plugins>
        <plugin interceptor="com.github.pagehelper.PageInterceptor">
            <property name="reasonable" value="true"></property>
        </plugin>
    </plugins>
</configuration>
```

* dataSource ：用于配置数据源，该属性为必选项，必须通过这个属性配置数据源 ，这里使用了上一节中配置好的 dataSource 数据库连接池 。
* mapper Locations ： 配置 SqlSessionFactoryBean 扫描 XML 映射文件的路径，可以使用 Ant 风格的路径进行配置。

* typeAliasesPackage ： 配置包中类的别名，配置后，包中的类在 XML 映射文件中使用时可以省略包名部分 ,直接使用类名。这个配置不支持 Ant 风格的路径，当 需要配置多个包路径时可以使用分号或逗号进行分隔。
* 除了上面几个常用 的属性外， SqlSessionFactoryBean 还有很多其他可以配置的属性，如果需要用到这些属性，可以直接查看 SqlSessionFactoryBean 的源码来了解每项属性及配置用法 。

3. 配置 MapperScannerConfigurer

  在以往和 Spring 集成的项目中，可能会有许多直接使用 SqlSession 的代码，或者使用了 Mapper 接口但是需要自己实现接口的用法。这些用法在 iBATIS 时期或者比较老的 MyBatis 项目中存在。这里要介绍的用法是最简单且推荐使用的一种，通过 MapperScannerConfigurer类自动扫描所有的 Mapper 接口，使用时可以直接注入接口 。

  在 Spring 配置文件 applicationContext.xml 中配置扫描类，代码如下 。

```xml
<!--配置扫描器  将mybatis接口实现加入到ioc容器中-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!--扫描所有dao接口的实现,加入到ioc容器中-->
        <property name="basePackage" value="com.soto.spring.dao"/>
    </bean>
```

MapperScannerConf igurer 中常配置以下两个属性 。

base Package ： 用于配置基本的包路径。可以使用分号或逗号作为分隔符设置多于一个的包路径，每个映射器将会在指定的包路径中递归地被搜索到 。

annotationClass ： 用于过滤被扫描的接口，如果设置了该属性，那么 MyBatis 的接口只有包含该注解才会被扫描进去。

## 几个简单的实例

### 基本准备

* 新建SQL表

```sql
CREATE TABLE mybatis.sys_dict (
	id bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
	code varchar(64) NOT NULL COMMENT '类别',
	name varchar(64) NOT NULL COMMENT '字典名',
	value varchar(64) NOT NULL COMMENT '字典值',
	PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci ;
```

```sql
INSERT INTO mybatis.sys_dict
(id, code, name, value)
VALUES(1, '性别', '男', '男');
INSERT INTO mybatis.sys_dict
(id, code, name, value)
VALUES(2, '性别', '女', '女');
INSERT INTO mybatis.sys_dict
(id, code, name, value)
VALUES(3, '季度', '第一季度', '1');
INSERT INTO mybatis.sys_dict
(id, code, name, value)
VALUES(4, '季度', '第二季度', '2');
INSERT INTO mybatis.sys_dict
(id, code, name, value)
VALUES(5, '季度', '第三季度', '3');
INSERT INTO mybatis.sys_dict
(id, code, name, value)
VALUES(6, '季度', '第四季度', '4');

```

在 src/main/iava 中新建 com.soto.spring.model 包 ， 然后新建 SysDict 实体类。

```java
public class SysDict implements Serializable {

    private Long id;
    private String code;
    private String name;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
```

### 开发Mapper层（DAO层）

Mappe r 层也就是常说的数据访 问 层（ Dao 层）。使用 Mapper 接口和 XML 映射文件结合的方式进行开发 ， 在 9.3 节集成 MyBatis 的配置中，自动扫描接口的包名为 tk.mybatis.mapper ，因此在创建 Mapper 接口所在的包时也要参照这个命名规则 。在 src/main/java 中

新建 tk.mybatis.web.mapper 包，然后新建 DictMapper 接口。

```java
public interface DictMapper {
    SysDict selectByPrimaryKey(Long id);

    List<SysDict> selectBySysDict(SysDict sysDict, RowBounds rowBounds);

    int insert(SysDict sysDict);

    int updateById(SysDict sysDict);

    int deleteById(Long id);
    
}
```

同时， 9.3 节的 SqlSessionFactoryBean 中也配置了扫描 XML 映射文件的目录classpath:tk/mybatis/**/mapper /* .xml 。

在 src/main/resources 中新建 tk/mybatis/web/mapper/ 目录，然后新建 DictMapper.xml 文件 。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.soto.spring.dao.DictMapper">

    <select id="selectByPrimaryKey" resultType="SysDict">
      select id,code,name,`value` from  sys_dict where id = #{id}
    </select>

    <select id="selectBySysDict" resultType="SysDict">
        select * from sys_dict
        <where>
            <if test="id !=null">
            and id = #{id};
            </if>

            <if test="code != null and code != ''">
              and code = #{code}
            </if>
        </where>
        order by code,`value`
    </select>

    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        insert into sys_dict (code, name, `value`)
        values (#{code},#{name},#{value });
    </insert>

    <update id="updateById">
        update sys_dict
        set code = #{code},
        name = #{name},
        value = #{value}
        where id = #{id}

    </update>

    <delete id="deleteById">
        delete from sys_dict where id = #{id}
    </delete>
</mapper>
```

因为 9.3 节配置 SqlSessionFactoryBean 时，将 typeAliasesPackage 配置为com .isea5 33 . mybatis.model ，所以这里设置 resultType 时可以直接使用类名，省略包名 。以下代码依次是根据字典参数和分页参数查询字典信息的方法，新增、 更新 、删除字典的接口的方法。

### 开发业务层（Service层）

```java
@Service
public class DictServiceImpl  implements DictService {

    @Autowired
    private DictMapper dictMapper;


    @Override
    public SysDict findById(@NotNull Long id) {
        return dictMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysDict> findBySysDict(SysDict sysDict, Integer offset, Integer limit) {
        RowBounds rowBounds = RowBounds.DEFAULT;
        if (offset != null && limit != null) {
            rowBounds = new RowBounds(offset, limit);
        }
        return dictMapper.selectBySysDict(sysDict, rowBounds);
    }

    @Override
    public boolean saveOrUpdate(SysDict sysDict) {
        if (sysDict.getId() == null) {
            return dictMapper.insert(sysDict) == 1;
        }
        return dictMapper.updateById(sysDict) == 1;
    }
    
    @Override
    public boolean deleteById(Long id) {
        return dictMapper.deleteById(id) == 1;
    }
    
}

```

 Service 的实现类中需要添加＠ Service 注解，在 9.2 节集成 Spring 时配置过自动扫描包，包名是 tk.mybatis . web. se r vice . impl, DictServiceimpl 实现类所在的包就是符合这个包名规则的，加上注解后 ， Spring 在初始化时就会扫描到这个类，然后由 Spring 管理这个类。因为配置了自动扫描 Mapper 接口，所以在 Service 层可以直接通过以下代码注入 Mapper。

```java
@Autowired
private  DIctMapper dictMapper ;
```

通过自动扫描 Mapper 和 自动注入可以更方便地使用 MyBatis 。  

### 开发控制层（Controller层）

```java
@Controller
@RequestMapping("/dicts")
public class DictController {

    @Autowired
    private DictService dictService;

    @RequestMapping
    public ModelAndView dicts(SysDict sysDict, Integer offset, Integer limit) {
        ModelAndView mv = new ModelAndView("dicts");

        List<SysDict> dicts = dictService.findBySysDict(sysDict, offset, limit);
        mv.addObject("dicts", dicts);
        return mv;
    }

    /**
     * 新增或修改字典信息页面，使用get中转到页面
     * @param id
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView add(Long id) {
        ModelAndView mv = new ModelAndView("dict_add");
        SysDict sysDict;
        if (id == null) {
            //如果id不存在，新增数据
            sysDict = new SysDict();
        } else {
            sysDict = dictService.findById(id);
        }
        mv.addObject("model", sysDict);
        return mv;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView save(SysDict sysDict) {
        ModelAndView mv = new ModelAndView();
        try {
            dictService.saveOrUpdate(sysDict);
            mv.setViewName("redirect:/dicts");
        } catch (Exception e) {
            mv.setViewName("dict_add");
            mv.addObject("msg", e.getMessage());
            mv.addObject("model", sysDict);
        }
        return mv;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap delete(@RequestParam Long id) {
        ModelMap modelMap = new ModelMap();
        try {
            boolean succccess = dictService.deleteById(id);
            modelMap.put("success", succccess);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("msg", e.getMessage());
        }
        return modelMap;
    }
    
    
    
}
```

