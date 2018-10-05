package com.hapex.paymentdemo.przelewy24;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;

public class Przelewy24Utils {

    public static final String P24_VERSION = "3.2";
    public static final String P24_SANDBOX_URL = "https://sandbox.przelewy24.pl/";
    public static final String P24_PRODUCTION_URL = "https://secure.przelewy24.pl/";

    /**
     * Generates signature for provided params
     * @param params Map of params (at least needed)
     * @return Signature hash (p24_sign)
     */
    public static String generateSignature(Map<String, String> params, String salt) throws Exception {
        String[] fields={"p24_session_id", "p24_merchant_id", "p24_amount", "p24_currency"};

        StringBuilder sb = new StringBuilder();


        try {
            for (String field : fields) {
                sb.append(params.get(field)).append("|");
            }
            sb.append(salt);

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Params for signature hash are invalid!");
        }

        return hashMD5(sb.toString());
    }

    public static String hashMD5(String str) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(str.getBytes("UTF-8"));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }
}
