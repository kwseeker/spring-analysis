package top.kwseeker.spring.utils.property;

import cn.hutool.core.util.XmlUtil;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import top.kwseeker.spring.utils.resource.DefaultResourceLoader;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtilTest {

    @Test
    public void testLoadProperties() throws IOException {
        Properties properties = PropertiesUtil.loadProperties("classpath:spring.properties");
        Assert.assertEquals(properties.getProperty("name"), "demo");
    }

    @Test
    public void testLoadXml() throws IOException {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Document doc = XmlUtil.readXML(resourceLoader.getResource("classpath:demo.xml").getInputStream());
        Element root = doc.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
    }
}