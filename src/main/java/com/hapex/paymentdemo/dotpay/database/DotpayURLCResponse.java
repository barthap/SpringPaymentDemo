package com.hapex.paymentdemo.dotpay.database;

import com.hapex.paymentdemo.domain.UserSubscribtion;
import com.hapex.paymentdemo.dotpay.DotpayUtils;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.nio.file.AccessDeniedException;
import java.util.Map;

@Entity
@Data
@ToString
@Table(name = "dotpay_transactions")
public class DotpayURLCResponse {

    @Id
    private String operationNumber;

    private long shopId;
    private OperationType operationType;
    private OperationStatus operationStatus;
    private String operationOriginalAmount;
    private String operationOriginalCurrency;
    private String operationDatetime;
    private String control;
    private String email;
    private String creditCardId;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    private UserSubscribtion relatedSubscribtion;

    @ElementCollection
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    Map<String, String> params;

    public DotpayURLCResponse() {}
    public DotpayURLCResponse(String pin, String ip, Map<String, String> req) throws IllegalArgumentException, AccessDeniedException {
        this.params = req;

        if(!DotpayUtils.checkIP(ip))
            throw new AccessDeniedException("Wrong Dotpay IP: " + ip);

        try {
            final String signature = req.get("signature");
            if(!DotpayUtils.checkSignature(pin, signature, params))
                throw new Exception();
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal Signature!");
        }

        try {
            this.shopId = Long.valueOf(req.get("id"));
        } catch(NullPointerException e) {
            throw new IllegalArgumentException("Missing shop ID!");
        }

        try {
            this.operationNumber = req.get("operation_number");
        } catch(NullPointerException e) {
            throw new IllegalArgumentException("Missing operation_number");
        }

        try {
            this.operationType = OperationType.valueOf(req.get("operation_type").toUpperCase());
        } catch(NullPointerException e) {
            throw new IllegalArgumentException("Missing operation_type");
        }

        try {
            this.operationStatus = OperationStatus.valueOf(req.get("operation_status").toUpperCase());
        } catch(NullPointerException e) {
            throw new IllegalArgumentException("Missing operation_status ");
        }

        try {
            this.operationOriginalAmount = req.get("operation_original_amount");
        } catch(NullPointerException e) {
            throw new IllegalArgumentException("Missing operation_original_amount");
        }

        try {
            this.operationOriginalCurrency = req.get("operation_original_currency");
        } catch(NullPointerException e) {
            throw new IllegalArgumentException("Missing operation_original_currency");
        }

        try {
            this.control = req.get("control");
        } catch(NullPointerException e) {
            throw new IllegalArgumentException("Missing control field");
        }

        this.creditCardId = req.getOrDefault("credit_card_id", "");
        this.operationDatetime = req.getOrDefault("operation_datetime", "");
        this.email = req.getOrDefault("email", "");
        this.description = req.getOrDefault("description", "");

    }

    public enum OperationStatus {
        NEW,PROCESSING,COMPLETED,REJECTED,PROCESSING_REALIZATION_WAITING,PROCESSING_REALIZATION
    }

    public enum OperationType {
        PAYMENT,PAYMENT_MULTIMERCHANT_CHILD,PAYMENT_MULTIMERCHANT_PARENT,REFUND,PAYOUT,RELEASE_ROLLBACK,UNIDENTIFIED_PAYMENT,COMPLAINT
    }
}
