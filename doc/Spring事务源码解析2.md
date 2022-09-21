# Spring事务源码解析

此文档只是流程图（SpringTransaction.drawio）的批注。



## DriverManagerDataSource

此类位于spring-jdbc。

![](img/UML-DriverManagerDataSource.png)



## TransactionTemplate

此类位于spring-tx。

![](img/UML-TransactionTemplate.png)



## 事务传播类型

+ MANDATORY：外部存在事务就将当前操作融合到外部事务，外部不存在事务就抛异常；
+ REQUIRED（默认）：外部存在事务就将当前操作融合到外部事务，外部不存在事务就新建事务；
+ REQUIRES_NEW：外部存在事务就将外部事务先挂起，然后创建新事务，外部不存在事务直接创建新事务；
+ NESTED：外部存在事务就将当前操作融合到外部事务，SavePoint外层影响内层内层不影响外层，外部不存在事务直接创建新事务；
+ SUPPORTS: 外部存在事务就将当前操作融合到外部事务，外部不存在事务也不创建新事务；
+ NOT_SUPPORTED: 外部存在事务就将外部事务挂起，外部不存在事务也不创建新事务；
+ NEVER: 外部存在事务就抛异常，外部不存在事务也不创建新事务；



## TransactionSynchronization 事务同步器

```java
public interface TransactionSynchronization extends Flushable {

	int STATUS_COMMITTED = 0;
	int STATUS_ROLLED_BACK = 1;
	int STATUS_UNKNOWN = 2;

	// 事务挂起的时候调用此方法（准确说是在事务线程本地资源释放的前一步）
	// 看源码事务挂起是主要是释放TransactionSynchronizationManager中的NamedTheadLocal资源
    //，即释放线程本地事务资源（流程图中橙黄色部分），详细参考 AbstractPlatformTransactionManager#suspend()
	// TransactionSynchronizationManager#unbindResource
	default void suspend() {
	}
    
	// 事务恢复时候调用
    // 即重新绑定事务线程本地资源的时候执行
	// TransactionSynchronizationManager#bindResource
	default void resume() {
	}

	// 将基础会话刷新到数据存储区（如果适用） 比如Hibernate/Jpa的session
	@Override
	default void flush() {
	}

	// 在事务提交之前触发。在AbstractPlatformTransactionManager.processCommit方法里 commit之前触发
	// 事务提交之前，比如flushing SQL statements to the database
	// 请注意：若此处发生了异常，会导致回滚
	default void beforeCommit(boolean readOnly) {
	}
	// 在beforeCommit之后，在commit/rollback之前执行
	// 它和beforeCommit还有个非常大的区别是：即使beforeCommit抛出异常了  这个也会执行
	default void beforeCompletion() {
	}

	// 这个就非常重要了，它是事务提交（注意事务已经成功提交，数据库已经持久化完成这条数据了）后执行  
    // 注意此处是成功提交而没有异常
	// javadoc说了：此处一般可以发短信或者email等操作,因为事务已经成功提交了
	// =====但是但是但是：======
	// 事务虽然已经提交，但事务资源（链接connection）可能仍然是活动的和可访问的。
	// 因此，此时触发的任何数据访问代码仍将“参与”原始事务 允许执行一些清理（不再执行提交操作！）
	// 除非它明确声明它需要在单独的事务中运行。
	default void afterCommit() {
	}

	// 和上面的区别在于：即使抛出异常回滚了  它也会执行的。它的notice同上
	default void afterCompletion(int status) {
	}
}
```

