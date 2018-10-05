package com.hapex.paymentdemo.dotpay.html;

import com.hapex.paymentdemo.dotpay.request.DotpayRequest;

public class DefaultDotpayHtmlTemplate extends DotpayHtmlTemplate {

    private static final String DEFAULT_BUTTON_TEXT = "Pay with Dotpay";

    DefaultDotpayHtmlTemplate(DotpayRequest request, String dotpayUrl) throws IllegalArgumentException {
        super(request, dotpayUrl);
    }


    @Override
    protected String renderButton() {
        return "<p><button type=\"submit\" form=\"" + FORM_NAME +
                "\" value=\"Submit\">" +
                DEFAULT_BUTTON_TEXT +
                "</button></p>";
    }

}
