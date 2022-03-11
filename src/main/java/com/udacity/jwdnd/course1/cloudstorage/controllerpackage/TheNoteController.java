package com.udacity.jwdnd.course1.cloudstorage.controllerpackage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.TheNoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteModel;
import com.udacity.jwdnd.course1.cloudstorage.services.TheNoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.udacity.jwdnd.course1.cloudstorage.constant.ConstantMsgs.*;

@Controller
@RequestMapping
public class TheNoteController {
    private TheNoteService theNoteService;
    private UserService userService;

    public TheNoteController(TheNoteService theNoteService, UserService userService) {
        this.theNoteService = theNoteService;
        this.userService = userService;
    }

    @PostMapping("home/note")
    public String notePostRequest(@ModelAttribute NoteModel noteModel, RedirectAttributes redirectAttributes, Authentication authentication) {
        Logger logger = LoggerFactory.getLogger(TheNoteMapper.class);
        String note_err = null;
        String note_ok =null;
        Integer noteId=null;
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            noteModel.setUserid(userId);

            noteId= noteModel.getNoteid();

            logger.error("noteId="+ noteModel.getNoteid());
            if (noteId == null) {//new noteModel
                Integer rowAdded = theNoteService.insertNote(noteModel);
                logger.error("noteModel row added="+rowAdded.toString());

                if (rowAdded < 0) {
                    note_err = NOTE_ERR_CREATION_FAILURE;
                    logger.error("insert noteModel row<0");
                }
                else {
                    note_ok=NOTE_NEW_SUCCESS;
                    noteId= theNoteService.getLastNoteId();//newly inserted NoteModel definetly has the last noteid;
                }
            } else {//edit noteModel
                logger.error("updating noteModel");
                Integer rowUpdated = theNoteService.updateNote(noteModel);
                if(rowUpdated<0){
                    note_err = NOTE_ERR_UPDATE_FAILURE;
                }
                else {
                    note_ok=NOTE_EDIT_SUCCESS;
                    //noteId=noteModel.getNoteid(); //for edit, just use the input NoteId;
                }

            }
        }catch(Exception a){
            logger.error("noteModel exception");
            note_err=NOTE_ERR_INVALIDSESSION;
            logger.error(a.toString());
        }

        //handling msg (success or failure) attributes
        if(note_err==null) {redirectAttributes.addAttribute("opNoteOk",true); redirectAttributes.addAttribute("opNoteMsg",note_ok+" -ID:"+noteId.toString());}
        else {redirectAttributes.addAttribute("opNoteNotOk",true);redirectAttributes.addAttribute("opNoteMsg",note_err+" -ID:"+noteId.toString());}


        return("redirect:/home");
    }

    @GetMapping("/home/note/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, RedirectAttributes redirectAttributes){
        String note_err=null;
        String note_ok=null;
        int rowDeleted= theNoteService.deleteNote(noteId);
        if(rowDeleted<0)
            note_err=NOTE_DELETE_ERR;
        else
            note_ok=NOTE_DELETE_SUCCESS;

        //handling msg (success or failure) attributes
        if(note_err==null) {redirectAttributes.addAttribute("opNoteOk",true); redirectAttributes.addAttribute("opNoteMsg",note_ok+" -ID:"+noteId.toString());}
        else {redirectAttributes.addAttribute("opNoteNotOk",true);redirectAttributes.addAttribute("opNoteMsg",note_err+" -ID:"+noteId.toString());}

        return("redirect:/home");
    }

}
