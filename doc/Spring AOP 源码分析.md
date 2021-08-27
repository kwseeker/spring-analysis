# Spring AOP 源码分析

参考资料：

《Spring揭秘》：发展史讲的不错，其他都是一些应用讲解，AOP源码实现原理讲解欠缺。

《Spring源码深度解析》：理解源码可以看下第7章，再结合实例调试下。

官方文档core第5、6章节。

## 发展史

关注遇到了什么问题，怎么演化的。

### AOP技术发展

软件开发过程中经常会碰到一些横切关注点问题（*和核心业务没有什么关系，但是核心业务又必须引入的通用功能，经典的案例有日志记录、安全校验、事务管理，个人理解一些业务的消息推送、格式化返回值也属于这一类问题*），随着业务增多，每个业务都要手动引入一次，不利于系统开发和维护。

比较理想的状态是将这些通用功能模块化，由工具或JVM自动引入（软件编码上零侵入）。

**静态AOP**

为了解决这种需求，第一代AOP技术产生，这代AOP都是静态AOP，以AspectJ为代表，需要使用特殊的编译器，将切面静态织入到类中，使用ajc编译器编译后可以看到对应的class文件有插入了一些代码。

这种方式切面织入在编译期间完成，不会损失性能，但是对应灵活性就不太好了(*关于灵活性举个例子：比如有个针对某个package下所有类方法的切面，如果package下面通过热加载新插入一个类，静态AOP就必须重新编译，但是动态AOP不需要*)。

**动态AOP**

因为编程语言引入了一些动态特性（反射、动态代理等），可以解决上面灵活性不足的问题，对应AOP也应用语言的动态特性发展出第二代AOP（动态AOP），动态AOP切面织入时机在类加载或运行时，采用了一些字节码编程技术（JDK动态代理、CGLib、ASM等，更多参考byte-code仓库），静态AOP是改变原有class，动态AOP则是生成新的class替代原有的class。

### Spring AOP 发展

注意Spring AOP属于第二代AOP(动态AOP)。

+ **Spring 1.2 基于接口实现AOP**（第一代）
+ **Spring 2.0 schema-base配置（XML配置）实现AOP**（第二代）
+ **Spring 2.0 @AspectJ配置实现AOP**（第二代）

虽然Spring AOP经历了两代发展，但是底层实现基本是不变的，变的只是使用方式。也就是说Spring AOP底层接口等还是那些。（TODO 通过源码再体会下）

只有少部分的改变：

+ **引入了@AspectJ的切面定义、解析和匹配**

+ **织入方式的改变**

  第一代：`FactoryBean` 编程式织入

  第二代：`AutoProxyCreator` 自动代理织入

## Spring 1.2 基于接口实现原理

[Spring AOP APIs](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-api) 这部分官方文档都是讲的这种老式的基于接口实现的AOP的使用方式。

### 使用案例

先看下使用案例，然后思考下如果要自己实现AOP，应该怎么做？

案例参考：aopapi。

需要使用FactoryBean (ProxyFactoryBean) 的方式创建代理对象。针对每一个需要增强的类都需要创建一个对应的ProxyFactoryBean。



### 原理分析

**ProxyFactoryBean 创建AOP代理对象原理**

Spring FactoryBean是通过里面的getObject()方法拿到Bean实例的，看下ProxyFactoryBean的数据结构和getObject()方法。

```java
public Object getObject() throws BeansException {
    initializeAdvisorChain();			//初始化Advisor链
    if (isSingleton()) {
        return getSingletonInstance();
    }
    else {
        if (this.targetName == null) {
            logger.warn("Using non-singleton proxies with singleton targets is often undesirable. " +
                    "Enable prototype proxies by setting the 'targetName' property.");
        }
        return newPrototypeInstance();
    }
}
```

**initializeAdvisorChain()** 

ProxyFactoryBean中我们配置了两个通知（一个MethodBeforeAdvice、一个MethodInterceptor（可以理解为环绕通知，继承Advice接口）、一个全局通知（通知通配符）），然后会遍历这三个通知，通过判断末尾是否有`*`，按照不同方式创建Advice实例，然后再与PointCut实例结合封装成Advisor(注意Advisor没有被IoC管理)，然后加入到当前ProxyFactoryBean的advisor链（LinkedList）中。

> spring core aop 6.4.6:
>
> ProxyFactoryBean setInterceptorNames() 方法可以传递带通配符的拦截器名称，所有与其匹配的Advisor都会加入到ProxyFactoryBean的Advisor链中。（相当于提供了一种简写方式）

疑问：

+ Advice为何要Bean实例化？

  

## Spring 2.0 基于配置实现原理

### 使用案例

### 原理分析

