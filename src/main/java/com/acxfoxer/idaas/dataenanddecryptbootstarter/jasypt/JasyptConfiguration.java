package com.acxfoxer.idaas.dataenanddecryptbootstarter.jasypt;

import com.acxfoxer.idaas.dataenanddecryptbootstarter.properties.DataDeCryptProperties;
import com.acxfoxer.idaas.dataenanddecryptbootstarter.properties.DataEnCryptProperties;
import com.acxfoxer.idaas.dataenanddecryptbootstarter.service.impl.DataDecryptService;
import com.ulisesbocchio.jasyptspringboot.annotation.ConditionalOnMissingBean;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * jasypt配置类
 */
@Configuration
@EnableConfigurationProperties({DataDeCryptProperties.class, DataEnCryptProperties.class})
public class JasyptConfiguration {
    @Bean
    public DataDecryptService dataDecryptService(DataDeCryptProperties dataDeCryptProperties) {
        return new DataDecryptService(dataDeCryptProperties);
    }

    @Bean(name = "encryptablePropertyDetector")
    public MyEncryptablePropertyDetector encryptablePropertyDetector() {
        return new MyEncryptablePropertyDetector();
    }

    // 注意这里，因为它有多个实现类，其他实现类实例化后有可能导致，此处不实例化,所以这里要用@Primary标注
    @Bean(name = "jasyptStringEncryptor")
    @Primary
    public StringEncryptor stringEncryptor(DataDecryptService dataDecryptService, MyEncryptablePropertyDetector myEncryptablePropertyDetector) {
        return new DefaultEncryptor(dataDecryptService, myEncryptablePropertyDetector);
    }
}
