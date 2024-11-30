package com.currency.exchanger.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Pair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pairId;

    private String description;

    @ManyToOne
    @JoinColumn(name = "coin_id")
    private Coin coin;

    @OneToMany(mappedBy = "pair")
    private List<Order> orders;
}
