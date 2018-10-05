package com.hapex.paymentdemo.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Builder
public class User {

    @Id
    @GeneratedValue
    private long id;

    private String firstName;
    private String lastName;
    private String email;

    @OneToOne(mappedBy = "user",
            optional = false,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    UserSubscribtion subscribtion;
}
