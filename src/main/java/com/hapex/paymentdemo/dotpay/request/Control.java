package com.hapex.paymentdemo.dotpay.request;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class Control implements RequestDecorator {

    private String control;

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("control", control);
        return params;
    }
}
