package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TheNoteMapper {
    @Insert("INSERT INTO NOTES(notetitle,notedescription,userid)  VALUES(#{noteTitle},#{noteDescription},#{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    Integer insertNote(NoteModel noteModel);

    @Select("Select * from NOTES WHERE userid = #{userid}")
    List<NoteModel> getNotesForUser(Integer userid);

    //looks like mybatis is updating all columns, even some coloumn is not given in SET
    @Update("UPDATE NOTES SET notetitle=#{noteTitle},notedescription=#{noteDescription},userid=#{userid} WHERE noteid=#{noteid}")
    Integer updateNote(NoteModel noteModel);

    @Delete("DELETE FROM NOTES WHERE noteid=#{noteid}")
    Integer deleteNote(Integer noteid);

    @Select("Select max(noteid) from NOTES")
    Integer getLastNoteId();
}
