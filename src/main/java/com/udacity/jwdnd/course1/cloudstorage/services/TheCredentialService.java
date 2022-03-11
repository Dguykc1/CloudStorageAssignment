package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.TheCredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class TheCredentialService {
    private TheCredentialsMapper myTheCredentialsMapper;
    private TheEncryptionService myTheEncryptionService;

    Logger logger = LoggerFactory.getLogger(TheCredentialService.class);

    public TheCredentialService(TheCredentialsMapper theCredentialsMapper, TheEncryptionService theEncryptionService) {
        this.myTheCredentialsMapper = theCredentialsMapper;
        this.myTheEncryptionService = theEncryptionService;
    }


    public Integer createCredential(CredentialsModel credential){
        logger.error("Now ready to insert the credential in Service  "+credential.getUrl()+"  "+credential.getUrlUserName()+" "+credential.getUrlPassWord());
        return myTheCredentialsMapper.createCredential(credential);
    }

    public Integer updateCredential(CredentialsModel credential){
        //when update, encrypt the passwd first
        return myTheCredentialsMapper.updateCredential(credential);
    }


    //update a POJO credential from Form with it's key from DB
    public void updateCredentialWithKey(CredentialsModel credential){
        credential.setKey(myTheCredentialsMapper.getKey(credential.getCredentialId()));
    }

    public Integer deleteCredential(Integer credentialId){
        return myTheCredentialsMapper.deleteCredential(credentialId);
    }
    public List<CredentialsModel> getAllCredentials(){
        return myTheCredentialsMapper.getAllCredentials();
    }
    public List<CredentialsModel> getCredentialsForUser(Integer userId) {return myTheCredentialsMapper.getCredentialsForUser(userId);}

    public void encryptPassword(CredentialsModel credential){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        credential.setKey(encodedKey);

        String encryptedPassword = myTheEncryptionService.encryptValue(credential.getUrlPassWord(), encodedKey);

        credential.setUrlPassWord(encryptedPassword);
        return;

    }

    public void decryptPassword(CredentialsModel credential){
               credential.setUrlPassWord(myTheEncryptionService.decryptValue(credential.getUrlPassWord(),credential.getKey()));
               return;

    }

    public String decryptPassword(String key, String encrptedPwd){
        return myTheEncryptionService.decryptValue(encrptedPwd,key);
    }


    public String getKeyById(Integer id){ return myTheCredentialsMapper.getKey(id);}
    public Integer getLastCredentialId() {return myTheCredentialsMapper.getLastCredentialId();}

}
