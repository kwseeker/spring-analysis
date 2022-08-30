package top.kwseeker.spring.annotation.anno01;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainApplicationTest {

    @Test
    public void testClassPresent() {
        boolean jsr250Present = ClassUtils.isPresent("javax.annotation.Resource", AnnotationConfigUtils.class.getClassLoader());
        Assert.assertTrue(jsr250Present);
        //此测试中后面一个条件不满足
        boolean jpaPresent = ClassUtils.isPresent("javax.persistence.EntityManagerFactory", AnnotationConfigUtils.class.getClassLoader()) &&
                ClassUtils.isPresent("org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor", AnnotationConfigUtils.class.getClassLoader());
        Assert.assertFalse(jpaPresent);
    }

    @Test
    public void testPattern() {
        String location = "classpath*:///**/*.class";
        int prefixEnd = location.indexOf(':') + 1;
        int rootDirEnd = location.length();
        while (rootDirEnd > prefixEnd && isPattern(location.substring(prefixEnd, rootDirEnd))) {
            rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
        }
        if (rootDirEnd == 0) {
            rootDirEnd = prefixEnd;
        }
        System.out.println(location.substring(0, rootDirEnd));
    }

    @Test
    public void testGetResourceByPath() throws IOException {
        String path = "//";       //成功
        //String path = "///";      //失败
        //String path = "/";          //失败
        //String path = "top/kwseeker/spring/annotation/anno01/";  //成功
        //String path = "top/kwseeker/spring/annotation/anno01";  //成功

        Set<Resource> result = new LinkedHashSet<>(16);
        ClassLoader cl = MainApplicationTest.class.getClassLoader();
        Enumeration<URL> resourceUrls = cl != null ? cl.getResources(path) : ClassLoader.getSystemResources(path);  //TODO getResource("//") JDK是怎么处理的？ 有空了梳理下getResource底层原理
                                                                                                                    //暂时参考：https://blog.csdn.net/qq_29328443/article/details/114606660
        while(resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            result.add(new UrlResource(url));
        }

        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testGenerateBeanName() {
        //将字符串专为Java变量风格
        String beanName = Introspector.decapitalize("BaseService");
        String url = Introspector.decapitalize("URL");
        Assert.assertEquals("baseService", beanName);
        Assert.assertEquals("URL", url);
    }

    /**
     * 对象默认的hashCode()就是取的identityHashCode(), JDK8默认使用线程状态通过 Marsaglia's xor-shift 在首次使用使用identity hash code时计算得到；
     * 即JDK8默认的hashCode()和对象内存中的地址没有关系。
     * 特点是生成速度更快占用空间更少。
     * 由于 identityHashCode 可能存在hash冲突，所以两个对象的 identityHashCode 相等并不能确保两个对象一定相等，但是 identityHashCode 不相等可以确保两个对象一定不相等。
     * 作用：加速对象不等判断，先通过identityHashCode判断如果不相等则对象一定不想等，相等的话再通过“==”判断，identityHashCode相等判断币比对象“==”判断性能高的多
     */
    @Test
    public void testGenerateIdentityHexString() {
        Object obj = new Object();
        int ihc = System.identityHashCode(obj);
        System.out.println(ihc);
        System.out.println(obj.hashCode());
        String identity = Integer.toHexString(System.identityHashCode(ihc));
        System.out.println(identity);
        //String 重写了Object的hashCode()方法
        String strObj = "hello";
        System.out.println(System.identityHashCode(strObj) + "---" + strObj.hashCode());
    }

    public boolean isPattern(String path) {
        return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
    }
}