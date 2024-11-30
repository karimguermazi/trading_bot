package com.currency.exchanger.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private String side;
    private String type;

    @ManyToOne
    @JoinColumn(name = "pair_id")
    private Pair pair;

    @ManyToOne
    @JoinColumn(name = "exchange_account_id")
    private ExchangeAccount exchangeAccount;
}
