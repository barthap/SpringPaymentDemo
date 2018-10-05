package com.hapex.paymentdemo.dotpay.request;

import com.hapex.paymentdemo.dotpay.DotpayUtils;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * Request objects containing all parameters needed to perform a Dotpay payment
 * @see <a href="https://ssl.dotpay.pl/s2/login/cloudfs1/magellan_media/common_file/dotpay_instrukcja_techniczna_implementacji_platnosci.pdf">Dotpay Implementation instruction (PL)</a>
 */
public class DotpayRequest {

    private final Map<String, String> params;

    private DotpayRequest(Map<String, String> params) throws IllegalArgumentException {
        this.params = params;
        validate();
    }

    /**
     * Returns complete request
     * @return Map of request parameters
     */
    public Map<String, String> getParams() {
        return this.params;
    }

    /**
     * Creates new Request builder
     * @return Request builder
     */
    public static DotpayRequestBuilder builder() {
        return new DotpayRequestBuilder();
    }

    public static class DotpayRequestBuilder implements RequestDecorator {
        private Map<String, String> params = new HashMap<>();

        //default package-private constructor
        DotpayRequestBuilder(){}

        /**
         * Adds params to request
         * @param decorator
         * @return self with params from decorator
         */
        public DotpayRequestBuilder with(RequestDecorator decorator) {
            params.putAll(decorator.getParams());
            return this;
        }

        /**
         * Finishes building request
         * @param pin Dotpay account shop pin
         * @return DotpayRequest
         * @throws IllegalArgumentException when request params are invalid
         */
        public DotpayRequest build(String pin) throws IllegalArgumentException {
            //add checksum
            final String chk = DotpayUtils.generateChk(pin, params);
            this.params.put("chk", chk);

            //finally build request
            return new DotpayRequest(params);
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

    /**
     * Validates request params
     * @throws IllegalArgumentException when any param is invalid
     */
    private void validate() throws IllegalArgumentException {
        if(!params.containsKey("id"))
            throw new IllegalArgumentException("Shop ID is not set!");
        if(!params.containsKey("amount"))
            throw new IllegalArgumentException("Transaction amount is not set!");
        if(!params.containsKey("currency"))
            throw new IllegalArgumentException("Transaction currency is not set!");
        if(!params.containsKey("description"))
            throw new IllegalArgumentException("Transaction description is not set!");
        if(!params.containsKey("chk"))
            throw new IllegalArgumentException("Request checksum is not set!");
    }
}
