package com.kaishengit.controller;

import com.kaishengit.entity.Account;
import com.kaishengit.service.AccountService;
import com.kaishengit.service.exception.ServiceException;
import com.kaishengit.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author zhao
 */
@Controller
public class LoginController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public JsonResult login(String accountName, HttpSession session) {
        try {
            Account account = accountService.findByName(accountName);
            session.setAttribute("curr_account", account);

            return JsonResult.success(account);
        } catch (ServiceException ex) {
            return JsonResult.error(ex.getMessage());
        }
    }

}
