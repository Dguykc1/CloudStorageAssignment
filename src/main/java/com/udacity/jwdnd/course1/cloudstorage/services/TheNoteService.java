package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.TheNoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheNoteService {

    private TheNoteMapper theNoteMapper;
    private Logger logger= LoggerFactory.getLogger(TheNoteService.class);

    public TheNoteService(TheNoteMapper theNoteMapper) {
        this.theNoteMapper = theNoteMapper;
    }

    public Integer insertNote(NoteModel noteModel){
        logger.error("TheNoteService: ready to insert noteModel");
        return theNoteMapper.insertNote(noteModel);
    }

    public List<NoteModel> getNotesForUser(Integer userId){
        logger.error("TheNoteService: ready to getNotesForUser");
        return theNoteMapper.getNotesForUser(userId);
    }

    public Integer updateNote(NoteModel noteModel){
        return theNoteMapper.updateNote(noteModel);
    }

    public Integer deleteNote(Integer noteid){
        return theNoteMapper.deleteNote(noteid);
    }

    public Integer getLastNoteId() {return theNoteMapper.getLastNoteId();}

}
