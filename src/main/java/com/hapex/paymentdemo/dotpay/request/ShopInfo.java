package com.hapex.paymentdemo.dotpay.request;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ShopInfo implements RequestDecorator {

    @NonNull private String shopId;
    private String url;
    private String pInfo;
    private String pEmail;

    public ShopInfo(String shopId, String url) {
        this(shopId, url, "", "");
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();

        params.put("id", shopId);
        if(!url.isEmpty())
            params.put("URL", url);
        if(!pInfo.isEmpty())
            params.put("p_info", pInfo);
        if(!pEmail.isEmpty())
            params.put("p_email", pEmail);

        return params;
    }
}
