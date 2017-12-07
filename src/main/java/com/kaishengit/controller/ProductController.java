package com.kaishengit.controller;

import com.kaishengit.entity.Account;
import com.kaishengit.entity.Product;
import com.kaishengit.service.ProductService;
import com.kaishengit.service.exception.ServiceException;
import com.kaishengit.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zhao
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String home(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);
        return "home";
    }

    @GetMapping("/new")
    public String newProduct() {
        return "new";
    }

    @PostMapping("/new")
    public String newProduct(Product product, MultipartFile file,String sTime, String eTime)
            throws ParseException, IOException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startTime = simpleDateFormat.parse(sTime);
        Date endTime = simpleDateFormat.parse(eTime);

        product.setStartTime(startTime);
        product.setEndTime(endTime);

        productService.save(product, file.getInputStream());

        return "redirect:/product";
    }

    @GetMapping("/{id:\\d+}")
    public String show(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product",product);
        return "product";
    }

    @GetMapping("/seckill/{id:\\d+}")
    @ResponseBody
    public JsonResult secKill(@PathVariable Integer id, HttpSession session) {

        Account account = (Account) session.getAttribute("curr_account");
        if (account == null) {
            return JsonResult.error("登录后才能参加秒杀");
        }

        try {
            productService.secKill(id, account);
            return JsonResult.success();
        } catch (ServiceException ex) {
            return JsonResult.error(ex.getMessage());
        }
    }

}
