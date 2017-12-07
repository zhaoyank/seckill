package com.kaishengit.service.impl;

import com.alibaba.fastjson.JSON;
import com.kaishengit.entity.Account;
import com.kaishengit.entity.Product;
import com.kaishengit.entity.ProductExample;
import com.kaishengit.file.FileStore;
import com.kaishengit.job.UpdateProductInventoryJob;
import com.kaishengit.mapper.ProductMapper;
import com.kaishengit.service.ProductService;
import com.kaishengit.service.exception.ServiceException;
import org.joda.time.DateTime;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhao
 */
@Service
public class ProductServiceImpl implements ProductService {

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private FileStore fileStore;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 简单商品列表
     * @return
     */
    @Override
    public List<Product> findAll() {
        ProductExample productExample = new ProductExample();
        productExample.setOrderByClause("start_time asc");
        return productMapper.selectByExample(productExample);
    }

    /**
     * 根据id查询商品详情
     * @param id
     * @return
     */
    @Override
    public Product findById(Integer id) {
        Product product;
        try(Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get("product:" + id);
            if (json == null) {
                product = productMapper.selectByPrimaryKey(id);
                jedis.set("product:" + id, JSON.toJSONString(product));
            } else {
                product = JSON.parseObject(json, Product.class);
            }
        }
        return product;
    }

    /**
     * 保存
     * @param product
     * @param inputStream
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void save(Product product, InputStream inputStream) {
        try {
            String key = fileStore.uploadFile(inputStream);
            product.setImage(key);

            productMapper.insertSelective(product);

            // 在redis中添加库存的list
            Jedis jedis = jedisPool.getResource();
            for (int i = 0; i < product.getProductInventory(); i++) {
                jedis.lpush("product:" + product.getId() + ":inventory", String.valueOf(i));
            }

            //添加定时任务,在秒杀结束后更新数据库库存
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("productId", product.getId());
            JobDetail jobDetail = JobBuilder.newJob(UpdateProductInventoryJob.class)
                    .setJobData(dataMap)
                    .withIdentity(new JobKey("productId:"+product.getId(),"productInventory"))
                    .build();

            DateTime dateTime = new DateTime(product.getEndTime().getTime());

            // 拼接cron表达式 ("0 30 12 24 10 ? 2017")
            StringBuilder cron = new StringBuilder("0")
                    .append(" ")
                    .append(dateTime.getMinuteOfHour())
                    .append(" ")
                    .append(dateTime.getHourOfDay())
                    .append(" ")
                    .append(dateTime.getDayOfMonth())
                    .append(" ")
                    .append(dateTime.getMonthOfYear())
                    .append(" ? ")
                    .append(dateTime.getYear());
            // 设置触发器
            ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron.toString());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(scheduleBuilder)
                    .build();

            // 获得调度器,调度器根据任务和触发器添加定时任务
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传到七牛云异常");
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new ServiceException("添加定时任务异常");
        }
    }

    /**
     * 秒杀
     * @param id
     */
    @Override
    public void secKill(Integer id, Account account) {
        try(Jedis jedis = jedisPool.getResource()) {
            List<String> stringList = jedis.lrange("product:" + id + ":accountId", 0 , -1);
            if (stringList.contains(String.valueOf(account.getId()))) {
                throw new ServiceException("一人只能参与一次秒杀");
            }

            String result = jedis.lpop("product:" + id + ":inventory");
            jedis.lpush("product:" + id + ":accountId", String.valueOf(account.getId()));
            if (result == null) {
                logger.info("库存不足");
                throw new ServiceException("您来晚了");
            } else {
                logger.info("秒杀成功");
                // 秒杀成功, 修改redis中的product剩余数量
                Product product = JSON.parseObject(jedis.get("product:" + id), Product.class);
                product.setProductInventory(product.getProductInventory() - 1);
                jedis.set("product:" + id, JSON.toJSONString(product));

                // 秒杀成功, 将成功的信息送到队列
                jmsTemplate.send("product-queue", new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        Map<String, Integer> map = new HashMap<>();
                        map.put("productId", id);
                        map.put("accountId", account.getId());
                        String json = JSON.toJSONString(map);

                        TextMessage textMessage = session.createTextMessage(json);
                        return textMessage;
                    }
                });
            }
        }
    }
}
