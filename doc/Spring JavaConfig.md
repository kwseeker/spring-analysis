# Spring JavaConfig

[JavaConfig Reference Guide](https://docs.spring.io/spring-javaconfig/docs/1.0.0.M4/reference/htmlsingle/spring-javaconfig-reference.html)

文档比较老，里面有些东西在新版本已经有了较大的改动。



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

### @Configuration

＠Configuration注解的类，作为Bean定义源文件。由@Bean 注释的方法组成，这些方法定义了将由 Spring IoC 容器管理的对象的实例化、配置和初始化逻辑。

一个应用中可以使用一个或多个@Configuration注解的类。

```java
//使用多个@Configuration注解的配置类
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class, HttpConfig.class);
//如果配置类太多，还可以通过指定package名，由spring自动扫描获取配置类
//package也可以指定多个
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("top.kwseeker.spring.config.introduction");
//而且还可以通过通配符进行匹配（测试失败）
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("**/config/introduction/*.class");
```

> @Configuration 可以被认为等同于 XML 的 <beans/> 元素。

### @Bean

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

**生命周期方法执行顺序**

[Combining lifecycle mechanisms](https://docs.spring.io/spring-framework/docs/2.5.x/reference/beans.html#beans-factory-lifecycle-combined-effects)

初始化方法执行顺序：

- Methods annotated with `@PostConstruct`
- `afterPropertiesSet()` as defined by the `InitializingBean` callback interface
- A custom configured `init()` method

销毁方法执行顺序：

- Methods annotated with `@PreDestroy`
- `destroy()` as defined by the `DisposableBean` callback interface
- A custom configured `destroy()` method

参考： top.kwseeker.spring.config.introduction.ServiceImpl.java

```txt
# 初始化
18:32:59.548 [main] DEBUG org.springframework.context.annotation.CommonAnnotationBeanPostProcessor - Invoking init method on bean 'service': public void top.kwseeker.spring.config.introduction.ServiceImpl.postConstructMethod()
top.kwseeker.spring.config.introduction.ServiceImpl postConstructMethod() ...
18:32:59.549 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Invoking afterPropertiesSet() on bean with name 'service'
top.kwseeker.spring.config.introduction.ServiceImpl afterPropertiesSet() ...
18:32:59.549 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Invoking init method  'customInit' on bean with name 'service'
top.kwseeker.spring.config.introduction.ServiceImpl customInit() ...
# 销毁
18:45:24.208 [Thread-0] DEBUG org.springframework.context.annotation.CommonAnnotationBeanPostProcessor - Invoking destroy method on bean 'service': public void top.kwseeker.spring.config.introduction.ServiceImpl.preDestroyMethod()
top.kwseeker.spring.config.introduction.ServiceImpl preDestroyMethod() ...
18:45:24.208 [Thread-0] DEBUG org.springframework.beans.factory.support.DisposableBeanAdapter - Invoking destroy() on bean with name 'service'
top.kwseeker.spring.config.introduction.ServiceImpl destroy() ...
18:45:24.208 [Thread-0] DEBUG org.springframework.beans.factory.support.DisposableBeanAdapter - Invoking destroy method 'customDestroy' on bean with name 'service'
top.kwseeker.spring.config.introduction.ServiceImpl customDestroy() ...
```

**Aware接口使用**

JavaConfig中使用没什么特殊的，Spring可以自动解析实现了哪些Aware接口并执行对应的处理。

**指定Bean的作用域**

使用@Bean的scope属性。

ScopedProxyMode: 作用域代理模式，解决作用域依赖。

**Lookup方法注入**

适合用在单例范围的Bean依赖于原型范围的Bean的情况。

**定制Bean命名**

可以通过CustomBeanNamingStrategy自定义Bean命名策略，并通过setter方法赋值给容器，有需要再看吧。

```java
ctx.setBeanNamingStrategy(new CustomBeanNamingStrategy());
```

**FactoryBean的实现**

FactoryBean是一个接口，是用来创建复杂的Bean实例的。注意FactoryBean定义实例化和普通Bean没什么区别，但是getBean时使用FactoryBean的名字，得到的并不是FactoryBean Bean本身，而是FactoryBean的T getObject() throws Exception; 方法实现返回的实例。

如果需要获取FactoryBean Bean本身可以 getBean("&FactoryBeanName")。

```java
public interface FactoryBean<T> {
    //返回的对象实例
    T getObject() throws Exception;
    //Bean的类型
    Class<?> getObjectType();
    //true是单例，false是非单例  在Spring5.0中此方法利用了JDK1.8的新特性变成了default方法，返回true
    boolean isSingleton();
}
```

**类型安全的访问消除歧义**

+ **@primary指定多个同类型Bean中的主要Bean**

+ **通过byName方式获取Bean**
+ **getBeansOfType() 获取所有此类型的Bean的Map集合**



## 模块化配置

### 跨＠Configuration类引用Bean

可以在一个@Configuration类中通过@Autowired注入另一个@Configuration类中定义的Bean．

也可以在一个@Configuration类中通过@Autowired注入另一个@Configuration类（本身也是实例化为Bean由Spring管理的）．

### @Import聚合多个配置类到一个全局配置类

```java
@Configuration
@Import({ DataSourceConfig.class, TransactionConfig.class })
public class AppConfig {
		...
}
```

#### @Import应用

应用参考：spring-javaconfig top.kwseeker.spring.config.importAnno.*

[＠Import API](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Import.html)

@Import 用于导入的一个或多个组件类 — 通常是 @Configuration 类。

支持导入3种组件类：@Configuration, ImportSelector, ImportBeanDefinitionRegistrar, 还可以直接导入@Component注解的Bean 。

为了方便拓展（如：外部模块通常会提供默认实现，但是业务中可能常常需要重新定制，于是怎么选择自定制的实现而不是默认的实现呢？），自动配置提供了ImportSelector的一个变种: DeferredImportSelector。通过 @ConditionalOnBean 注解判断是否有自定义的实现，有则使用自定义的实现，否则使用默认的实现。

> 条件注解的内容参考Spring Boot源码分析。

#### @Import原理

**DeferredImportSelector原理**

参考 `ConfigurationClassParser$parse(Set<BeanDefinitionHolder> configCandidates)`方法。

如果getImportGroup返回自定义Group, 会调用自定义Group的process方法加载BeanDefinition; 
如果getImportGroup返回 null, 会调用DefaultDeferredImportSelectorGroup的process方法, 即调用selectImports。

具体处理流程：

```java
public void parse(Set<BeanDefinitionHolder> configCandidates) {
    this.deferredImportSelectors = new LinkedList();
    Iterator var2 = configCandidates.iterator();
	
    //遍历处理除了DeferredImportSelectors外导入的组件
    //处理主要是注册到 configurationClasses (Map<ConfigurationClass, ConfigurationClass>) 用于后置处理
    //（如果是普通类解析下@PropertySources、如果是配置类看看有没有@ComponentScans、@Import[可能一层层深入进去]）
    while(var2.hasNext()) {
        BeanDefinitionHolder holder = (BeanDefinitionHolder)var2.next();
        BeanDefinition bd = holder.getBeanDefinition();

        try {
            if (bd instanceof AnnotatedBeanDefinition) {
                this.parse(((AnnotatedBeanDefinition)bd).getMetadata(), holder.getBeanName());
            } else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition)bd).hasBeanClass()) {
                this.parse(((AbstractBeanDefinition)bd).getBeanClass(), holder.getBeanName());
            } else {
                this.parse(bd.getBeanClassName(), holder.getBeanName());
            }
        } catch (BeanDefinitionStoreException var6) {
            throw var6;
        } catch (Throwable var7) {
            throw new BeanDefinitionStoreException("Failed to parse configuration class [" + bd.getBeanClassName() + "]", var7);
        }
    }

    //由此可见DeferredImportSelectors是在所有配置类中@Bean、@Import导入的@Component、配置类、ImportSelector等都处理完才处理的
    this.processDeferredImportSelectors();
}
```

@Import 导入的类的处理

```java
//ConfigurationClassParser
private void processImports(ConfigurationClass configClass, ConfigurationClassParser.SourceClass currentSourceClass, Collection<ConfigurationClassParser.SourceClass> importCandidates, boolean checkForCircularImports) {
    if (!importCandidates.isEmpty()) {
        if (checkForCircularImports && this.isChainedImportOnStack(configClass)) {
            this.problemReporter.error(new ConfigurationClassParser.CircularImportProblem(configClass, this.importStack));
        } else {
            this.importStack.push(configClass);

            try {
                //就是我们测试案例中通过@Import导入的三个类（SourceClass）
                Iterator var5 = importCandidates.iterator();

                while(true) {
                    while(true) {
                        while(var5.hasNext()) {
                            ConfigurationClassParser.SourceClass candidate = (ConfigurationClassParser.SourceClass)var5.next();
                            Class candidateClass;
                            //@Import导入的实现了ImportSelector接口的类
                            if (candidate.isAssignable(ImportSelector.class)) {
                                candidateClass = candidate.loadClass();
                                //1 先反射实例化ImportSelector实现类
                                ImportSelector selector = (ImportSelector)BeanUtils.instantiateClass(candidateClass, ImportSelector.class);
                                ParserStrategyUtils.invokeAwareMethods(selector, this.environment, this.resourceLoader, this.registry);
                                //2 DeferredImportSelector 先加入到List中最后处理，ImportSelector直接处理
                                if (this.deferredImportSelectors != null && selector instanceof DeferredImportSelector) {
                                    this.deferredImportSelectors.add(new ConfigurationClassParser.DeferredImportSelectorHolder(configClass, (DeferredImportSelector)selector));
                                } else {
                                    String[] importClassNames = selector.selectImports(currentSourceClass.getMetadata());
                                    Collection<ConfigurationClassParser.SourceClass> importSourceClasses = this.asSourceClasses(importClassNames);
                                    this.processImports(configClass, currentSourceClass, importSourceClasses, false);
                                }
                            //@Import导入的实现了ImportBeanDefinitionRegistrar接口的类
                            } else if (candidate.isAssignable(ImportBeanDefinitionRegistrar.class)) {
                                candidateClass = candidate.loadClass();
                                ImportBeanDefinitionRegistrar registrar = (ImportBeanDefinitionRegistrar)BeanUtils.instantiateClass(candidateClass, ImportBeanDefinitionRegistrar.class);
                                ParserStrategyUtils.invokeAwareMethods(registrar, this.environment, this.resourceLoader, this.registry);
                                configClass.addImportBeanDefinitionRegistrar(registrar, currentSourceClass.getMetadata());
                            //其他类（如@Component）
                            } else {
                                this.importStack.registerImport(currentSourceClass.getMetadata(), candidate.getMetadata().getClassName());
                                this.processConfigurationClass(candidate.asConfigClass(configClass));
                            }
                        }

                        return;
                    }
                }
            } catch (BeanDefinitionStoreException var15) {
                throw var15;
            } catch (Throwable var16) {
                throw new BeanDefinitionStoreException("Failed to process import candidates for configuration class [" + configClass.getMetadata().getClassName() + "]", var16);
            } finally {
                this.importStack.pop();
            }
        }
    }
}
```

初步处理之后 configurationClasses 数据:

```java
configurationClasses = {LinkedHashMap@1416}  size = 3
 {ConfigurationClass@2000} "ConfigurationClass: beanName 'null', top.kwseeker.spring.config.importAnno.beans.Person" -> {ConfigurationClass@2000} "ConfigurationClass: beanName 'null', top.kwseeker.spring.config.importAnno.beans.Person"
  key = {ConfigurationClass@2000} "ConfigurationClass: beanName 'null', top.kwseeker.spring.config.importAnno.beans.Person"
  value = {ConfigurationClass@2000} "ConfigurationClass: beanName 'null', top.kwseeker.spring.config.importAnno.beans.Person"
   metadata = {StandardAnnotationMetadata@2005} 
    annotations = {Annotation[1]@2015} 
     0 = {$Proxy6@2037} "@org.springframework.stereotype.Component(value=person)"
    nestedAnnotationsAsMap = true
    introspectedClass = {Class@1428} "class top.kwseeker.spring.config.importAnno.beans.Person"
   resource = {DescriptiveResource@2006} "top.kwseeker.spring.config.importAnno.beans.Person"
   beanName = null
   importedBy = {LinkedHashSet@2007}  size = 1
   beanMethods = {LinkedHashSet@2008}  size = 0
   importedResources = {LinkedHashMap@2009}  size = 0
   importBeanDefinitionRegistrars = {LinkedHashMap@2010}  size = 0
   skippedBeanMethods = {HashSet@2011}  size = 0
 {ConfigurationClass@1795} "ConfigurationClass: beanName 'null', top.kwseeker.spring.config.importAnno.DatabaseConfig" -> {ConfigurationClass@1795} "ConfigurationClass: beanName 'null', top.kwseeker.spring.config.importAnno.DatabaseConfig"
  key = {ConfigurationClass@1795} "ConfigurationClass: beanName 'null', top.kwseeker.spring.config.importAnno.DatabaseConfig"
  value = {ConfigurationClass@1795} "ConfigurationClass: beanName 'null', top.kwseeker.spring.config.importAnno.DatabaseConfig"
   metadata = {StandardAnnotationMetadata@1813} 
    annotations = {Annotation[1]@1817} 
     0 = {$Proxy9@2191} "@org.springframework.context.annotation.PropertySource(name=, factory=interface org.springframework.core.io.support.PropertySourceFactory, ignoreResourceNotFound=false, encoding=, value=[classpath:db.properties])"
    nestedAnnotationsAsMap = true
    introspectedClass = {Class@1556} "class top.kwseeker.spring.config.importAnno.DatabaseConfig"
   resource = {DescriptiveResource@2017} "top.kwseeker.spring.config.importAnno.DatabaseConfig"
   beanName = null
   importedBy = {LinkedHashSet@2018}  size = 1
   beanMethods = {LinkedHashSet@2019}  size = 1
   importedResources = {LinkedHashMap@2020}  size = 0
   importBeanDefinitionRegistrars = {LinkedHashMap@2021}  size = 0
   skippedBeanMethods = {HashSet@2022}  size = 0
 {ConfigurationClass@1667} "ConfigurationClass: beanName 'globalConfig', top.kwseeker.spring.config.importAnno.GlobalConfig" -> {ConfigurationClass@1667} "ConfigurationClass: beanName 'globalConfig', top.kwseeker.spring.config.importAnno.GlobalConfig"
  key = {ConfigurationClass@1667} "ConfigurationClass: beanName 'globalConfig', top.kwseeker.spring.config.importAnno.GlobalConfig"
  value = {ConfigurationClass@1667} "ConfigurationClass: beanName 'globalConfig', top.kwseeker.spring.config.importAnno.GlobalConfig"
   metadata = {StandardAnnotationMetadata@1987} 
    annotations = {Annotation[2]@2184} 
     0 = {$Proxy4@2186} "@org.springframework.context.annotation.Configuration(value=)"
     1 = {$Proxy5@2187} "@org.springframework.context.annotation.Import(value=[class top.kwseeker.spring.config.importAnno.beans.Person, class top.kwseeker.spring.config.importAnno.DatabaseConfig, class top.kwseeker.spring.config.importAnno.service.UserServiceDeferredImportSelector])"
    nestedAnnotationsAsMap = true
    introspectedClass = {Class@1561} "class top.kwseeker.spring.config.importAnno.GlobalConfig"
   resource = {DescriptiveResource@1988} "top.kwseeker.spring.config.importAnno.GlobalConfig"
   beanName = "globalConfig"
   importedBy = {LinkedHashSet@1990}  size = 0
   beanMethods = {LinkedHashSet@1991}  size = 0
   importedResources = {LinkedHashMap@1992}  size = 0
   importBeanDefinitionRegistrars = {LinkedHashMap@1993}  size = 0
   skippedBeanMethods = {HashSet@1994}  size = 0
knownSuperclasses = {HashMap@1417}  size = 0
propertySourceNames = {ArrayList@1418}  size = 1
importStack = {ConfigurationClassParser$ImportStack@1419}  size = 0
deferredImportSelectors = {LinkedList@1657}  size = 1
 0 = {ConfigurationClassParser$DeferredImportSelectorHolder@2196} 
  configurationClass = {ConfigurationClass@1667} "ConfigurationClass: beanName 'globalConfig', top.kwseeker.spring.config.importAnno.GlobalConfig"
  importSelector = {UserServiceDeferredImportSelector@2197} 
```

DeferredImportSelector 处理流程（由下面代码可见，DeferredImportSelector多了排序、分组归类）

```java
private void processDeferredImportSelectors() {
    List<ConfigurationClassParser.DeferredImportSelectorHolder> deferredImports = this.deferredImportSelectors;
    this.deferredImportSelectors = null;
    if (deferredImports != null) {
        //排序 TODO
        deferredImports.sort(DEFERRED_IMPORT_COMPARATOR);
        Map<Object, ConfigurationClassParser.DeferredImportSelectorGrouping> groupings = new LinkedHashMap();
        Map<AnnotationMetadata, ConfigurationClass> configurationClasses = new HashMap();
        Iterator var4 = deferredImports.iterator();
		//遍历DeferredImportSelector，进行分组归类 TODO
        while(var4.hasNext()) {
            ConfigurationClassParser.DeferredImportSelectorHolder deferredImport = (ConfigurationClassParser.DeferredImportSelectorHolder)var4.next();
            Class<? extends Group> group = deferredImport.getImportSelector().getImportGroup();
            ConfigurationClassParser.DeferredImportSelectorGrouping grouping = (ConfigurationClassParser.DeferredImportSelectorGrouping)groupings.computeIfAbsent(group == null ? deferredImport : group, (key) -> {
                return new ConfigurationClassParser.DeferredImportSelectorGrouping(this.createGroup(group));
            });
            grouping.add(deferredImport);
            configurationClasses.put(deferredImport.getConfigurationClass().getMetadata(), deferredImport.getConfigurationClass());
        }
		//
        var4 = groupings.values().iterator();
        while(var4.hasNext()) {
            ConfigurationClassParser.DeferredImportSelectorGrouping grouping = (ConfigurationClassParser.DeferredImportSelectorGrouping)var4.next();
            grouping.getImports().forEach((entry) -> {
                ConfigurationClass configurationClass = (ConfigurationClass)configurationClasses.get(entry.getMetadata());

                try {
                    //递归
                    this.processImports(configurationClass, this.asSourceClass(configurationClass), this.asSourceClasses(entry.getImportClassName()), false);
                } catch (BeanDefinitionStoreException var5) {
                    throw var5;
                } catch (Throwable var6) {
                    throw new BeanDefinitionStoreException("Failed to process import candidates for configuration class [" + configurationClass.getMetadata().getClassName() + "]", var6);
                }
            });
        }

    }
}
```

还没完，ConfigurationClass List 怎么处理的？

TODO



## 外部值使用

对应新版本应该说的是 @Value 和 @ConfigurationProperties 。



## 合并配置格式

JavaConfig 支持和XML配置方式一起使用。

### XML和JavaConfig混用

**XML为主JavaConfig为辅**

只需要引入ConfigurationPostProcessor, 然后就可以处理代码中的@Configuration类。

```xml
<bean class="org.springframework.config.java.process.ConfigurationPostProcessor"/>
```

**JavaConfig为主XML为辅**

可以在@Configuration配置类上使用@ImportXml注解导入XML配置。

```
@ImportXml("classpath:com/company/app/datasource-config.xml")
```

### 注解驱动配置

**@ComponentScan**

使用@ComponentScan扫描基于＠Component的注解的Bean,　如 @Controller、@Service、@Repository，包括＠Component。



## 事务管理支持

参考新版本文档。

## AOP支持

参考新版本文档。

## JMX支持

参考新版本文档。

## 测试支持

参考新版本文档。

## JavaConfig拓展

略。