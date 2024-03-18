package com.acxfoxer.idaas.dataenanddecryptbootstarter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "idaas.encryptor")
public class DataEnCryptProperties {
    private String encryptPassword;
    private String algorithm;

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(String decryptPassword) {
        this.encryptPassword = decryptPassword;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
