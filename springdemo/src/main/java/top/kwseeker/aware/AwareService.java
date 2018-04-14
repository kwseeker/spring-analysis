package top.kwseeker.aware;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class AwareService implements BeanNameAware,ResourceLoaderAware {//1
	
	private String beanName;
	private ResourceLoader loader;

    /**
     * 实现ResourceLoaderAware接口需要重写setResourceLoader
     * @param resourceLoader
     */
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {//2
		this.loader = resourceLoader;
	}

    /**
     * 实现BeanNameAware接口需要重写setBeanName接口
     * @param name
     */
	@Override
	public void setBeanName(String name) {//3
		this.beanName = name;
	}
	
	public void outputResult(){
		System.out.println("Bean的名称： " + beanName);
		
		Resource resource = loader.getResource("classpath:test.txt");
		try{
			System.out.println("ResourceLoader加载文件内容: " + IOUtils.toString(resource.getInputStream()));
		}catch(IOException e){
			e.printStackTrace();
		}
	
	}

}
