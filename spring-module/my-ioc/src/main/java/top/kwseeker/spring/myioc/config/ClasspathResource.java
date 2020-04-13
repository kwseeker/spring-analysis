package top.kwseeker.spring.myioc.config;

import java.io.InputStream;

public class ClasspathResource implements Resource {

    private String location;

    public ClasspathResource(String location) {
        this.location = location;
    }

    @Override
    public InputStream getInputStream() {
        //读取本class所在classpath目录中名字为location的文件
        return this.getClass().getClassLoader().getResourceAsStream(location);
    }
}
