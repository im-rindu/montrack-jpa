package com.example.montrack_jpa.pocket.entity;

import java.time.Instant;

import org.hibernate.annotations.ColumnDefault;

import com.example.montrack_jpa.wallet.entity.Wallet;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "pocket", schema = "finance")
public class Pocket {
  @Id
  @GeneratedValue
  private Integer id;

  @Column(name = "emoji")
  private String emoji;

  @Column(name = "name", nullable = false)
  private String name;

  @JoinColumn(name = "wallet_id")
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = Wallet.class)
  @JsonIgnore
  private Wallet wallet;
  
  @Column(name = "wallet_id", insertable = false, updatable = false)
  private Integer walletId;

  @ColumnDefault("0.0")
  @Column(name = "limit_amount")
  private Double limitAmount;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt = Instant.now();

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt = Instant.now();

  @Column(name = "deleted_at")
  private Instant deletedAt;
}
