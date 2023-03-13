package com.example.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

/**
 * @author yangzq80@gmail.com
 * @date 3/10/23
 */
@Configuration
@Slf4j
public class CustomerBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        for (String name : beanDefinitionRegistry.getBeanDefinitionNames()) {


            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(name);

            if (name.contains("feign")) {
                System.out.println(name + " ********* " );
            }

            if ("org.springframework.cloud.openfeign.FeignClientFactoryBean".equalsIgnoreCase(beanDefinition.getBeanClassName())) {
//            if("org.springframework.cloud.openfeign.FeignClientSpecification".equalsIgnoreCase(beanDefinition.getBeanClassName())){


                //log.info(beanDefinition.getPropertyValues().toString());

                MutablePropertyValues mutablePropertyValues = beanDefinition.getPropertyValues();

//                //动态读取配置信息
//                String mappingService = getMappingService(mutablePropertyValues.get("name").toString());
//
//                if (mappingService==null){
//                    return;
//                }

                mutablePropertyValues.add("url", "dev.com");

                try {
                    Field field = AbstractBeanDefinition.class.getDeclaredField("propertyValues");
                    field.setAccessible(true);
                    field.set(beanDefinition, mutablePropertyValues);
                    field.setAccessible(false);
                    beanDefinitionRegistry.registerBeanDefinition(name, beanDefinition);

                } catch (Exception e) {
                    log.error("Customer register FeignClientFactoryBean error {}", e.getMessage());
                }
            }

        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
