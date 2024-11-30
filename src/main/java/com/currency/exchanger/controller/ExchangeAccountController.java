package com.currency.exchanger.controller;

import com.currency.exchanger.models.ExchangeAccount;
import com.currency.exchanger.service.ExchangeAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/exchange-accounts")
public class ExchangeAccountController {
    private final ExchangeAccountService exchangeAccountService;

    public ExchangeAccountController(ExchangeAccountService exchangeAccountService) {
        this.exchangeAccountService = exchangeAccountService;
    }

    @GetMapping
    public List<ExchangeAccount> getAllExchangeAccounts() {
        return exchangeAccountService.getAllExchangeAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExchangeAccount> getExchangeAccountById(@PathVariable int id) {
        return exchangeAccountService.getExchangeAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ExchangeAccount createExchangeAccount(@RequestBody ExchangeAccount account) {
        return exchangeAccountService.saveExchangeAccount(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExchangeAccount(@PathVariable int id) {
        exchangeAccountService.deleteExchangeAccount(id);
        return ResponseEntity.noContent().build();
    }
}

