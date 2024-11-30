package com.currency.exchanger.service;

import com.currency.exchanger.models.CoinMarketData;
import com.currency.exchanger.repository.CoinMarketDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoinMarketDataService {
    private final CoinMarketDataRepository coinMarketDataRepository;

    public CoinMarketDataService(CoinMarketDataRepository coinMarketDataRepository) {
        this.coinMarketDataRepository = coinMarketDataRepository;
    }

    public List<CoinMarketData> getAllMarketData() {
        return coinMarketDataRepository.findAll();
    }

    public Optional<CoinMarketData> getMarketDataById(int id) {
        return coinMarketDataRepository.findById(id);
    }

    public CoinMarketData saveMarketData(CoinMarketData data) {
        return coinMarketDataRepository.save(data);
    }

    public void deleteMarketData(int id) {
        coinMarketDataRepository.deleteById(id);
    }
}
