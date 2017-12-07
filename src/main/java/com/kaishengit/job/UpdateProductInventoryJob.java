package com.kaishengit.job;

import com.alibaba.fastjson.JSON;
import com.kaishengit.entity.Product;
import com.kaishengit.mapper.ProductMapper;
import com.kaishengit.service.exception.ServiceException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 秒杀结束后, 定时任务更新数据库中的库存数量
 * @author zhao
 */
public class UpdateProductInventoryJob implements Job {

    private Logger logger = LoggerFactory.getLogger(UpdateProductInventoryJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Integer productId = (Integer) jobDataMap.get("productId");

        try {
            ApplicationContext applicationContext = (ApplicationContext) jobExecutionContext
                    .getScheduler()
                    .getContext()
                    .get("springApplicationContext");
            JedisPool jedisPool = (JedisPool) applicationContext.getBean("jedisPool");
            ProductMapper productMapper = (ProductMapper) applicationContext.getBean("productMapper");

            Jedis jedis = jedisPool.getResource();
            Product product = JSON.parseObject(jedis.get("product:" + productId), Product.class);

            productMapper.updateByPrimaryKeySelective(product);
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            throw new ServiceException("定时任务设置异常");
        }

        logger.info("更新商品{}库存成功", productId);
    }
}
