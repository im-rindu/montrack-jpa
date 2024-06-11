package com.example.montrack_jpa.trade;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface TradeRepository extends CrudRepository<Trade, Integer> {
  Iterable<Trade> findAllByDeletedAtIsNullAndWalletIdOrderByIdDesc(Integer walletId);
  
  @Query("SELECT t FROM Trade t WHERE t.deletedAt IS NULL AND t.walletId = :walletId ORDER BY t.date DESC")
  Page<Trade> findLastTransaction(@RequestParam Integer walletId, Pageable pageable);
  
  @Query("SELECT t FROM Trade t WHERE t.deletedAt IS NULL AND t.walletId = :walletId AND t.date >= :startDate ORDER BY t.date DESC")
  Page<Trade> findLastTransaction(@RequestParam Integer walletId, @RequestParam Instant startDate, Pageable pageable);

  @Query("SELECT t FROM Trade t WHERE t.deletedAt IS NULL AND t.walletId = :walletId AND t.date <= :endDate ORDER BY t.date DESC")
  Page<Trade> findLastTransaction(@RequestParam Integer walletId, Pageable pageable, @RequestParam Instant endDate);

  @Query("SELECT t FROM Trade t WHERE t.deletedAt IS NULL AND t.walletId = :walletId AND t.date >= :startDate AND t.date <= :endDate ORDER BY t.date DESC")
  Page<Trade> findLastTransaction(@RequestParam Integer walletId, @RequestParam Instant startDate, @RequestParam Instant endDate, Pageable pageable);

  @Query("SELECT t.type, SUM(t.amount) FROM Trade t WHERE t.deletedAt IS NULL AND t.walletId = :walletId AND t.date >= :startDate GROUP BY t.type")
  Iterable<Trade> getSumTransactionByType(@RequestParam Integer walletId, @RequestParam Instant startDate);
} 
