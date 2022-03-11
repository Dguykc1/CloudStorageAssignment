package com.udacity.jwdnd.course1.cloudstorage.controllerpackage;

import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.udacity.jwdnd.course1.cloudstorage.constant.ConstantMsgs.*;


@Controller
@RequestMapping("/signup")
public class TheSignupController {

    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(TheSignupController.class);

    public TheSignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showSignUpPage(@ModelAttribute("User") UserModel userModel, Model model){
        return "signup";
    }

    @PostMapping
    public String signUpNewUser(@ModelAttribute("User") UserModel userModel, Model model, RedirectAttributes redirectAttributes){
        logger.error("---Starting signupNewUser---");
        logger.error("Before Hash, userName="+ userModel.getUserName()+" passwd="+ userModel.getPassWord()+" salt="+ userModel.getSalt());
        logger.error("FirstName and Last name are"+ userModel.getFirstName()+" "+ userModel.getLastName());
        userService.hashUserPassword(userModel); //userModel object will be updated with hashed passwd and salt value
        logger.error("After Hash, userName="+ userModel.getUserName()+" passwd="+ userModel.getPassWord()+" salt="+ userModel.getSalt());


        String signup_err = null;
        if(!userService.isUsernameAvailable(userModel.getUserName()))
            signup_err=SIGNUP_USER_EXISTING_ERR;

        if(signup_err==null){
           int rowAdded=  userService.insertUser(userModel);
           if(rowAdded<0)
               signup_err =SIGNUP_ERR;
        }

        if(signup_err==null){
            redirectAttributes.addAttribute("isSuccess",true);
            redirectAttributes.addAttribute("signupMsg",SIGNUP_SUCCESS+ userModel.getUserName());
            return "redirect:/login";
        }
        else{
            model.addAttribute("isFailure",true);
            model.addAttribute("signupMsg",signup_err);
            }


       // logger.error("---Finishing signupNewUser---");
       return "signup";

    }
}
