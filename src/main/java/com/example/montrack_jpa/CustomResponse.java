package com.example.montrack_jpa;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Data;

@Data
public class CustomResponse<T> {
  private int status;
  private String statusMessage;
  private String message;
  private T data;

  public CustomResponse(HttpStatus status, String statusMessage, String message, T data){
    this.status = status.value();
    this.statusMessage = statusMessage;
    this.message = message;
    this.data = data;
  }

  public ResponseEntity<CustomResponse<T>> toResponseEntity() {
    return ResponseEntity.status(HttpStatus.valueOf(status)).body(this);
  }
}