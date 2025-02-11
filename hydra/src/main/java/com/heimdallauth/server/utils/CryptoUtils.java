package com.heimdallauth.server.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.*;
import lombok.extern.slf4j.Slf4j;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;

@Slf4j
public class CryptoUtils {
    private static final String RSA_ALGORITHM = "RSA";
    private static final String EC_ALGORITHM = "EC";


    public static KeyPair generateRSAKeyPair(int keySize){
        try{
            KeyPairGenerator kpGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            kpGen.initialize(keySize);
            return kpGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static KeyPair generateECKeyPair(String curveName){
        try{
            KeyPairGenerator kpGen = KeyPairGenerator.getInstance(EC_ALGORITHM);
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(curveName);
            kpGen.initialize(ecSpec);
            return kpGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public static JWK convertToJWKPublic(KeyPair kp){
        JWK jwkWithPrivate = convertToJWKPrivate(kp);
        return jwkWithPrivate.toPublicJWK();
    }

    private static JWK convertToJWKPrivate(KeyPair keyPair){
        try{
            if(keyPair.getPrivate().getAlgorithm().equals(RSA_ALGORITHM)){
                return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                        .keyUse(KeyUse.SIGNATURE)
                        .privateKey((RSAPrivateKey) keyPair.getPrivate())
                        .keyIDFromThumbprint()
                        .build();
            }
            else if(keyPair.getPrivate().getAlgorithm().equals(EC_ALGORITHM)) {
                return new ECKey.Builder(Curve.forECParameterSpec(((ECPublicKey) keyPair.getPublic()).getParams()), (ECPublicKey) keyPair.getPublic())
                        .privateKey(keyPair.getPrivate())
                        .keyIDFromThumbprint()
                        .build();
            }else{
                throw new IllegalArgumentException("Unsupported key type");
            }
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
