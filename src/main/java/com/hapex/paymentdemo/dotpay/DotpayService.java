package com.hapex.paymentdemo.dotpay;

import com.hapex.paymentdemo.domain.User;
import com.hapex.paymentdemo.domain.UserSubscribtion;
import com.hapex.paymentdemo.dotpay.database.DotpayURLCResponse;
import com.hapex.paymentdemo.dotpay.database.URLCRepository;
import com.hapex.paymentdemo.dotpay.html.DotpayHtmlTemplate;
import com.hapex.paymentdemo.dotpay.request.BackButton;
import com.hapex.paymentdemo.dotpay.request.DotpayRequest;
import com.hapex.paymentdemo.dotpay.request.ShopInfo;
import com.hapex.paymentdemo.repository.UserRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.hapex.paymentdemo.domain.UserSubscribtion.SubscribtionStatus.ACTIVE;
import static com.hapex.paymentdemo.domain.UserSubscribtion.SubscribtionStatus.INACTIVE;
import static com.hapex.paymentdemo.dotpay.database.DotpayURLCResponse.OperationStatus.COMPLETED;

@Service
@CommonsLog
public class DotpayService {

    private final URLCRepository urlcRepository;
    private final UserRepository userRepository;

    private final DotpayConfiguration config;

    @Autowired
    public DotpayService(URLCRepository urlcRepository, UserRepository userRepository, DotpayConfiguration config) {
        this.urlcRepository = urlcRepository;
        this.userRepository = userRepository;
        this.config = config;
    }

    public <T extends DotpayHtmlTemplate> T BeginTransactionForUser(DotpayRequest.DotpayRequestBuilder requestParams, Class<T> htmlTemplate) throws Exception {

       /* HashMap<String, String> params = new HashMap<>();*/
        //params.put("credit_card_store", "1");
        //params.put("credit_card_customer_id", sub.getCustomerId());
        //params.put("credit_card_operation_type", "recurring_init");

        final DotpayRequest request = DotpayRequest.builder()
                .with(new ShopInfo(config.getShopId(), config.getTransactionUrl()))
                .with(new BackButton(BackButton.ComebackType.BACK_BUTTON))
                .with(requestParams)
                .build(config.getPin());

        final String url = config.isProduction() ? DotpayUtils.DOTPAY_PRODUCTION_URL : DotpayUtils.DOTPAY_SANDBOX_URL;
        return htmlTemplate.getConstructor(DotpayRequest.class, String.class).newInstance(request, url);
    }

    public DotpayURLCResponse HandleURLCResponse(Map<String, String> requestBody) throws Exception {

        final String ip = DotpayUtils.DOTPAY_IP;//req.getRemoteAddr();
        DotpayURLCResponse response = new DotpayURLCResponse(config.getPin(), ip, requestBody);

        return urlcRepository.save(response);
    }

    public Iterable<DotpayURLCResponse> getAllTransactions() {
        return urlcRepository.findAll();
    }
}
