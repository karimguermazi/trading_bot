package com.currency.exchanger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Coin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int coinId;

    private String symbol;

    private BigDecimal value;

    @OneToMany(mappedBy = "coin")
    @JsonIgnore
    private List<Pair> pairs;

    @OneToMany(mappedBy = "coin")
    @JsonIgnore
    private List<UserCoin> userCoins;

    @OneToOne(mappedBy = "coin")
    @JsonIgnore
    private CoinMarketData marketData;
}
