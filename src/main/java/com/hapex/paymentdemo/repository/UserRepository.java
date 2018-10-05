package com.hapex.paymentdemo.repository;

import com.hapex.paymentdemo.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findBySubscribtionTransactionControl(String control);
}
