package com.currency.exchanger.service;

import com.currency.exchanger.models.Coin;
import com.currency.exchanger.repository.CoinRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoinService {
    private final CoinRepository coinRepository;

    public CoinService(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public List<Coin> getAllCoins() {
        return coinRepository.findAll();
    }

    public Optional<Coin> getCoinById(int id) {
        return coinRepository.findById(id);
    }

    public Coin saveCoin(Coin coin) {
        return coinRepository.save(coin);
    }

    public void deleteCoin(int id) {
        coinRepository.deleteById(id);
    }


}
