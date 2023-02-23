package com.zjtlcb.one.onedp.cloud.seata.configura;

import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yangzq80@gmail.com
 * @date 2/21/23
 * <p>
 * SEATA提供的Saga模式是基于状态机引擎来实现的，提供db存储的状态机默认配置
 */
@Configuration
@ConditionalOnProperty(name = "onedp.cloud.saga.enabled", matchIfMissing = false)
public class SagaModeAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SagaModeAutoConfiguration.class);

    @Bean
    public ThreadPoolExecutorFactoryBean threadExecutor() {
        ThreadPoolExecutorFactoryBean threadExecutor = new ThreadPoolExecutorFactoryBean();
        threadExecutor.setThreadNamePrefix("SAGA_ASYNC_EXE_");
        threadExecutor.setCorePoolSize(1);
        threadExecutor.setMaxPoolSize(20);
        return threadExecutor;
    }

    @Bean
    public DbStateMachineConfig dbStateMachineConfig(ThreadPoolExecutorFactoryBean threadExecutor, DataSource hikariDataSource) throws IOException {
        DbStateMachineConfig dbStateMachineConfig = new DbStateMachineConfig();
        dbStateMachineConfig.setDataSource(hikariDataSource);
        dbStateMachineConfig.setThreadPoolExecutor((ThreadPoolExecutor) threadExecutor.getObject());
        dbStateMachineConfig.setResources(new String[]{"classpath*:statelang/*.json"});
        dbStateMachineConfig.setEnableAsync(true);
        dbStateMachineConfig.setApplicationId("dbStateMachineApp");
        dbStateMachineConfig.setTxServiceGroup("default_tx_group");
        log.info("SagaModeAutoConfiguration ... appID[{}] classpath[{}] txGroup[{}]", "dbStateMachineApp", "classpath*:statelang/*.json", "default_tx_group");
        return dbStateMachineConfig;
    }

    @Bean
    public ProcessCtrlStateMachineEngine stateMachineEngine(DbStateMachineConfig dbStateMachineConfig) {
        ProcessCtrlStateMachineEngine stateMachineEngine = new ProcessCtrlStateMachineEngine();
        stateMachineEngine.setStateMachineConfig(dbStateMachineConfig);
        return stateMachineEngine;
    }

    @Bean
    public StateMachineEngineHolder stateMachineEngineHolder(ProcessCtrlStateMachineEngine stateMachineEngine) {
        StateMachineEngineHolder stateMachineEngineHolder = new StateMachineEngineHolder();
        stateMachineEngineHolder.setStateMachineEngine(stateMachineEngine);
        return stateMachineEngineHolder;
    }
}
