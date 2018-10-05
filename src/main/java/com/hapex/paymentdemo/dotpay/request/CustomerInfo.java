package com.hapex.paymentdemo.dotpay.request;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class CustomerInfo implements RequestDecorator {

    private String firstName;
    private String lastName;
    private String email;

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params= new HashMap<>();
        params.put("firstname", firstName);
        params.put("lastname", lastName);
        params.put("email", email);
        return params;
    }
}
