package com.example.montrack_jpa.pocket.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.montrack_jpa.pocket.entity.Pocket;

@Repository
public interface PocketRepository extends CrudRepository<Pocket, Integer> {
  Iterable<Pocket> findAllByDeletedAtIsNull();
  Iterable<Pocket> findAllByWalletIdAndDeletedAtIsNull(Integer walletId);
  Optional<Pocket> findByIdAndDeletedAtIsNull(Integer id);
  
} 
