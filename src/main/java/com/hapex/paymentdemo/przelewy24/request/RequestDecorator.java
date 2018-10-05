package com.hapex.paymentdemo.przelewy24.request;

import java.util.Map;

public interface RequestDecorator {
    Map<String, String> getParams();
}
