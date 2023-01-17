package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author yangzq80@gmail.com
 * @date 12/5/22
 */
@Component
@Slf4j
public class Devtools implements InitializingBean {


    @Override
    public void afterPropertiesSet() {
        log.info("guava-jar classLoader----------------: " + StringUtils.class.getClassLoader().toString());
        log.info("Devtools ClassLoader------1112121--------: " + this.getClass().getClassLoader().toString());
    }


}
