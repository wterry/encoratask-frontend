package com.skytouch.task.pagecontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages/products")
public class ProductPageController {

    @GetMapping("/list")
    public String viewProducts() {
        return "products";
    }

}
