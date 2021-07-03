package top.kwseeker.spring.ioc.initialize;

import javassist.*;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.*;
import org.junit.Test;
import sun.misc.ProxyGenerator;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

public class CglibUsageTest {

    /**
     * 最主要的是Enhancer MethodInterceptor
     */
    @Test
    public void testSimplestCglibEnhance() throws Exception {
        //动态代理类输出到文件
        //-Dsun.misc.ProxyGenerator.saveGeneratedFiles=true
        //System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/tmp/spring");

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SomeService.class);                              //增强哪个类
        //enhancer.setNamingPolicy();
        enhancer.setInterceptDuringConstruction(false);                         //不对构造方法进行拦截.默认false
        //enhancer.setCallback(new LogInterceptor());                           //类方法增强逻辑
        //方式1
        enhancer.setCallbacks(new Callback[]{new LogInterceptor(), new ExecuteTimeInterceptor()});
        enhancer.setCallbackFilter(new SomeServiceFilter());
        SomeService serviceProxy = (SomeService) enhancer.create();
        //方式2
        //enhancer.setCallbackTypes(new Class[]{LogInterceptor.class, ExecuteTimeInterceptor.class});
        //enhancer.setCallbackFilter(new SomeServiceFilter());
        //Class<?> proxyClass = enhancer.createClass();
        //SomeService serviceProxy = (SomeService) proxyClass.getDeclaredConstructor().newInstance();     //TODO 为什么这么写只有一个代理类,没有生成那两个Fast类
        //方式1 生成了3个类，方式2 只有下面第一个（没有Fast类）
        // ├── CglibUsageTest$SomeService$$EnhancerByCGLIB$$a60cd3c7.class
        // ├── CglibUsageTest$SomeService$$EnhancerByCGLIB$$a60cd3c7$$FastClassByCGLIB$$d21336b2.class
        // └── CglibUsageTest$SomeService$$FastClassByCGLIB$$bbbfb15f.class

        //使用javassist输出
        //outputProxyClass(serviceProxy.getClass());
        //outputProxyClass2(serviceProxy.getClass());

        serviceProxy.doService();
        serviceProxy.doService2();
        serviceProxy.getUserInfo();         //TODO 为何带返回值的代理类方法调用会报 cast 类型失败, 错误信息还很奇怪
                                            //java.lang.ClassCastException: top.kwseeker.spring.ioc.initialize.CglibUsageTest$SomeService$$EnhancerByCGLIB$$a60cd3c7 cannot be cast to top.kwseeker.spring.ioc.initialize.User
    }

    /**
     * TODO 为何 javassist 获取 cglib动态生成的代理类 失败
     */
    private void outputProxyClass(Class<?> proxyClass) throws NotFoundException, CannotCompileException, IOException {
        String proxyClassName = proxyClass.getName();
        System.out.println(proxyClassName);

        ClassPool classPool = ClassPool.getDefault();
        //classPool.insertClassPath(new ClassClassPath(proxyClass));
        //classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = classPool.get(proxyClassName);
        ctClass.writeFile("output");
    }

    private void outputProxyClass2(Class<?> proxyClass) throws IOException {
        String proxyClassName = proxyClass.getName();
        System.out.println(proxyClassName);
        //只能生成实现接口的类的代理类的字节码，这个类属于 Java rt.jar
        byte[] bytes = ProxyGenerator.generateProxyClass(proxyClassName, proxyClass.getSuperclass().getInterfaces());

        String fileDir = getClass().getResource("").getPath();
        System.out.println(fileDir);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(fileDir + "/" + proxyClassName + ".class", false));
        out.write(bytes);
        out.flush();
        out.close();
    }

    static class SomeServiceFilter implements CallbackFilter {
        @Override
        public int accept(Method method) {
            if ("getUserInfo".equals(method.getName())) {
                return 1;
            }
            return 0;
        }
    }

    static class LogInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object object, Method method, Object[] objects, MethodProxy proxy) throws Throwable {
            System.out.printf("Before %s invoke ...%n", method.getName());
            proxy.invokeSuper(object, objects);
            System.out.printf("After %s Invoke ...%n", method.getName());
            return object;
        }
    }

    static class ExecuteTimeInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object object, Method method, Object[] objects, MethodProxy proxy) throws Throwable {
            long startTs = System.currentTimeMillis();
            proxy.invokeSuper(object, objects);
            System.out.printf("%s execute time: %d", method.getName(), System.currentTimeMillis() - startTs);
            return object;
        }
    }

    static class SomeService {

        public void doService() {
            System.out.println("service 1 ...");
        }

        public void doService2() {
            System.out.println("service 2 ...");
        }

        public User getUserInfo() {
            return new User("Arvin", 18);
        }
    }
}
