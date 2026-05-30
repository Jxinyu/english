package com.ljb.english.controller;

import com.ljb.english.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
public class ViewsController {

    /**
     * 登录
     *
     * @return {@link String}
     */
    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    /**
     * 注销
     *
     * @return {@link R}
     */
    @GetMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/index";
    }

    @GetMapping({"/toIndex", "/index"})
    public String toIndex(){
        return "index";
    }

    @GetMapping("/toStart")
    public String toStartEnglish(){
        return "start_english";
    }

    /**
     * 回顾
     * /toReview/today  每日复习
     * /toReview/sign  回顾标记
     * /toReview/all  回顾所有
     *
     * @return {@link String}
     */
    @GetMapping("/toReview/{choice}")
    public String toReview(@PathVariable("choice") String choice, Model model){

        model.addAttribute("choice", choice);
        return "review";
    }

    @GetMapping("/english/self")
    public String self(){
        return "self";
    }

}
