package com.kaishengit.service.impl;

import com.kaishengit.entity.AccountProductKey;
import com.kaishengit.mapper.AccountProductMapper;
import com.kaishengit.service.AccountProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhao
 */
@Service
public class AccountProductServiceImpl implements AccountProductService {

    @Autowired
    private AccountProductMapper accountProductMapper;

    @Override
    public void save(AccountProductKey accountProductKey) {
        accountProductMapper.insert(accountProductKey);
    }
}
