package com.acxfoxer.idaas.dataenanddecryptbootstarter.jasypt;

import com.acxfoxer.idaas.dataenanddecryptbootstarter.Comstant.JasyptConstant;
import com.acxfoxer.idaas.dataenanddecryptbootstarter.properties.DataDeCryptProperties;
import com.acxfoxer.idaas.dataenanddecryptbootstarter.properties.DataEnCryptProperties;
import com.acxfoxer.idaas.dataenanddecryptbootstarter.service.impl.CustomGetCryptorService;
import com.acxfoxer.idaas.dataenanddecryptbootstarter.service.impl.DataDecryptService;
import com.acxfoxer.idaas.dataenanddecryptbootstarter.util.CommonTool;
import com.acxfoxer.idaas.dataenanddecryptbootstarter.util.IdaasKaiserUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DefaultEncryptor implements StringEncryptor {

    private static final Log log = LogFactory.getLog(DefaultEncryptor.class);

    private final DataDecryptService dataDecryptService;
    private final MyEncryptablePropertyDetector propertyDetector;
    @Resource
    private DataDeCryptProperties dataDeCryptProperties;
    @Resource
    private DataEnCryptProperties dataEnCryptProperties;
    private String innerPrefix = "AEN(";
    private String innerSuffix = ")";
    public static final String COLON = "::";

    public DefaultEncryptor(DataDecryptService dataDecryptService, MyEncryptablePropertyDetector propertyDetector) {
        this.propertyDetector = propertyDetector;
        this.dataDecryptService = dataDecryptService;
    }


    /**
     * 解密
     * @param message
     * @return
     */
    @Override
    public String decrypt(String message) {
        String prefix = propertyDetector.getPrefix();
        String suffix = propertyDetector.getSuffix();
        message = CommonTool.getMessage(message, prefix, suffix);
        String result = "";
        if (StringUtils.isNotBlank(message)) {
            result = getDecryptOrRemoteKey(message);
        }
        return result;
    }
    /**
     * 加密
     * @param message
     * @return
     */
    public String encrypt(String message){
//        String prefix = propertyDetector.getPrefix();
//        String suffix = propertyDetector.getSuffix();
//        message = CommonTool.getMessage(message, prefix, suffix);
        String encryptPWD="";
        if(StringUtils.isNotBlank(message)){
            encryptPWD=getEncryptPWD(message);
        }
        return encryptPWD;
    }

    private String getEncryptPWD(String message) {
        //判断时用默认加密还是凯撒加密算法
        String algorithm = dataEnCryptProperties.getAlgorithm();
        String pwd = dataEnCryptProperties.getEncryptPassword();
        int key;
        try{
            key=Integer.parseInt(pwd);
        }catch (Exception e){
            log.warn("password is not number");
            key=pwd.length()<<2;
        }
        if(algorithm.equals(JasyptConstant.KAISER)){
            return IdaasKaiserUtil.encryptKaiser(message,key);
        }else if(algorithm.equals(JasyptConstant.PBEWithMD5AndDES)) {
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword(pwd);
            return textEncryptor.encrypt(message);
        }else {
            return "";
        }
    }

    /**
     * 获取含有 :: 的个数并分支
     * @param message
     * @return
     */
    private String getDecryptOrRemoteKey(String message) {
        int countOfColon = countColon(message);
        if (countOfColon <= 0) {
            return getResultWithNoColon(message);
        }
        if (countOfColon == 1) {
            return getResultWithColon(message);
        }
        return "";
    }


    private String getResultWithNoColon(String message) {
        String trimValue = message.trim();
        boolean flag = trimValue.startsWith(innerPrefix) && trimValue.endsWith(innerSuffix);
        if(flag){
            int decryptPassword = dataDeCryptProperties.getDecryptPassword();
            message = CommonTool.getMessage(message, innerPrefix, innerSuffix);
            return IdaasKaiserUtil.decryptKaiser(message,decryptPassword);
        }
        return message;
    }
    /**
     * 含有一个 :: 的处理逻辑
     * @param message
     * @return
     */
    private String getResultWithColon(String message) {
        String result = null;
        String[] messageArray = message.split(COLON);
        String key = messageArray[0];
        String value = messageArray[1];
        String url = dataDeCryptProperties.getUrl();
        result = getByRemoteOrKms(key, url);
        if (StringUtils.isBlank(result)) {
            result = getResultWithNoColon(value);
        }
        return result;
    }
    /**
     * 远程获取密钥
     * @param key
     * @param url
     * @return
     */
    private String getByRemoteOrKms(String key, String url) {
        com.acxfoxer.idaas.dataenanddecryptbootstarter.service.CustomGetCryptorService cryptorService = getCryptorInstance();

        try {
            return cryptorService.getRemoteCryptor(url, key);
        } catch (Exception e) {
            log.error("获取远程密钥失败", e);
        }
        return null;
    }
    /**
     * 获取对象(starter本身密钥获取对象 还是 客户端自定义密钥获取对象)
     * @return
     */
    private com.acxfoxer.idaas.dataenanddecryptbootstarter.service.CustomGetCryptorService getCryptorInstance() {
        String customGetCryptorClass = dataDeCryptProperties.getCustomGetCryptorClass();
        com.acxfoxer.idaas.dataenanddecryptbootstarter.service.CustomGetCryptorService customGetCryptorService = null;
        try {
            if (StringUtils.isNotBlank(customGetCryptorClass)) {
                Class<?> clazz = Class.forName(customGetCryptorClass);
                customGetCryptorService = (com.acxfoxer.idaas.dataenanddecryptbootstarter.service.CustomGetCryptorService) clazz.newInstance();
            } else {
                customGetCryptorService = new CustomGetCryptorService();
            }
        } catch (Exception e) {
            log.error("获取客户端对象失败！");
        }
        return customGetCryptorService;
    }
    /**
     * 计算::的个数
     * @param message
     * @return
     */
    private int countColon(String message) {
        String trimValue = message.trim();
        int count = 0;
        int length = message.length();
        while (trimValue.indexOf(COLON) != -1) {
            trimValue = trimValue.substring(trimValue.indexOf(COLON) + 1, length);
            count++;
        }
        return count;
    }
}

