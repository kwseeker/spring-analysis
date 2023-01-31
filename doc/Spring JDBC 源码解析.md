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



### JDBC批量操作



### 使用SimpleJdbc简化JDBC操作

### 将JDBC操作建模为Java对象

### 参数和结果处理的常见问题

### 嵌入式数据库的支持

### 初始化数据源



## Spring JDBC 工作原理源码分析































