package com.acxfoxer.idaas.dataenanddecryptbootstarter.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CustomGetCryptorService implements com.acxfoxer.idaas.dataenanddecryptbootstarter.service.CustomGetCryptorService {
    private static final Log log = LogFactory.getLog(CustomGetCryptorService.class);
    @Override
    public String getRemoteCryptor(String url, String key) {
        // todo 自定义远程获取密钥
        log.info("starter获取远程密钥失败");
        return null;
    }
}
