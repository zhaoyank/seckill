package com.kaishengit.mq;

import com.alibaba.fastjson.JSON;
import com.kaishengit.entity.AccountProductKey;
import com.kaishengit.service.AccountProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * 从队列中获取成功秒杀的数据,放入数据库中
 * @author zhao
 */
@Component
public class OrderConsumer {

    private Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    private AccountProductService accountProductService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "product-queue", containerFactory = "jmsListenerContainerFactory")
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendSaleMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String json = textMessage.getText();
            Map<String, Integer> map = JSON.parseObject(json, HashMap.class);
            Integer productId = map.get("productId");
            Integer accountId = map.get("accountId");

            AccountProductKey accountProductKey = new AccountProductKey();
            accountProductKey.setAccountid(accountId);
            accountProductKey.setProductid(productId);
            accountProductService.save(accountProductKey);

            message.acknowledge();
            logger.info("账号:{}秒杀商品:{}", accountId, productId);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
