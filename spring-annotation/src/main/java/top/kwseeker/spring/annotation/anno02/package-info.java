package top.kwseeker.spring.annotation.anno02;

/*
* ＠Configuration 和　＠Component 对 @Bean 处理的不同
*
* Spring在BeanFactory后置处理阶段（BeanFactoryPostProcessor），将＠Configuration配置类的BeanDefinition的beanClass替换成了beanClass的增强类
*   beanClass原本是：top.kwseeker.spring.annotation.anno02.MyConfig
*   增强后变为：top.kwseeker.spring.annotation.anno02.MyConfig$$EnhancerBySpringCGLIB$$4c786335
*
* MyConfig 上注解 @Configuration：两个SomeBean实例内引用的是同一个CommonBean对象；
* 测试结果：
*   someBean1: 1823409783
*   someBean2: 1823409783
*
* MyConfig 上注解 @Component：两个SomeBean实例内引用的不是同一个CommonBean对象；
* 测试结果：
*   someBean1: 561480862
*   someBean2: 1087081975
* */