package com.chao.controller;

import com.chao.consts.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sun.reflect.generics.repository.MethodRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String logined(HttpServletRequest request, @RequestParam("username") String username) {
        HttpSession session = request.getSession();
        session.setAttribute(Constants.DEFAULT_SESSION_USERNAME, username);
        return "redirect:index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
