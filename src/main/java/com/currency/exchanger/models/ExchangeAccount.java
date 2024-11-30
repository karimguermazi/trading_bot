package com.currency.exchanger.models;

import com.currency.exchanger.auth.models.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class ExchangeAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exchangeAccountId;

    private String apiKey;
    private String apiSecret;
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "exchangeAccount")
    private List<Order> orders;
}
