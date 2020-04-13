package top.kwseeker.spring.myioc.factory;

import top.kwseeker.spring.myioc.config.Resource;
import top.kwseeker.spring.myioc.config.XmlBeanDefinitionReader;

public class XmlBeanFactory extends DefaultListableBeanFactory {

    private XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(this);

    private Resource resource;

    public XmlBeanFactory(Resource resource) {
        this.resource = resource;
        loadBeanDefinitions();
    }

    public void loadBeanDefinitions() {
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
    }
}
