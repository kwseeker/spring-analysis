package top.kwseeker.jdbc.transaction;

import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC实现事务操作
 * spring-tx.account
 *
 */
public class JdbcTransactionExample {

    /**
     * 配置初始化与获取连接
     */
    public static Connection getConnection() throws SQLException {
        Properties connProps = new Properties();
        connProps.put("datasource", "jdbc:mysql://localhost:3306/spring-tx?useSSL=false");
        connProps.put("user", "root");
        connProps.put("password", "123456");
        return DriverManager.getConnection(connProps.getProperty("datasource"), connProps.getProperty("user"), connProps.getProperty("password"));
    }

    /**
     * 事务控制
     */
    //public transactionWrap() {
    //
    //}

    /**
     * 业务逻辑
     * aaa用户转给bbb用户100块钱
     */
    @MyTransactional
    public void pay(Connection conn) throws SQLException {
        PreparedStatement decreaseMoney = conn.prepareStatement("update account set money = money-100 where accountName = '111'");
        PreparedStatement increaseMoney = conn.prepareStatement("update account set money = money+100 where accountName = '222'");

        decreaseMoney.executeUpdate();
        int i=1/0;
        increaseMoney.executeUpdate();

        decreaseMoney.close();
        increaseMoney.close();
    }

    //CGLib Enhancer setInterfaces() 设置对哪些方法进行增强
    public void payWithoutTx(Connection conn) throws SQLException {
        PreparedStatement decreaseMoney = conn.prepareStatement("update account set money = money-100 where accountName = '111'");
        PreparedStatement increaseMoney = conn.prepareStatement("update account set money = money+100 where accountName = '222'");

        decreaseMoney.executeUpdate();
        increaseMoney.executeUpdate();

        decreaseMoney.close();
        increaseMoney.close();
    }

    //增强功能：事务控制
    //把事务控制全部放在增强里面
    public static class TransactionProxy implements MethodInterceptor {
        /**
         * @param object 原业务实例对象
         * @param method 原业务实例对象的业务逻辑方法，也是要增强的方法，基于反射调用
         * @param objects method的参数
         * @param methodProxy 针对原业务实例对象的方法代理
         * @return 业务执行结果
         * @throws Throwable
         */
        @Override
        public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println(method.getName());
            //从参数列表获取连接
            System.out.println("enhance begin");
            //执行事务处理
            Connection conn = null;
            if(objects.length == 1 && objects[0] instanceof Connection) {
                conn = (Connection) objects[0];
            }
            if(conn == null) {
                throw new NullPointerException("conn shouldn't be null");
            }
            try {
                //关闭自动提交
                conn.setAutoCommit(false);
                //反射执行业务操作,注意这里调用父类方法，即原业务对象；还要注意原方法中的异常要全部抛出来
                methodProxy.invokeSuper(object, objects);
                //全部正常执行则提交
                conn.commit();
            } catch (SQLException | RuntimeException e) {   //要回退的异常
                e.printStackTrace();
                if(conn != null) {
                    try {
                        System.out.println("rollback");
                        conn.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } finally {
                conn.setAutoCommit(true);
                System.out.println("enhance end");
            }

            return null;
        }
    }

    public static class TransactionFilter implements CallbackFilter {
        @Override
        public int accept(Method method) {
            if(method.getAnnotation(MyTransactional.class) != null) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = JdbcTransactionExample.getConnection();

        TransactionProxy txProxy = new TransactionProxy();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(JdbcTransactionExample.class);
        //enhancer.setCallback(txProxy);
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, txProxy});
        enhancer.setCallbackFilter(new TransactionFilter());

        JdbcTransactionExample enhancedExample = (JdbcTransactionExample) enhancer.create();
        enhancedExample.pay(conn);
        //enhancedExample.payWithoutTx(conn);

        conn.close();
    }
}
