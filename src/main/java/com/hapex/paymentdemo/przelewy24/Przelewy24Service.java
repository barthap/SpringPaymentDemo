package com.hapex.paymentdemo.przelewy24;

import com.hapex.paymentdemo.przelewy24.request.CustomParams;
import com.hapex.paymentdemo.przelewy24.request.Przelewy24Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

@Service
public class Przelewy24Service {

    private static final String URI_REGISTER = "trnRegister";
    private static final String URI_REQUEST = "trnRequest";
    private static final String URI_VERIFY = "trnVerify";

    private final Przelewy24Configuration config;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public Przelewy24Service(Przelewy24Configuration config) {
        this.config = config;
    }

    /**
     * Performs Przelewy24 REGISTER call
     * @param builder Params needed to perform payment
     * @return URL for User to redirect to Przelewy24 payment page
     * @throws Exception when request fails
     */
    public String sendRegister(Przelewy24Request.P24RequestBuilder builder) throws Exception{

        Map<String, String> defaultParams = new HashMap<>();
        defaultParams.put("p24_merchant_id", config.getMerchantId());
        defaultParams.put("p24_pos_id", config.getPosId());
        defaultParams.put("p24_url_return", config.getThanksUrl());
        defaultParams.put("p24_url_status", config.getStatusUrl());
        defaultParams.put("p24_api_version", Przelewy24Utils.P24_VERSION);

        //TODO: Add other params outside this fun
        defaultParams.put("p24_session_id", "SomethingLikeControl");

        Przelewy24Request request = Przelewy24Request.builder()
                .with(new CustomParams(defaultParams))
                .build(config.getSalt());

        Map<String, String> response = sendP24Post(URI_REGISTER, request.getParams());

        try {
            final String error = response.get("error");
            if(!error.equals("0"))
                throw new Exception("Przelewy24 error: " + error + response.getOrDefault("errorMessage", ""));


        } catch (NullPointerException e) {
            throw new Exception("Unknown response error!");
        }

        try {
            return getPaymentURL(response.get("token"));
        } catch (NullPointerException e) {
            throw new Exception("Reponse did not contain token!");
        }
    }

    /**
     * @see <a href="https://www.przelewy24.pl/storage/app/media/pobierz/Instalacja/przelewy24_dokumentacja_3.2.pdf">Paragrapsh 5.4 and 5.5</a>
     * @param requestBody POST data gathered from Przelewy24 status URL
     */
    public void HandleStatusURLResponse(Map<String, String> requestBody) {
        throw new NotImplementedException();
    }

    private String getPaymentURL(String token) {
        return getP24BaseUrl() + URI_REQUEST + "/" + token;
    }

    private Map<String, String> sendP24Post(String uri, Map<String, String> params) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity( getP24BaseUrl() + uri, request , String.class );

        if(response.getStatusCode() != HttpStatus.OK)
            throw new Exception("HTTP Error code " + String.valueOf(response.getStatusCodeValue()));

        String[] paramPairs = response.getBody().split("&");
        Map<String, String> resParams = new HashMap<>();

        for(String pair : paramPairs) {
            String[] kv = pair.split("=");
            resParams.put(kv[0], kv[1]);
        }

        return resParams;
    }

    private String getP24BaseUrl() {
        return config.isProduction() ? Przelewy24Utils.P24_PRODUCTION_URL : Przelewy24Utils.P24_SANDBOX_URL;
    }

}
