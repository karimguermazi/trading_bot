package com.currency.exchanger.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exchangeId;

    private String name;
    private String apiBaseUrl;
    private boolean status;
}

