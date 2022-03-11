package com.udacity.jwdnd.course1.cloudstorage.controllerpackage;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsModel;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteModel;
import com.udacity.jwdnd.course1.cloudstorage.model.TheFileModel;
import com.udacity.jwdnd.course1.cloudstorage.services.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/home")
public class TheHomeController {

    private TheCredentialService theCredentialService;
    private UserService userService;
    private TheNoteService theNoteService;
    private TheFileModelService fileModelService;
    private TheEncryptionService theEncryptionService;

    private Logger logger = LoggerFactory.getLogger(TheHomeController.class);

    public TheHomeController(TheCredentialService theCredentialService, UserService userService,
                             TheNoteService theNoteService, TheFileModelService fileModelService, TheEncryptionService theEncryptionService) {
        this.theCredentialService = theCredentialService;
        this.userService=userService;
        this.theNoteService = theNoteService;
        this.fileModelService=fileModelService;
        this.theEncryptionService = theEncryptionService;
    }

    @GetMapping
    public String getHome(Model model, Authentication authentication){
        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();

        logger.error("at HOME");
        //CredentialsModel
        List<CredentialsModel> credentialsModelList = theCredentialService.getCredentialsForUser(currentUserId);

        Iterator<CredentialsModel> iterator= credentialsModelList.iterator();
        while(iterator.hasNext()){
            CredentialsModel cren=iterator.next();
            logger.error("username="+cren.getUrlUserName());
            logger.error("cryptedpassword="+cren.getUrlPassWord());
            logger.error("url="+cren.getUrl());
            logger.error("crenid="+cren.getCredentialId().toString());
            logger.error("userid="+cren.getUserId().toString());
            //credentialService.decryptPassword(cren);
            //logger.error("decrypted password="+cren.getUrlPassWord());
        }

        model.addAttribute("encryptionService", theEncryptionService);
        model.addAttribute("credentials", credentialsModelList);

        //Notes
        logger.error("Home:ready to add note attributes");
        List<NoteModel> notesList= theNoteService.getNotesForUser(currentUserId);
        Iterator<NoteModel> iterator1=notesList.iterator();
        while(iterator1.hasNext()){
            NoteModel n1= iterator1.next();
            logger.error("at Home, from DB query noteid="+n1.getNoteid().toString());
        }

        model.addAttribute("noteslist",notesList);

        //Files
        List<TheFileModel> theFileModelList = fileModelService.getFilesForUser(currentUserId);

        model.addAttribute("fileList", theFileModelList);

        return "home";
    }
}
