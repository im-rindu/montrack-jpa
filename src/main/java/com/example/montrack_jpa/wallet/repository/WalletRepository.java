package com.example.montrack_jpa.wallet.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.montrack_jpa.wallet.entity.Wallet;

public interface WalletRepository extends CrudRepository<Wallet, Integer> {
  Iterable<Wallet> findAllByDeletedAtIsNullOrderByIdAsc();
  Iterable<Wallet> findAllByUserIdAndDeletedAtIsNull(Integer userId);
} 
