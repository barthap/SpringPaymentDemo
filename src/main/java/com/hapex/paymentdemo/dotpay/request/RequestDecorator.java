package com.hapex.paymentdemo.dotpay.request;

import java.util.Map;

public interface RequestDecorator {
    Map<String, String> getParams();
}
