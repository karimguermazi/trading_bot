package com.currency.exchanger.repository;

import com.currency.exchanger.models.ExchangeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeAccountRepository extends JpaRepository<ExchangeAccount, Integer> { }

