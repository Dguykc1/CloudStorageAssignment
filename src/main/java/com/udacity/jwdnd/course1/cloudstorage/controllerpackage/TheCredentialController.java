package com.udacity.jwdnd.course1.cloudstorage.controllerpackage;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsModel;
import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.TheCredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.udacity.jwdnd.course1.cloudstorage.constant.ConstantMsgs.*;

@Controller
public class TheCredentialController {
    private TheCredentialService credentialFormService;
    private UserService userFormService;
    private AuthenticationService authenticationFormService;

    private Logger logger = LoggerFactory.getLogger(TheCredentialController.class);

    @Autowired
    public TheCredentialController(TheCredentialService theCredentialService, UserService userService, AuthenticationService authenticationService) {
        this.credentialFormService = theCredentialService;
        this.userFormService = userService;
        this.authenticationFormService = authenticationService;
    }

    @PostMapping("home/credentials")
    public String createCredential(@ModelAttribute CredentialsModel credential, RedirectAttributes redirectAttributes){
        logger.error("---Begining Credential Controller----");
        String credential_err=null;
        String credential_ok=null;
        String userName=authenticationFormService.getUserName();
        Integer credId=credential.getCredentialId();

        if(userName==null)
            credential_err = CREDENTIAL_INVALIDSESSION_ERR;

        int userId=-1;
        if(credential_err==null) {
            UserModel userModel = userFormService.getUser(userName);
            if(userModel !=null)
               credential.setUserId(userModel.getUserId());
            else
                credential_err = CREDENTIAL_INVALIDSESSION_ERR;
        }

        //Integer credId=credential.getCredentialId();
        if(credential_err==null){//create new credential
            if(credId==null) {
                logger.error("Now ready to insert the credential  " + credential.getUrl() + "  " + credential.getUrlUserName() + " " + credential.getUrlPassWord());
                credentialFormService.encryptPassword(credential);
                int rowAdded = credentialFormService.createCredential(credential);
                if (rowAdded < 0) {
                    logger.error("TheCredentialController: insert failed");
                    credential_err = CREDENTIAL_CREATE_ERR;
                }else{
                    credId= credentialFormService.getLastCredentialId();//newly inserted is always the last one
                    credential_ok=CREDENTIAL_CREATE_SUCCESS;
                }
            }
            else {
                //now the POJO credential is from the form, not from DB, so below line encrptPssword won't work
                // credentialService.encryptPassword(credential);

                credentialFormService.updateCredentialWithKey(credential);
                credentialFormService.encryptPassword(credential);

                int rowUpdated = credentialFormService.updateCredential(credential);
                if(rowUpdated<0)
                    credential_err = CREDENTIAL_UPDATE_ERR;
                else
                    credential_ok=CREDENTIAL_UPDATE_SUCCESS;
            }
        }

        //redirectAttributes.addAttribute("credentials", credentialService.getAllCredentials());
        //handling msg (success or failure) attributes
        if(credential_err==null) {redirectAttributes.addAttribute("opCredOk",true); redirectAttributes.addAttribute("opCredMsg",credential_ok+" -ID:"+credId.toString());}
        else {redirectAttributes.addAttribute("opCredNotOk",true);redirectAttributes.addAttribute("opCredMsg",credential_err+" -ID:"+credId.toString());}

        return("redirect:/home");
    }

    //Delete Credential, but have to use GET?
    @GetMapping("/home/credentials/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId,
                                   RedirectAttributes redirectAttributes){

        String credential_err=null;
        String credential_ok=null;

        int rowDeleted=credentialFormService.deleteCredential(credentialId);
        if(rowDeleted<0)
            credential_err=CREDENTIAL_DELETE_ERR;
        else
            credential_ok=CREDENTIAL_DELETE_SUCCESS;

        //handling msg (success or failure) attributes
        if(credential_err==null) {redirectAttributes.addAttribute("opCredOk",true); redirectAttributes.addAttribute("opCredMsg",credential_ok+" -ID:"+credentialId.toString());}
        else {redirectAttributes.addAttribute("opCredNotOk",true);redirectAttributes.addAttribute("opCredMsg",credential_err+" -ID:"+credentialId.toString());}

        return("redirect:/home");
    }
}
