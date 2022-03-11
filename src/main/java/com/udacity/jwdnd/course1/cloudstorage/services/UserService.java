package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.TheUsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Random;


@Service
public class UserService {
    private TheUsersMapper theUsersMapper;
    private TheHashService theHashService;

    public UserService(TheUsersMapper theUsersMapper, TheHashService theHashService) {
        this.theUsersMapper = theUsersMapper;
        this.theHashService = theHashService;
    }



    public int insertUser(UserModel userModel){
        return theUsersMapper.createUser(userModel);
    }

    public UserModel getUser(String userName){
        return theUsersMapper.getUser(userName);
    }


    public boolean isUsernameAvailable(String userName){
        return getUser(userName)==null;
    }

    public void hashUserPassword(UserModel userModel){
        Random random = new Random();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = theHashService.getHashedValue(userModel.getPassWord(), encodedSalt);

        userModel.setSalt(encodedSalt);
        userModel.setPassWord(hashedPassword);

    }
}
