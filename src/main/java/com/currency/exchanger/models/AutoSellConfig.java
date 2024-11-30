package com.currency.exchanger.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AutoSellConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID values
    private Long id;

    private int coinId; // Reference to the coin being tracked

    private String username; // Username of the user configuring auto-sell

    private BigDecimal aboveInput; // Price threshold for auto-sell

    private Boolean isAutoSell; // Whether auto-sell is enabled

    // Custom constructor for easier object creation
    public AutoSellConfig(String username, int coinId, Double aboveInput, Boolean isAutoSell) {
        this.username = username;
        this.coinId = coinId;
        this.aboveInput = BigDecimal.valueOf(aboveInput); // Convert to BigDecimal
        this.isAutoSell = isAutoSell;
    }
}

