package com.currency.exchanger.service;

import com.currency.exchanger.auth.models.User;
import com.currency.exchanger.auth.repository.UserRepository;
import com.currency.exchanger.models.AutoSellConfig;
import com.currency.exchanger.models.Coin;
import com.currency.exchanger.models.UserCoin;
import com.currency.exchanger.repository.AutoSellConfigRepository;
import com.currency.exchanger.repository.CoinRepository;
import com.currency.exchanger.repository.UserCoinRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserCoinService {
    private final UserCoinRepository userCoinRepository;
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;
    private final AutoSellConfigRepository autoSellConfigRepository;

    public final Map<Integer, BigDecimal> autoSellMap = new HashMap<>();

    public UserCoinService(UserCoinRepository userCoinRepository, UserRepository userRepository, CoinRepository coinRepository, AutoSellConfigRepository autoSellConfigRepository) {
        this.userCoinRepository = userCoinRepository;
        this.userRepository = userRepository;
        this.coinRepository = coinRepository;
        this.autoSellConfigRepository = autoSellConfigRepository;
    }

    public List<UserCoin> getAllUserCoins() {
        return userCoinRepository.findAll();
    }

    public Optional<UserCoin> getUserCoinById(int id) {
        return userCoinRepository.findById(id);
    }

    public BigDecimal getBalanceByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        BigDecimal balance = BigDecimal.ZERO;
        if (user.isPresent()) {
            List<UserCoin> userCoins = user.get().getUserCoins();
            for (UserCoin userCoin : userCoins) {
                BigDecimal coinBalance = userCoin.getBalance();
                if (coinBalance != null) {
                    balance = balance.add(coinBalance);
                }
            }
        }
        return balance;
    }

    @Transactional
    public void regroupUserCoins() {
        userCoinRepository.createTempTable();
        userCoinRepository.deleteOriginalData();
        userCoinRepository.insertGroupedData();
        userCoinRepository.dropTempTable();
    }

    @Transactional
    public UserCoin saveUserCoinswithupdate(UserCoin userCoin, String username, int coinId) {
        userCoin.setUser(userRepository.findByUsername(username).orElse(null));
        userCoin.setCoin(coinRepository.getReferenceById(coinId));
        userCoinRepository.save(userCoin);
        this.regroupUserCoins();
        return userCoin;
    }

    public void deleteUserCoin(int id) {
        userCoinRepository.deleteById(id);
    }

    public void updateBalanceById(int id, BigDecimal balance) {
        Optional<UserCoin> userCoin = userCoinRepository.findById(id);
        if (userCoin.isPresent()) {
            userCoin.get().setBalance(balance);
            userCoinRepository.save(userCoin.get());
        }
    }


    public void setAuto(BigDecimal autoSell, int coinId) {
        autoSellMap.put(coinId, autoSell);
    }

    @Scheduled(fixedRate = 10000) // Check every 10 seconds
    public void monitorAutoSellConditions() {
        List<AutoSellConfig> configs = autoSellConfigRepository.findAll();

        for (AutoSellConfig config : configs) {
            Optional<Coin> optionalCoin = coinRepository.findById(config.getCoinId());
            if (config.getIsAutoSell() && optionalCoin.get().getValue().compareTo(config.getAboveInput()) > 0) {
                // Perform the sell logic
                Optional<User> userOpt = userRepository.findByUsername(config.getUsername());
                if (userOpt.isEmpty()) {
                    break;
                }
                User user = userOpt.get();
                Optional<UserCoin> userCoinOpt = user.getUserCoins().stream()
                        .filter(userCoin -> userCoin.getCoin().equals(optionalCoin.get()))
                        .findFirst();
                if (userCoinOpt.isEmpty()) {
                    break;
                }

                UserCoin userCoin = userCoinOpt.get();

                // Calculate transaction cost and check balance
                BigDecimal transactionCost = userCoinOpt.get().getBalance().divide(userCoin.getCoin().getValue(), 0);
                if(transactionCost.equals(0)){
                    break;
                }
                sellCoin(config.getUsername(), config.getCoinId(), transactionCost);
                autoSellConfigRepository.delete(config); // Remove config after execution
            }
        }
    }

    public ResponseEntity<String> sellCoin(String username, int coinId, BigDecimal value) {
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
        userCoin.setBalance(userCoin.getBalance().add(transactionCost));

        // Save changes (Transactional annotation ensures atomicity)
        userCoinRepository.save(userCoin);
        userRepository.save(user);

        return ResponseEntity.ok("Coin purchase successful");
    }
}

