package com.siewe.inventorymanagementsystem.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class userFrontendController {

    @RequestMapping(value = "/users")
    public String createUser(){
        return "static/createUser";
    }
}
