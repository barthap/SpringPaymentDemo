package com.hapex.paymentdemo.dotpay.html;

import com.hapex.paymentdemo.dotpay.DotpayUtils;
import com.hapex.paymentdemo.dotpay.request.DotpayRequest;

import java.util.Map;

public abstract class DotpayHtmlTemplate {

    private final Map<String, String> params;
    private final String dotpayUrl;
    protected final String FORM_NAME = "dotpay_redirection_form";

    DotpayHtmlTemplate(DotpayRequest request, String dotpayUrl) throws IllegalArgumentException {
        this.dotpayUrl = dotpayUrl;
        this.params = request.getParams();
    }

    public String render() {
        StringBuilder htmlBuilder = new StringBuilder("<div><form action=\"");
        htmlBuilder.append(dotpayUrl);
        htmlBuilder.append("\" id=\"");
        htmlBuilder.append(FORM_NAME);
        htmlBuilder.append("\" method=\"POST\" enctype=\"application/x-www-form-urlencoded\">");

        for (Map.Entry<String, String> param: params.entrySet()) {
            htmlBuilder.append("<input type=\"hidden\" name=\"");
            htmlBuilder.append(param.getKey());
            htmlBuilder.append("\" value=\"");
            htmlBuilder.append(param.getValue());
            htmlBuilder.append("\">");
        }

        htmlBuilder.append("</form>");

        htmlBuilder.append(renderButton());

        htmlBuilder.append("</div>");

        return htmlBuilder.toString();
    }

    protected abstract String renderButton();
}
