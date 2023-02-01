# Spring JDBC

实现流程参考对应流程图，这里只是作为流程图的附录与注释。

这里主要分析Spring数据源和JDBCTemplate实现原理（事务放在另一个文档单独分析）。生产环境基本不会直接使用Spring JDBC, 但是生产环境很多其他框架都是以它为基础进行数据库访问的。

官方文档：https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc

## 流程分工

下面是Spring-JDBC进行数据库操作的流程，即分工（spring与开发者各自负责那部分操作，大部分工作都是Spring完成的）。

| Action                                                   | Spring | You  |
| :------------------------------------------------------- | :----- | :--- |
| Define connection parameters.                            |        | X    |
| Open the connection.                                     | X      |      |
| Specify the SQL statement.                               |        | X    |
| Declare parameters and provide parameter values          |        | X    |
| Prepare and run the statement.                           | X      |      |
| Set up the loop to iterate through the results (if any). | X      |      |
| Do the work for each iteration.                          |        | X    |
| Process any exception.                                   | X      |      |
| Handle transactions.                                     | X      |      |
| Close the connection, the statement, and the resultset.  | X      |      |

## Spring JDBC 官方文档摘要

### 访问数据库的方式

+ **JdbcTemplate**

+ **NamedParameterJdbcTemplate**

  在JdbcTemplate基础上提供命名参数而不是使用传统的 JDBC "?"占位符。当一条 SQL 语句有多个参数时，这种方法可以提供更好的文档和易用性（传参不随参数顺序填充，通过命名匹配填充）。

+ SimpleJdbcInsert
+ SimpleJdbcCall

+ RDBMS objects（MappingSqlQuery、SqlUpdate、StoredProcedure）

### spring-jdbc 包继承

```java
org.springframework:spring-jdbc:5.0.7.RELEASE
    org.springframework:spring-beans:5.0.7.RELEASE
    org.springframework:spring-core:5.0.7.RELEASE
    org.springframework:spring-tx:5.0.7.RELEASE
        org.springframework:spring-beans:5.0.7.RELEASE
        org.springframework:spring-core:5.0.7.RELEASE
```

主要的package

```java
org.springframework.jdbc.core	//提供JdbcTemplate等接口类实现
org.springframework.jdbc.datasource	//定义各种数据源实现（包括关系型数据库、NoSQL数据库、嵌入式数据库）
org.springframework.jdbc.object		//用于RDBMS objects
org.springframework.jdbc.support	//提供SQLEXCEPTION转换功能和一些实用程序类。JDBC处理期间抛出的异常转化为org.springframework.dao软件包中定义的异常。
org.springframework.dao		//spring DAO层通用定义
```

### JDBC核心类使用

- [Using `JdbcTemplate`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-JdbcTemplate)

  包括执行SQL查询（select）、调用更新语句(update：增删改)与存储过程、对结果集ResultSet迭代并提取返回参数值、捕获JDBC异常并转换为`org.springframework.dao`中更通用的异常。

  JdbcTemplate是线程安全的，且有状态内部记录了连接到的数据源。

  > TIPS:
  >
  > JdbcTemplate.queryForObject()等方法支持将结果集转成领域对象（通过RowMapper 或 领域对象Class类型[内部也是基于RowMapper进行转换]）。
  >
  > JdbcDaoSupport的用途
  >
  > 官方文档没说清，直接看源码吧，源码很简单不算注释不超100行。JdbcDaoSupport 继承 DaoSupport，DaoSupport 实现了 InitializingBean接口。看代码可知主要是帮我们**自动完成了JdbcTemplate的注入**和其他一些DAO实例初始化阶段的处理拓展。
  >
  > 对应的还有一个NamedParameterJdbcDaoSupport，可以自动注入NamedParameterJdbcTemplate Bean实例。

- [Using `NamedParameterJdbcTemplate`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-NamedParameterJdbcTemplate)

  具名参数使用":" + 参数名指定，参数名对应SqlParameterSource中的key。

  SqlParameterSource有三种实现：

  + MapSqlParameterSource

    存储参数名和值的本质是个`Map<String, Object>`, NamedParameterJdbcTemplate也支持直接使用Map替代SqlParameterSource作为参数映射。

  + BeanPropertySqlParameterSource

  + EmptySqlParameterSource

  ```java
  public int countOfActorsByFirstName(String firstName) {
      String sql = "select count(*) from T_ACTOR where first_name = :first_name";
      SqlParameterSource namedParameters = new MapSqlParameterSource("first_name", firstName);
      return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
  }
  ```

- [Using `SQLExceptionTranslator`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-SQLExceptionTranslator)

- [Running Statements](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-statements-executing)

- [Running Queries](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-statements-querying)

- [Updating the Database](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-updates)

- [Retrieving Auto-generated Keys](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-auto-generated-keys)

  检索数据库自动生成的主键。

  ```java
  KeyHolder keyHolder = new GeneratedKeyHolder(); //用于存储自动生成的主键
  int ret = jdbcTemplate.update("insert into user (name) values(?)", name, keyHolder);
  Number pk = keyHolder.getKey();
  ```

### 数据库连接控制

- [Using `DataSource`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-datasource)

  Spring通过DataSource获取数据库的连接。DataSource是JDBC规范的一部分，是一个广义的连接工厂。它允许容器或框架对应用程序代码隐藏连接池和事务管理问题。

  当您使用Spring的JDBC层时，可以从JNDI获得数据源，也可以使用第三方提供的连接池实现配置自己的数据源。

  DriverManagerDataSource和SimpleDriverDataSource这些变体不提供池，并且当对一个连接发出多个请求时性能很差。

- [Using `DataSourceUtils`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-DataSourceUtils)

  DataSourceUtils类是一个方便而强大的辅助类，它提供了静态方法来从JNDI获取连接，并在必要时关闭连接。它支持线程绑定连接，例如DataSourceTransactionManager。

- [Implementing `SmartDataSource`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-SmartDataSource)

  SmartDataSource接口应该由能够提供关系数据库连接的类实现。它扩展了DataSource接口，让使用它的类**查询在给定操作之后是否应该关闭连接**。当知道需要重用某个连接时，这种用法是有效的。

- [Extending `AbstractDataSource`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-AbstractDataSource)

- [Using `SingleConnectionDataSource`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-SingleConnectionDataSource)

  SingleConnectionDataSource类是SmartDataSource接口的实现，该接口封装了一个在每次使用后都不会关闭的连接。它不支持多线程。

  SingleConnectionDataSource主要是一个测试类。它通常能够与简单的JNDI环境一起在应用程序服务器之外轻松地测试代码。与DriverManagerDataSource相比，它总是重用相同的连接，避免了过多的物理连接创建。

- [Using `DriverManagerDataSource`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-DriverManagerDataSource)

  DriverManagerDataSource类是标准DataSource接口的实现，该接口通过bean属性配置普通JDBC驱动程序，并每次返回一个新的Connection。

- [Using `TransactionAwareDataSourceProxy`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-TransactionAwareDataSourceProxy)

  TransactionAwareDataSourceProxy是目标数据源的代理。代理包装以DataSource为目标，以添加spring管理事务的感知。

- [Using `DataSourceTransactionManager`](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc-DataSourceTransactionManager)

  DataSourceTransactionManager类是一个用于单个JDBC数据源的PlatformTransactionManager实现。它将来自指定数据源的JDBC连接绑定到当前执行的线程，可能允许每个数据源有一个线程连接。

  DataSourceTransactionManager类支持自定义隔离级别和超时，可作为适当的JDBC语句查询超时用。为了支持后者，应用程序代码必须使用JdbcTemplate或为每个创建的语句调用DataSourceUtils.applyTransactionTimeout(..)方法。

  在单数据源的情况下，可以使用这个实现而不是JtaTransactionManager，因为它不需要容器支持JTA。在两者之间切换只是一个配置问题，前提是坚持使用规定的连接查找模式。JTA不支持自定义隔离级别。

### JDBC批量操作

通过实现特殊接口BatchPreparedStatementSetter的两个方法，并将该实现作为batchUpdate方法调用中的第二个参数传入，可以完成JdbcTemplate批处理。

如果从流或文件读取数据进行批量操作，则可能有预订的批处理大小，但最后一批批处理的大小可能没有达到指定的大小。在这种情况下，可以使用InterruptibleBatchPreparedStatementSetter接口，该接口允许在输入源耗尽时中断批处理。isBatchExhausted方法允许您发出批处理结束的信号（然后触发最后一批批处理操作）。

### 使用SimpleJdbc简化JDBC操作

### 将JDBC操作建模为Java对象

### 参数和结果处理的常见问题



### 嵌入式数据库的支持

### 初始化数据源



## Spring JDBC 工作原理源码分析































