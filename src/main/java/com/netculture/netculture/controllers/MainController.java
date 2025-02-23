package com.netculture.netculture.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/*, /index")
    public String initial(Model m) {
        return "index";
    }
    @RequestMapping("/loginVendedor")
    public String loginVendedor(Model m) {
        return "loginVendedor";
    }
    @RequestMapping("/loginComprador")
    public String loginComprador(Model m) {
        return "loginComprador";
    }   
    
}
