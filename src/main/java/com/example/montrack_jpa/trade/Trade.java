package com.example.montrack_jpa.trade;

import java.time.Instant;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "trades", schema = "finance")
public class Trade {
  @Id
  @GeneratedValue
  private Integer Id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "type", nullable = false)
  private String type;

  @NotNull
  @Column(name = "date", nullable = false)
  private Instant date;

  @NotNull
  @Column(name = "amount", nullable = false)
  private Double amount;

  @Column(name = "description")
  private String description;

  @Column(name = "pocket_id")
  private Integer pocketId;

  @Column(name = "wallet_id")
  private Integer walletId;

  @Column(name = "currency")
  private String currency;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt = Instant.now();

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt = Instant.now();

  @Column(name = "deleted_at")
  private Instant deletedAt;
}
