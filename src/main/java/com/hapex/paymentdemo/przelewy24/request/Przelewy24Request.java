package com.hapex.paymentdemo.przelewy24.request;


import com.hapex.paymentdemo.przelewy24.Przelewy24Utils;

import java.util.HashMap;
import java.util.Map;

public class Przelewy24Request {

    private final Map<String, String> params;

    private Przelewy24Request(Map<String, String> params) throws IllegalArgumentException {
        this.params = params;
        validate();
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public static Przelewy24Request.P24RequestBuilder builder() {
        return new Przelewy24Request.P24RequestBuilder();
    }

    public static class P24RequestBuilder implements RequestDecorator {
        private Map<String, String> params = new HashMap<>();

        public Przelewy24Request.P24RequestBuilder with(RequestDecorator decorator) {
            params.putAll(decorator.getParams());
            return this;
        }

        public Przelewy24Request build(String salt) throws Exception {

            final String crc = Przelewy24Utils.generateSignature(params, salt);
            this.params.put("p24_sign", crc);

            //finally build request
            return new Przelewy24Request(params);
        }

        /**
         * This allows builder to use params from another builder
         * @return builder params
         */
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    private void validate() throws IllegalArgumentException {
        //TODO: Add validation params

        /*if(!params.containsKey("id"))
            throw new IllegalArgumentException("Shop ID is not set!");
        if(!params.containsKey("amount"))
            throw new IllegalArgumentException("Transaction amount is not set!");
        if(!params.containsKey("currency"))
            throw new IllegalArgumentException("Transaction currency is not set!");
        if(!params.containsKey("description"))
            throw new IllegalArgumentException("Transaction description is not set!");
        if(!params.containsKey("chk"))
            throw new IllegalArgumentException("Request checksum is not set!");*/
    }
}
