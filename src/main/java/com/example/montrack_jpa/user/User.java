package com.example.montrack_jpa.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users", schema = "finance")
public class User {
  @Id
  @GeneratedValue
  private Integer id;

  @Column(name = "active_wallet", nullable = false)
  private Integer activeWallet;
}
