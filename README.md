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

















​              