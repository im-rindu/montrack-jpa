package com.example.montrack_jpa.wallet.entity;

import java.time.Instant;

import org.hibernate.annotations.ColumnDefault;

import com.example.montrack_jpa.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "wallet", schema = "finance")
public class Wallet {
  @Id
  @GeneratedValue
  private Integer Id;

  @NotNull
  @ColumnDefault("New Wallet")
  @Column(name = "name", nullable = false)
  private String name;

  @JoinColumn(name = "user_id")
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
  @JsonIgnore
  private User user;
  
  @NotNull
  @Column(name = "user_id", insertable = false, updatable = false)
  private Integer userId;

  @ColumnDefault("0.0")
  @Column(name = "balance", nullable = false)
  private Double balance = 0.0;
  
  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt = Instant.now();

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt = Instant.now();

  @Column(name = "deleted_at")
  private Instant deletedAt;
}
