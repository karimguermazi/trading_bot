package com.currency.exchanger.repository;

import com.currency.exchanger.models.UserCoin;
import com.currency.exchanger.models.UserCoinProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCoinRepository extends JpaRepository<UserCoin, Integer> {

    @Modifying
    @Transactional
    @Query(value = """
    CREATE TEMPORARY TABLE user_coin_temp AS
    SELECT user_id, coin_id, SUM(balance) AS total_balance
    FROM user_coin
    GROUP BY user_id, coin_id;
    """, nativeQuery = true)
    void createTempTable();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_coin", nativeQuery = true)
    void deleteOriginalData();

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO user_coin (user_id, coin_id, balance, display)
    SELECT user_id, coin_id, total_balance, TRUE
    FROM user_coin_temp;
    """, nativeQuery = true)
    void insertGroupedData();

    @Modifying
    @Transactional
    @Query(value = "DROP TEMPORARY TABLE user_coin_temp", nativeQuery = true)
    void dropTempTable();




}
