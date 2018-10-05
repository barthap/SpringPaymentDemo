package com.hapex.paymentdemo.dotpay.request;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class TransactionInfo implements RequestDecorator {

    @NonNull private float amount;
    @NonNull private String currency;
    @NonNull private String description;

    public TransactionInfo(float amount, String description) {
        this(amount, "PLN", description);
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();

        final String strAmount = String.format("%.2f", amount).replace(',', '.');
        params.put("amount", strAmount);
        params.put("currency", currency);
        params.put("description", description);
        return params;
    }
}
