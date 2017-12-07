package com.kaishengit.service.impl;

import com.kaishengit.entity.Account;
import com.kaishengit.entity.AccountExample;
import com.kaishengit.mapper.AccountMapper;
import com.kaishengit.service.AccountService;
import com.kaishengit.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhao
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Account findByName(String name) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andAccountNameEqualTo(name);
        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if (accountList.isEmpty()) {
            throw new ServiceException("用户名错误");
        }
        return accountList.get(0);
    }
}
