package com.currency.exchanger.service;

import com.currency.exchanger.models.Exchange;
import com.currency.exchanger.repository.ExchangeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;

    public ExchangeService(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    public List<Exchange> getAllExchanges() {
        return exchangeRepository.findAll();
    }

    public Optional<Exchange> getExchangeById(int id) {
        return exchangeRepository.findById(id);
    }

    public Exchange saveExchange(Exchange exchange) {
        return exchangeRepository.save(exchange);
    }

    public void deleteExchange(int id) {
        exchangeRepository.deleteById(id);
    }
}

