package com.hapex.paymentdemo.dotpay.request;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

public class CreditCardInfo implements RequestDecorator {
    @Override
    public Map<String, String> getParams() {
        throw new NotImplementedException();
    }
}
