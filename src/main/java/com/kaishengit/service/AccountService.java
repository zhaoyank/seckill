package com.kaishengit.service;


import com.kaishengit.entity.Account;

/**
 * @author zhao
 */
public interface AccountService {

    Account findByName(String name);

}
