package com.hapex.paymentdemo.dotpay;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Utility functions for Dotpay payments
 * @see <a href="https://ssl.dotpay.pl/s2/login/cloudfs1/magellan_media/common_file/dotpay_instrukcja_techniczna_implementacji_platnosci.pdf">Dotpay Implementation instruction (PL)</a>
 */
public class DotpayUtils {

    public static final String DOTPAY_PRODUCTION_URL ="https://ssl.dotpay.pl/t2/";
    public static final String DOTPAY_SANDBOX_URL = "https://ssl.dotpay.pl/test_payment/";
    public static final String DOTPAY_IP = "195.150.9.37";

    public static String generateSignature(String pin, Map<String, String> params) {
        String[] fields={"id","operation_number","operation_type","operation_status","operation_amount",
                "operation_currency","operation_withdrawal_amount","operation_commission_amount",
                "is_completed",
                "operation_original_amount","operation_original_currency","operation_datetime",
                "operation_related_number","control","description",
                "email","p_info","p_email",
                "credit_card_issuer_identification_number",
                        "credit_card_masked_number",
                        "credit_card_brand_codename",
                        "credit_card_brand_code","credit_card_id",
                "channel","channel_country","geoip_country"};

        StringBuilder sb = new StringBuilder(pin);
        for (String field : fields) sb.append(params.getOrDefault(field, ""));

        try {
            return hashSha256PHP(sb.toString());
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String generateChk(String pin, Map<String, String> params) {
        final String[] fields = {"api_version","charset","lang","id","pid","amount","currency","description",
                "control","channel","credit_card_brand","ch_lock","channel_groups","onlinetransfer",
                "URL","type","buttontext","urlc","firstname","lastname","email","street","street_n1",
                "street_n2","state","addr3","city","postcode","phone","country","code","p_info",
                "p_email","n_email","expiration_date","deladdr","recipient_account_number",
                "recipient_company","recipient_first_name","recipient_last_name",
                "recipient_address_street","recipient_address_building","recipient_address_apartment",
                "recipient_address_postcode","recipient_address_city","application",
                "application_version","warranty","bylaw","personal_data","credit_card_number",
                "credit_card_expiration_date_year","credit_card_expiration_date_month",
                "credit_card_security_code","credit_card_store","credit_card_store_security_code",
                "credit_card_customer_id","credit_card_id","blik_code","credit_card_registration",
                "recurring_frequency","recurring_interval","recurring_start","recurring_count",
                "surcharge_amount","surcharge","ignore_last_payment_channel","vco_call_id",
                "vco_update_order_info","vco_subtotal","vco_shipping_handling","vco_tax","vco_discount",
                "vco_gift_wrap","vco_misc","vco_promo_code","credit_card_security_code_required",
                "credit_card_operation_type","credit_card_avs","credit_card_threeds"
        };

        StringBuilder sb = new StringBuilder(pin);
        for (String field : fields) sb.append(params.getOrDefault(field, ""));

        try {
            return hashSha256PHP(sb.toString());
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private static String hashSha256PHP(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(str.getBytes());
        byte byteData[] = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte aByteData : byteData) {
            sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
        }

        //hashowanie zgodne z PHP
        sb = new StringBuilder();
        for (byte aByteData : byteData) {
            String hex = Integer.toHexString(0xff & aByteData);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }

        return sb.toString();
    }

    public static boolean checkSignature(String pin, String signature, Map<String, String> params) {
        return signature.equals(generateSignature(pin, params));
    }

    public static boolean checkIP(String ip) {
        return ip.equals(DOTPAY_IP);
    }
}
