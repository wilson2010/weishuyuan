package com.healife.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by healife-605 on 2016/11/28.
 */
@Controller
@RequestMapping("/heailfe")
public class LoginController {

    @RequestMapping("/login")
    public String toLogin(){
		//水水水水水水水啊手动阀手动阀
		System.out.println("11111111111111111111");
		
		//水水水水水水水啊手动阀手动阀
		System.out.println("11111111111111111111");
		
        return "login";
    }
}
