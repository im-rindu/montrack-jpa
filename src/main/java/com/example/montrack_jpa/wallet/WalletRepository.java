package com.example.montrack_jpa.wallet;

import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Integer> {
  Iterable<Wallet> findAllByDeletedAtIsNullOrderByIdAsc();
  Iterable<Wallet> findAllByUserIdAndDeletedAtIsNull(Integer userId);
} 
