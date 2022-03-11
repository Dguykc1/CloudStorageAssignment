package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface TheUsersMapper {
    @Select("SELECT * FROM USERS WHERE username=#{userName}")
    UserModel getUser(String userName);


    @Insert("Insert INTO USERS(username, salt, password, firstname, lastname) VALUES(#{userName},#{salt},#{passWord},#{firstName},#{lastName})")
    @Options(useGeneratedKeys= true, keyProperty="userId")
    int createUser(UserModel userModel);

    @Select("SELECT * FROM USERS")
    ArrayList<UserModel> getAllUsers();

}
