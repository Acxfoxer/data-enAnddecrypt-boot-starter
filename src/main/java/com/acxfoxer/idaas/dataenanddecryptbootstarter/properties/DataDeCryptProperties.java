package com.acxfoxer.idaas.dataenanddecryptbootstarter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "idaas.decryptor")
public class DataDeCryptProperties {

    private String url;
    private int decryptPassword;
    private String customGetCryptorClass;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getDecryptPassword() {
        return decryptPassword;
    }
    public void setDecryptPassword(int decryptPassword) {
        this.decryptPassword = decryptPassword;
    }
    public String getCustomGetCryptorClass() {
        return customGetCryptorClass;
    }
    public void setCustomGetCryptorClass(String customGetCryptorClass) {
        this.customGetCryptorClass = customGetCryptorClass;
    }
}

