package com.hoperun.framework;

import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:spring-*.xml"})
public abstract class SpringTest<T> extends AbstractJUnit4SpringContextTests {
     
   public <T> T getBean(Class<T> type){
    return applicationContext.getBean(type);
   }
    
   public T getBean(String beanName){
    return (T)applicationContext.getBean(beanName);
   }
   protected ApplicationContext getContext(){
    return applicationContext;
   }
}

