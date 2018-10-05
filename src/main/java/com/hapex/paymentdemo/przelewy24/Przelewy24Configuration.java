package com.hapex.paymentdemo.przelewy24;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("payment.przelewy24")
public class Przelewy24Configuration {
    private String salt;
    private String merchantId;
    private String posId;
    private boolean isProduction = false;
    private String thanksUrl;
    private String statusUrl;
}
