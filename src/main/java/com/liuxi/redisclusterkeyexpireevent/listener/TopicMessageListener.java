package com.liuxi.redisclusterkeyexpireevent.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lenovo021
 * Date: 2018-09-19
 * Time: 10:09
 */
@Component
public class TopicMessageListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(TopicMessageListener.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        logger.info("收到过期消息,body={},channel={}",new String(message.getBody()),new String(message.getChannel()));

        byte[] body = message.getBody();// 请使用valueSerializer
        String itemValue = new String(body);

        logger.info("收到过期消息："+itemValue);
    }
}
