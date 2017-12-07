package com.kaishengit.service;

import com.kaishengit.entity.Account;
import com.kaishengit.entity.Product;

import java.io.InputStream;
import java.util.List;

/**
 * @author zhao
 */
public interface ProductService {

    /**
     * 简单商品列表
     * @return
     */
    List<Product> findAll();

    /**
     * 根据id查询商品详情
     * @param id
     * @return
     */
    Product findById(Integer id);

    /**
     * 保存
     * @param product
     * @param inputStream
     */
    void save(Product product, InputStream inputStream);

    /**
     * 秒杀
     * @param id
     */
    void secKill(Integer id, Account account);
}
