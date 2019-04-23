package com.liuxi.redisclusterkeyexpireevent.config;

import com.liuxi.redisclusterkeyexpireevent.listener.TopicMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lenovo021
 * Date: 2018-09-19
 * Time: 10:09
 */
@Configuration
public class RedisMessageListenerContainerConfig {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private TopicMessageListener messageListener;

    @Bean
    public RedisMessageListenerContainer configRedisMessageListenerContainer(Executor executor) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置Redis的连接工厂
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        // 设置监听使用的线程池
        container.setTaskExecutor(executor);
        // 设置监听的Topic
        ChannelTopic channelTopic = new ChannelTopic("__keyevent@0__:expired");
        // 设置监听器
        container.addMessageListener(messageListener, channelTopic);
        return container;
    }

    @Primary
    @Bean // 配置线程池
    public Executor myTaskAsyncPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        //executor.setQueueCapacity(20);
        executor.setKeepAliveSeconds(300);
        //executor.setThreadNamePrefix(config.getThreadNamePrefix());

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
