package com.currency.exchanger.repository;

import com.currency.exchanger.models.CoinMarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinMarketDataRepository extends JpaRepository<CoinMarketData, Integer> { }

