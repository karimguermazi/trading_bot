package com.currency.exchanger.controller;

import com.currency.exchanger.models.CoinMarketData;
import com.currency.exchanger.service.CoinMarketDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/coin-market-data")
public class CoinMarketDataController {
    private final CoinMarketDataService coinMarketDataService;

    public CoinMarketDataController(CoinMarketDataService coinMarketDataService) {
        this.coinMarketDataService = coinMarketDataService;
    }

    @GetMapping
    public List<CoinMarketData> getAllMarketData() {
        return coinMarketDataService.getAllMarketData();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoinMarketData> getMarketDataById(@PathVariable int id) {
        return coinMarketDataService.getMarketDataById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CoinMarketData createMarketData(@RequestBody CoinMarketData data) {
        return coinMarketDataService.saveMarketData(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarketData(@PathVariable int id) {
        coinMarketDataService.deleteMarketData(id);
        return ResponseEntity.noContent().build();
    }
}

