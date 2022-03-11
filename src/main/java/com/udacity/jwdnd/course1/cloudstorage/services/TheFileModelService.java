package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.TheFileModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheFileModelService {

    private FileMapper fileMapper;

    public TheFileModelService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public Integer insertFile(TheFileModel theFileModel){
        return fileMapper.insertFile(theFileModel);
    }

    public List<TheFileModel> getFilesForUser(Integer userId){
        return fileMapper.selectFilesForUser(userId);
    }




    public Integer deleteFile(String fileName){
        return fileMapper.deleteFile(fileName);
    }



    public boolean isFileDuplicate(Integer userId,String fileName){
        List<String> fileNameList = fileMapper.getAllFileNamesForUser(userId);
        return fileNameList.contains(fileName);
    }

    public TheFileModel getFileByNameForUser(Integer userId, String fileName){
        return fileMapper.getFileByNameForUser(userId,fileName);
    }
}
