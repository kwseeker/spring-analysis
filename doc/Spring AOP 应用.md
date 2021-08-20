# Spring AOP 应用

参考文档：

官方文档core第5、6章节。

[Aspect Oriented Programming with Spring](https://docs.spring.io/spring-framework/docs/current-SNAPSHOT/reference/html/core.html#aop)

[Spring AOP APIs](https://docs.spring.io/spring-framework/docs/current-SNAPSHOT/reference/html/core.html#aop-api)

和Aspect的区别，参考byte-code仓库。



## Spring 面向切面编程

AOP是对OOP的补充。被广泛应用在了中间件的开发上（或许所有横切关注点问题都可以写成中间件模块？）。

### SpringAOP 与 AspectJ

SpringAOP完全基于注解(可以完全依靠java代码实现)，AspectJ 同时支持aj与注解。

Spring不需要特殊编译器（运行期织入代码，动态代理运行时增强）AspectJ依赖特殊编译器（分为编译期织入[compile-time]、后编译期织入[post-compile]、加载期织入[load-time]，静态代理编译时增强）;

> AspectJ使用上感觉好像是动态代理一样，但是仍然属于静态代理，只不过AspectJ的静态代理类是由特殊编译器ajc给完成的。
>
> **静态代理类**：由程序员创建或由特定工具自动生成源代码，再对其编译。在程序运行前，代理类的.class文件就已经存在了。
> **动态代理类**：在程序运行时，运用反射机制动态创建而成。

<u>SpringAOP只是使用了AspectJ定义的注解（切入点解析和匹配）</u>并没有使用它的编译期和织入器；

即SpringAOP是动态织入，AspectJ是静态织入；对应的SpringAOP性能肯定比AspectJ差些。

SpringAOP致力于解决企业级开发中最普遍的AOP（方法织入）。而AspectJ是一个功能完备的AOP方案。

SpringAOP切面不能作为其他切面的增强，但是AspectJ是可以的（记得是可以的）。

SpringAOP只能作用与Spring容器中的Bean, 只能作用于Bean的方法（TODO, 从源码理解）。

### AOP概念

切面、连接点、通知、切点、引入（Introduction）、目标对象、代理、织入。

通知类型：前置通知、后置通知(After finally advice)、返回通知(After returning advice)、异常通知(After throwing advice)、环绕通知。

Spring AOP 仅仅支付方法执行连接点（在Bean的方法上织入通知）。并没有实现字段拦截（Filed Interception）。

> 这里说的字段拦截应该就是指AspectJ 的set() get() 切入点。
>
> 参考《AspectJ Cookbook》 第8章。

### AOP代理

SpringAOP默认为AOP代理使用标准的JDK动态代理。

也支持使用CGLib，动态代理类而不是接口，如果业务对象没有实现接口，Spring会默认选择CGLib。

当织入通知的方法未在接口上声明或需要将代理对象作为具体类型参数传递给方法的情况下（希望很少见），可以强制使用 CGLIB。

### @AspectJ支持

**启用@AspectJ风格的切面**

首先需要引入AspectJ的aspectjweaver.jar。

```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.7</version>
    <!--<scope>runtime</scope>-->
</dependency>
```

注解方式启用：

```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
}
```

XML配置启用：

```xml
<aop:aspectj-autoproxy/>
```

**定义切面类**

只需要添加@Aspect注解。

**定义切点**

语法和AspectJ应该是一样的。

SpringAOP切点标识符

+ execution 

  匹配方法执行连接点

  ```java
  
  ```

+ within 

  限制匹配某些类型中的连接点

+ this

+ target

+ args

+ @target

+ @args

+ @within

+ @annotation

**定义通知**

**引入(Introductions)**

**切面实例化模型**



### 基于XML AOP 支持

 略。

### AOP实现方案和配置风格选择



### 编程式实现@AspectJ代理



### Spring中使用AspectJ案例

可以参考下Spring这些模块源码加深对Spring AOP使用的理解。



### 其他资料

推荐了AspectJ官网和两本书《*Eclipse AspectJ* 》《*AspectJ in Action*》。



## Spring AOP APIs

