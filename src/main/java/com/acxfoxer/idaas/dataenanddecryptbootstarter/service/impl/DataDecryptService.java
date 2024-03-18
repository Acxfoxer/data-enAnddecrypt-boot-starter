package com.acxfoxer.idaas.dataenanddecryptbootstarter.service.impl;

import com.acxfoxer.idaas.dataenanddecryptbootstarter.properties.DataDeCryptProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 获取业务类
 */
public class DataDecryptService {

    private DataDeCryptProperties dataDeCryptProperties;
    public DataDecryptService() {

    }
    public DataDecryptService(DataDeCryptProperties dataDeCryptProperties) {
        this.dataDeCryptProperties = dataDeCryptProperties;
    }
}
