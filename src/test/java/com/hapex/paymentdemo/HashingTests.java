package com.hapex.paymentdemo;

import com.hapex.paymentdemo.przelewy24.Przelewy24Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class HashingTests {

    @Test
    public void generateSignatureTest() throws Exception {
        //prepare
        Map<String, String> params = new HashMap<>();
        final String salt = "a123b456c789d012";
        params.put("p24_session_id", "abcdefghijk");
        params.put("p24_merchant_id", "9999");
        params.put("p24_amount", "2500");
        params.put("p24_currency", "PLN");

        final String encoded = Przelewy24Utils.generateSignature(params, salt);

        assertThat(encoded).isEqualTo("6c7f0bb62c046fbc89921dc3b2b23ede");
    }

    @Test
    public void md5Test() throws Exception {
        final String raw = "abcdefghijk|9999|2500|PLN|a123b456c789d012";

        final String encoded = Przelewy24Utils.hashMD5(raw);

        assertThat(encoded).isEqualTo("6c7f0bb62c046fbc89921dc3b2b23ede");
    }
}
