package com.heimdallauth.server.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
@Slf4j
public class RandomIdGeneratorUtil {
    private static final String RANDOMIZATION_ALGORITHM = "SHA1PRNG";
    private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int ID_LENGTH = 32;
    private static SecureRandom RANDOM;
    static {
        try{
            RANDOM = SecureRandom.getInstance(RANDOMIZATION_ALGORITHM);
        }catch (NoSuchAlgorithmException e){
            log.error("Error while creating random id generator", e);
        }
    }

    public static String generateRandomServerId(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < ID_LENGTH; i++){
            stringBuilder.append(CHARSET.charAt(RANDOM.nextInt(CHARSET.length())));
        }
        return stringBuilder.toString();
    }
}
