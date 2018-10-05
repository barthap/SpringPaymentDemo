package com.hapex.paymentdemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "user_subscribtion")
public class UserSubscribtion {

    @Id
    private long id;
    private String customerId = UUID.randomUUID().toString();

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    private SubscribtionStatus status = SubscribtionStatus.INACTIVE;
    private String transactionControl;
    private Date lastTransactionDate = new Date();

    private String creditCardId;

    private boolean recurringActive;


    public enum SubscribtionStatus {
        INACTIVE, PENDING, ACTIVE
    }
}
