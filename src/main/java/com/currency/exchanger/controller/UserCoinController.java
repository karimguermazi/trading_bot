package com.currency.exchanger.controller;

import com.currency.exchanger.models.UserCoin;
import com.currency.exchanger.service.UserCoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user-coins")
public class UserCoinController {
    private final UserCoinService userCoinService;

    public UserCoinController(UserCoinService userCoinService) {
        this.userCoinService = userCoinService;
    }

    @GetMapping
    public List<UserCoin> getAllUserCoins() {
        return userCoinService.getAllUserCoins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCoin> getUserCoinById(@PathVariable int id) {
        return userCoinService.getUserCoinById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add-coin/{username}/{coinId}")
    public UserCoin createUserCoin(@RequestBody UserCoin userCoin, @PathVariable String username, @PathVariable int coinId) {
        return userCoinService.saveUserCoinswithupdate(userCoin, username, coinId);
    }

    @GetMapping("/get-user-coin/{username}")
    public BigDecimal getUserCoin(@PathVariable String username) {
        return userCoinService.getBalanceByUsername(username);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserCoin(@PathVariable int id) {
        userCoinService.deleteUserCoin(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-coin/{id}/{balance}")
    public void updateCoin(@PathVariable int id, @PathVariable BigDecimal balance){
        userCoinService.updateBalanceById(id, balance);
    }

    @GetMapping("/get-auto-sell/{autoSell}/{coinId}")
    public void getAuto(@PathVariable BigDecimal autoSell, @PathVariable int coinId){
        userCoinService.setAuto(autoSell, coinId);
    }


}

