package com.currency.exchanger.service;

import com.currency.exchanger.models.ExchangeAccount;
import com.currency.exchanger.repository.ExchangeAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExchangeAccountService {
    private final ExchangeAccountRepository exchangeAccountRepository;

    public ExchangeAccountService(ExchangeAccountRepository exchangeAccountRepository) {
        this.exchangeAccountRepository = exchangeAccountRepository;
    }

    public List<ExchangeAccount> getAllExchangeAccounts() {
        return exchangeAccountRepository.findAll();
    }

    public Optional<ExchangeAccount> getExchangeAccountById(int id) {
        return exchangeAccountRepository.findById(id);
    }

    public ExchangeAccount saveExchangeAccount(ExchangeAccount account) {
        return exchangeAccountRepository.save(account);
    }

    public void deleteExchangeAccount(int id) {
        exchangeAccountRepository.deleteById(id);
    }
}

