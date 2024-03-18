package com.acxfoxer.idaas.dataenanddecryptbootstarter.util;

public class CommonTool {
    public static String getMessage(String message,String prefix,String suffix){
        int prefixIndex = message.indexOf(prefix);
        int suffixIndex = message.lastIndexOf(suffix);
        return message.substring(prefixIndex+prefix.length(),suffixIndex);
    }
}
