package com.example.montrack_jpa.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user", schema = "finance")
public class User {
  @Id
  @GeneratedValue
  private Integer id;
}
