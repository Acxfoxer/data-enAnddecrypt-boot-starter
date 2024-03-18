package com.acxfoxer.idaas.dataenanddecryptbootstarter.jasypt;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;
import org.apache.commons.lang3.StringUtils;

public class MyEncryptablePropertyDetector implements EncryptablePropertyDetector {
    private String prefix = "AIDSREN(";
    private String suffix = ")";
    public MyEncryptablePropertyDetector() {

    }
    public MyEncryptablePropertyDetector(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }
    @Override
    public boolean isEncrypted(String message) {
        if(StringUtils.isBlank(message)){
            return false;
        }
        String trimValue = message.trim();
        return trimValue.startsWith(this.prefix) && trimValue.endsWith(this.suffix);
    }
    @Override
    public String unwrapEncryptedValue(String message) {
        return message;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
