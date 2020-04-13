package top.kwseeker.spring.myioc.config;

import lombok.AllArgsConstructor;
import org.dom4j.Document;
import top.kwseeker.spring.myioc.beandefinition.BeanDefinitionReader;
import top.kwseeker.spring.myioc.beandefinition.BeanDefinitionRegistry;
import top.kwseeker.spring.myioc.util.DocumentReader;

import java.io.InputStream;

@AllArgsConstructor
public class XmlBeanDefinitionReader implements BeanDefinitionReader {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    @Override
    public void loadBeanDefinitions(Resource resource) {
        InputStream inputStream = resource.getInputStream();
        Document document = DocumentReader.createDocument(inputStream);
        XmlBeanDefinitionDocumentReader documentReader = new XmlBeanDefinitionDocumentReader(beanDefinitionRegistry);
        documentReader.loadBeanDefinitions(document.getRootElement());
    }
}
