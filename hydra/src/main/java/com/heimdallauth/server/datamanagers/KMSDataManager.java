package com.heimdallauth.server.datamanagers;

import com.heimdallauth.server.commons.models.hydra.JWKPrivateModel;
import com.nimbusds.jose.jwk.JWK;

import java.util.List;

public interface KMSDataManager {
    void storeJWK(String keyId, String encryptedJWK, String encryptedJWKThumbprint, String keyType);
    JWKPrivateModel getJWK(String keyId);
    void deleteJWK(String keyId);
    List<JWKPrivateModel> getAllJWKs();
}
