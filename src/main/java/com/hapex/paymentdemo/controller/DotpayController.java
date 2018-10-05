package com.hapex.paymentdemo.controller;


import com.hapex.paymentdemo.domain.User;
import com.hapex.paymentdemo.domain.UserSubscribtion;
import com.hapex.paymentdemo.dotpay.database.DotpayURLCResponse;
import com.hapex.paymentdemo.dotpay.html.DefaultDotpayHtmlTemplate;
import com.hapex.paymentdemo.dotpay.html.DotpayHtmlTemplate;
import com.hapex.paymentdemo.dotpay.DotpayService;
import com.hapex.paymentdemo.dotpay.request.*;
import com.hapex.paymentdemo.dotpay.request.DotpayRequest.DotpayRequestBuilder;
import com.hapex.paymentdemo.repository.UserRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

import static com.hapex.paymentdemo.domain.UserSubscribtion.SubscribtionStatus.ACTIVE;
import static com.hapex.paymentdemo.domain.UserSubscribtion.SubscribtionStatus.INACTIVE;
import static com.hapex.paymentdemo.domain.UserSubscribtion.SubscribtionStatus.PENDING;
import static com.hapex.paymentdemo.dotpay.database.DotpayURLCResponse.OperationStatus.COMPLETED;

@RestController("/dotpay")
@CommonsLog
public class DotpayController {

    private final UserRepository userRepository;
    private final DotpayService service;

    @Autowired
    public DotpayController(UserRepository userRepository, DotpayService service) {
        this.userRepository = userRepository;
        this.service = service;
    }

    /**
     *
     * TODO: Move the business logic out of controller to service layer!
     */

    @RequestMapping(value = "/pay/{userId}", produces = MediaType.TEXT_HTML_VALUE)
    public String payForUser(@PathVariable long userId) throws Exception{

        User user = userRepository.findById(userId).orElseThrow(()-> new Exception("User Not found!"));
        UserSubscribtion sub = user.getSubscribtion();

        if(sub.getStatus() != INACTIVE)
            return "<!doctype html><h2>Your subscribtion status is " + sub.getStatus().toString() + "</h2>";

        //set subscribtion status to pending
        sub.setTransactionControl(UUID.randomUUID().toString());
        sub.setLastTransactionDate(new Date());
        sub.setStatus(PENDING);
        userRepository.save(user);

        DotpayRequestBuilder request = DotpayRequest.builder()
                .with(new TransactionInfo(10.00f, "Subscribtion payment"))
                .with(new BackButton(BackButton.ComebackType.BACK_BUTTON))
                .with(new Control(sub.getTransactionControl()))
                .with(new CustomerInfo(user.getFirstName(), user.getLastName(), user.getEmail()));


        DotpayHtmlTemplate htmlTemplate = service.BeginTransactionForUser(request, DefaultDotpayHtmlTemplate.class);

        return "<!doctype html><p>You have no active subscribtion</p>" + htmlTemplate.render();
    }

    @RequestMapping(value = "/thanks", produces = MediaType.TEXT_HTML_VALUE)
    public String thanksPage(@RequestParam String status) {
        return  "<!doctype html><h3>Thank you! Your transaction status is " + status + "</h3>";
    }

    @PostMapping(
            value = "/urlc-receiver",
            produces = MediaType.TEXT_PLAIN_VALUE,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public String receiveURLCMessage(@RequestParam Map<String, String> params, HttpServletRequest req) throws Exception {

        DotpayURLCResponse response = service.HandleURLCResponse(params);

        Optional<User> optUser = userRepository.findBySubscribtionTransactionControl(response.getControl());
        if(!optUser.isPresent())
            log.error("User not found by control!");
        else {
            //TODO: Check also if currency, amount etc. are right

            User user = optUser.get();
            UserSubscribtion sub = user.getSubscribtion();
            sub.setStatus(response.getOperationStatus() == COMPLETED ? ACTIVE : INACTIVE);
            sub.setTransactionControl("");

            response.setRelatedSubscribtion(sub);

            userRepository.save(user);
        }

        return "OK";
    }

    @GetMapping("/transactions")
    public Iterable<DotpayURLCResponse> getAllTransactions() {
        return service.getAllTransactions();
    }

    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public void addUser() {
        createUser();
    }

    @Transactional
    void createUser() {
        UserSubscribtion sub = UserSubscribtion.builder()
                .id(1L)
                .status(INACTIVE)
                .customerId(UUID.randomUUID().toString())
                .build();

        User user = User.builder()
                .id(1L)
                .email("test@test.pl")
                .firstName("Eustachy")
                .lastName("Motyka")
                .subscribtion(sub)
                .build();
        sub.setUser(user);

        userRepository.save(user);
    }
}
