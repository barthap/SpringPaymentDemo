package com.hapex.paymentdemo.dotpay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "payment.dotpay")
@Data
public class DotpayConfiguration {
    private String shopId;
    private String pin;
    private String transactionUrl;
    private boolean isProduction = false;
}
