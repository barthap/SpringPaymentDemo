package com.hapex.paymentdemo.dotpay.request;

import java.util.Map;

public class CustomParams implements RequestDecorator {
    private Map<String, String> customParams;

    public CustomParams(Map<String, String> customParams) {
        this.customParams = customParams;
    }

    @Override
    public Map<String, String> getParams() {
        return customParams;
    }
}
