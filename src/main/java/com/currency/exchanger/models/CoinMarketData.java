package com.currency.exchanger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class CoinMarketData {
    @Id
    private int coinId;

    private LocalDateTime syncDate;
    private BigDecimal ath;
    private BigDecimal athMs;
    private BigDecimal circulatingSupply;
    private BigDecimal totalSupply;
    private BigDecimal maxSupply;

    @OneToOne
    @MapsId
    @JoinColumn(name = "coin_id")
    private Coin coin;
}
