package com.currency.exchanger.repository;

import com.currency.exchanger.models.AutoSellConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoSellConfigRepository extends JpaRepository<AutoSellConfig, Long> { }
