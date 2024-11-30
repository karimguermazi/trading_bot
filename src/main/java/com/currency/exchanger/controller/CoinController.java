package com.currency.exchanger.controller;

import com.currency.exchanger.auth.models.User;
import com.currency.exchanger.auth.repository.UserRepository;
import com.currency.exchanger.models.AutoSellConfig;
import com.currency.exchanger.models.Coin;
import com.currency.exchanger.models.UserCoin;
import com.currency.exchanger.repository.AutoSellConfigRepository;
import com.currency.exchanger.repository.CoinRepository;
import com.currency.exchanger.repository.UserCoinRepository;
import com.currency.exchanger.service.CoinService;
import com.currency.exchanger.service.UserCoinService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/coins")
public class CoinController {
    private final CoinService coinService;

    private final CoinRepository coinRepository;

    private final UserCoinRepository userCoinRepository;

    private final UserRepository userRepository;

    private final UserCoinService userCoinService;

    private final AutoSellConfigRepository autoSellConfigRepository;

    public CoinController(CoinService coinService, CoinRepository coinRepository, UserCoinRepository userCoinRepository, UserRepository userRepository, UserCoinService userCoinService, AutoSellConfigRepository autoSellConfigRepository) {
        this.coinService = coinService;
        this.coinRepository = coinRepository;
        this.userCoinRepository = userCoinRepository;
        this.userRepository = userRepository;
        this.userCoinService = userCoinService;
        this.autoSellConfigRepository = autoSellConfigRepository;
    }

    @GetMapping("/getall")
    public List<Coin> getAllCoins() {
        return coinService.getAllCoins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coin> getCoinById(@PathVariable int id) {
        return coinService.getCoinById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Coin createCoin(@RequestBody Coin coin) {
        return coinService.saveCoin(coin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoin(@PathVariable int id) {
        coinService.deleteCoin(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update-coin-symbole/{symbole}/{coindId}")
    public void updateCoinSymbole(@PathVariable String symbole, @PathVariable int coindId) {
        Coin coin = coinRepository.getReferenceById(coindId);
        if (Objects.nonNull(symbole)) {
            coin.setSymbol(symbole);
        }
        coinRepository.save(coin);
    }

    @PostMapping("/update-coin-value/{coindId}/{value}")
    public void updateCoinValue(@PathVariable int coindId, @PathVariable BigDecimal value) {
        Coin coin = coinRepository.getReferenceById(coindId);
        if (Objects.nonNull(value)) {
            coin.setValue(value);
        }
        coinRepository.save(coin);
    }

    @PostMapping("/add-coin")
    public void updateCoinValue(@RequestBody Coin coin) {
        coinRepository.save(coin);
    }

    @PostMapping("/buy-coin/{username}/{coinId}/{value}")
    @Transactional
    public ResponseEntity<String> updateCoinValue(
            @PathVariable String username,
            @PathVariable int coinId,
            @PathVariable BigDecimal value) {

        // Fetch user by username
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = userOpt.get();

        // Fetch coin by ID
        Optional<Coin> coinOpt = coinRepository.findById(coinId);
        if (coinOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coin not found");
        }
        Coin coin = coinOpt.get();

        // Find UserCoin associated with the user and coin
        Optional<UserCoin> userCoinOpt = user.getUserCoins().stream()
                .filter(userCoin -> userCoin.getCoin().equals(coin))
                .findFirst();

        if (userCoinOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not own this coin");
        }

        UserCoin userCoin = userCoinOpt.get();

        // Calculate transaction cost and check balance
        BigDecimal transactionCost = coin.getValue().multiply(value);
        if (userCoin.getBalance().compareTo(transactionCost) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance");
        }

        // Update balance
        userCoin.setBalance(userCoin.getBalance().subtract(transactionCost));

        // Save changes (Transactional annotation ensures atomicity)
        userCoinRepository.save(userCoin);
        userRepository.save(user);

        return ResponseEntity.ok("Coin purchase successful");
    }

    @PostMapping("/sell-coin/{username}/{coinId}/{value}")
    @Transactional
    public ResponseEntity<String> sellCoinValue(
            @PathVariable String username,
            @PathVariable int coinId,
            @PathVariable BigDecimal value) {

        return userCoinService.sellCoin(username, coinId, value);
    }



    @PostMapping("/configure-auto-sell/{username}/{coinId}")
    public ResponseEntity<String> configureAutoSell(
            @PathVariable String username,
            @PathVariable int coinId,
            @RequestParam Double aboveInput,
            @RequestParam Boolean isAutoSell) {

        // Save the auto-sell configuration
        AutoSellConfig config = new AutoSellConfig(username, coinId, aboveInput, true);
        autoSellConfigRepository.save(config);

        return ResponseEntity.ok("Auto-sell configured successfully");
    }


}

