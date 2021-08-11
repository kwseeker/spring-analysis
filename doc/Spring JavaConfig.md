# Spring JavaConfig

[JavaConfig Reference Guide](https://docs.spring.io/spring-javaconfig/docs/1.0.0.M4/reference/htmlsingle/spring-javaconfig-reference.html)

## 1 介绍

JavaConfig(Spring Java Configuration) 提供了一种纯Java配置Spring IoC容器的方式（得益于Java 5 引入的注解和泛型）。

JavaConfig的优势：

+ 为依赖注入提供面向对象机制，使得配置代码可以复用、继承、实现多态。
+ 可以完全控制实例化和依赖注入。
+ 因为只需要 Java，可以使用完全可重构的配置逻辑，在 IDE 之外不需要任何特殊工具。

JavaConfig 支持拓展和定制。

源码下载编译测试：

```shell
svn co https://src.springframework.org/svn/spring-javaconfig/trunk/ spring-javaconfig
cd spring-javaconfig/org.springframework.config.java
ant clean test
# 代码导入到IDE 略
```

## 2 @Configuration 配置类编写与使用

### 2.1 @Configuration

＠Configuration注解的类，作为Bean定义源文件。由@Bean 注释的方法组成，这些方法定义了将由 Spring IoC 容器管理的对象的实例化、配置和初始化逻辑。

一个应用中可以使用一个或多个@Configuration注解的类。

> @Configuration 可以被认为等同于 XML 的 <beans/> 元素。

### 2.2 @Bean

@Bean 是一个方法级的注解，等同于XML <bean/> 元素。支持<bean/> 提供的大部分属性，例如：init-method、destroy-method、autowiring、lazy-init、dependency-check、depends-on 和 scope。

**依赖注入**

这里只是演示注解方式的依赖注入，关于详细的看Spring IoC部分内容。

注解方式的构造器注入

```java
@Configuration
public class AppConfig {
    @Bean
    public Foo foo() {
        return new Foo(bar());
    }

    @Bean
    public Bar bar() {
        return new Bar();
    }
}
```





## 模块化配置



## 外部值使用



## 合并配置格式



## 事务管理支持



## AOP支持



## JMX支持



## 测试支持



## JavaConfig拓展

