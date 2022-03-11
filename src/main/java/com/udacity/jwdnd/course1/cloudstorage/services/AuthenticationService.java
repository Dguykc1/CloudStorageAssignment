package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.TheUsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {
    private TheUsersMapper theUsersMapper;
    private TheHashService theHashService;

    public AuthenticationService(TheUsersMapper theUsersMapper, TheHashService theHashService) {
        this.theUsersMapper = theUsersMapper;
        this.theHashService = theHashService;
    }



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String plainPassword = authentication.getCredentials().toString();

        UserModel userModel = theUsersMapper.getUser(username);
        if (userModel != null) {
            String hashedPassword = theHashService.getHashedValue(plainPassword, userModel.getSalt());
            if (userModel.getPassWord().equals(hashedPassword)) {
                return new UsernamePasswordAuthenticationToken(username, hashedPassword, new ArrayList<>());
            }
        }

        return null;


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public String getUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
