package com.hapex.paymentdemo.dotpay.request;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class BackButton implements RequestDecorator {

    ComebackType buttonType;
    String buttonText = "";

    public BackButton(ComebackType buttonType) {
        this(buttonType, "");
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();

        if(buttonType != ComebackType.NONE)
            params.put("type", String.valueOf(buttonType.getValue()));
        if(!buttonText.isEmpty())
            params.put("buttontext", buttonText);

        return params;
    }

    public enum ComebackType {
        BACK_BUTTON (0),
        NONE (2),
        REDIRECT (4);

        private int value;
        ComebackType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
