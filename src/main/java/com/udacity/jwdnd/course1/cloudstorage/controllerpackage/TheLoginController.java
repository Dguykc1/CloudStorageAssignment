package com.udacity.jwdnd.course1.cloudstorage.controllerpackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class TheLoginController {
    @GetMapping()
    public String loginView(Model model) {
        Logger logger= LoggerFactory.getLogger(TheLoginController.class);
        return "login";
    }
}
