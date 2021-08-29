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

Spring FactoryBean是通过里面的getObject()方法拿到动态代理Bean实例的，看下ProxyFactoryBean的数据结构和getObject()方法。

数据结构：

```java
//连接点 -> 通知列表
Map<MethodCacheKey, List<Object>> methodCache;
```

流程：

１）初始化Advisor链；

２）根据作用域类型创建单例或原型代理实例；

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

initializeAdvisorChain() 

ProxyFactoryBean中我们配置了两个通知（一个MethodBeforeAdvice、一个MethodInterceptor（可以理解为环绕通知，继承Advice接口）、一个全局通知（通知通配符）），然后会遍历这三个通知，通过判断末尾是否有`*`，按照不同方式创建Advice实例，然后再与PointCut实例结合封装成Advisor(注意Advisor没有被IoC管理)，然后加入到当前ProxyFactoryBean的advisor链（LinkedList）中。

Advisor链对于代理对象就像BeanDefinition对于Bean。

```java
//通过DefaultAdvisorAdapterRegistry封装Advice对象
//	1) 是否是Advisor，是直接返回
//	2) 是否是MethodInterceptor（环绕通知）
// 	3) 是否是MethodBeforeAdvice -> MethodBeforeAdviceInterceptor
//  4) 是否是AfterReturingAdvice -> AfterReturningAdviceInterceptor
//	5) 是否是ThrowsAdvice -> ThrowsAdviceInterceptor
//	2-5) Interceptor -> DefaultPointcutAdvisor (里面pointcut=TruePointcut)
//加入Advisor数组
addAdvisorOnChainCreation(advice, name);
```

> spring core aop 6.4.6:
>
> ProxyFactoryBean setInterceptorNames() 方法可以传递带通配符的拦截器名称，所有与其匹配的Advisor都会加入到ProxyFactoryBean的Advisor链中。（相当于提供了一种简写方式）

getSingletonInstance()

```java
//TargetSource 就是要增强的目标类的实例。
//如果ProxyFactoryBean实例化过程中增强对象传递的是类，则先创建类对象实例作为TargetSource。 
//这个方法就是确保ProxyFactoryBean.targetSource成员指向增强目标对象的实例。
freshTargetSource();
//提取增强目标对象所有接口，存储到ProxyFactoryBean.interfaces属性。
setInterfaces(ClassUtils.getAllInterfacesForClass(targetClass, this.proxyClassLoader));
//创建AOP代理对象
//首先激活所有AdvisedSupportListener；???
//然后根据当前ProxyFactoryBean的配置选择使用CGlib还是JDK动态代理创建代理对象；
// CGLib: ObjenesisCglibAopProxy
//		是否需要性能优化，targetClass是否是非接口类，targetClass是否是非代理类，是否没有可代理的接口
// JDK: JdkDynamicAopProxy
//		除了上面其他情况
getProxy(createAopProxy());
	//config即ProxyFactoryBean(继承AdvisedSupport接口)
	//这个类并不是代理类而是用于生成代理类的，同时又是InvocationHandler实例。
	new JdkDynamicAopProxy(config);
	getProxy()
        //JDK动态代理
        Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);     
```

３）使用动态代理对象执行目标方法。

```java
//当使用动态代理类执行增强的方法时，如案例
int sum = calculate.add(1, 1);
	//下面第２、3点代码是最需要关注的逻辑
	//1) 获取被增强类的实例,JDK动态代理需要它完成基本功能
	//2) 获取增强方法的增强逻辑（即绑定到该方法的所有通知的List,首次获取会缓存到methodCache）
	//	 对应代码：List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
	//3) 创建ReflectiveMethodInvocation，就是将各种通知逻辑加入到InvocationHandler::invoke前后，生成的加强版方法的反射执行器。具体看其process()方法实现。
	//	 对应代码：invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
	//4) 执行ReflectiveMethodInvocation::process
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取当前方法的拦截器链
        //1) 获取方法的Class类型
        //2) 获取DefaultAdvisorAdapterRegistry
        //3) 遍历ProxyBeanFactory的Advisor数组，根据类型，通过DefaultAdvisorAdapterRegistry转换为Interceptor,最终返回Interceptor链
        //4)     MethodInterceptor | MethodBeforeAdviceInterceptor | AfterReturningAdviceInterceptor | ThrowsAdviceInterceptor
        this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
        //代理对象，被代理对象，方法，参数，被代理类，拦截器链
        new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
        //是个递归调用
        //判断待执行的拦截器个数，然后从index=0开始依次递归执行拦截器逻辑，
        //执行前有一步判断是否是InterceptorAndDynamicMethodMatcher类型（多了一步方法模式匹配），此案例中没有用到，但是如果Pointcut有指定拦截的方法名会走这里
        retVal = invocation.proceed();
    }
```

问题：

+ 要增强的目标类为何要实例化（对应TargetSource实例）？

  其中一个原因：<u>JDK动态代理依然要依赖被代理类的实例</u>，增强功能是动态代理类完成的，但是核心功能还是通过动态代理类反射调用被代理类实例完成的。

  衍生问题：CGLib不需要依赖被代理类实例，是不是代理类实例对CGLib动态代理实现AOP无用？

+ 案例中的LogBeforeAdvice为何对应的ClassFilter为TrueClassFilter（这是全局Advisor应该用的吧）？

  ```java
  //logBeforeAdvice明明指定的是SimpleCalculator类实例
  proxyFactory.setInterceptorNames("logBeforeAdvice", "logInterceptor", "global*");
  proxyFactory.setTarget(calculate);
  ```

+ Advice为何要Bean实例化？

  JDK动态代理执行切面逻辑时是执行的Advice实例的通知方法。

+ SpringProxy、Advised、DecoratingProxy接口？



## Spring 2.0 基于配置实现原理

### 使用案例

### 原理分析



## JDK动态代理 & CGLib动态代理

两者都使用到字节码编程。

JDK是通过反射的方式实现的（*并没有使用ASM*），且只是生成了一个架子，代理对象方法并没有具体的业务逻辑，而是内部拥有InvocationHandler对象的引用，通过执行这个对象方法，进而执行增强逻辑和基本的业务逻辑的（即执行过程中仍然要反射调用被增强的类实例）。

> **JDK动态代理是委托机制**，JDK动态代理类相当于被代理类的秘书，最终做事还是要反射调用被代理类的实例。

CGLib则是使用ASM框架实现的，读取被代理类的class文件，对class文件修改（即对方法等进行增强）生成新的类，执行过程与原来的被代理类就没有什么关系了。

> **CGLib动态代理是继承机制**，CGLib动态代理类完全复制了被代理类的功能，就像一个增强的克隆体。业务执行和被代理类完全无关。

所以jdk动态代理的方式创建代理对象效率较高，执行效率较低，cglib创建效率较低，执行效率高。