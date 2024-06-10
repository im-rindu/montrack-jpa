package com.example.montrack_jpa.wallet;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.montrack_jpa.CustomResponse;
import com.example.montrack_jpa.user.User;
import com.example.montrack_jpa.user.UserRepository;

@Service
public class WalletService {
  private WalletRepository walletRepository;

  private UserRepository userRepository;

  public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
    this.walletRepository = walletRepository;
    this.userRepository = userRepository;
  }

  public ResponseEntity<CustomResponse<Object>> createWallet(Wallet wallet) {
    List<String> errorInput = new ArrayList<>();
    if(wallet.getName().isBlank()) {
      errorInput.add("Name can't be blank or empty");
    }
    if(wallet.getBalance() < 0) {
      errorInput.add("Balance can't be negative, only 0 or more number allowed");
    }
    if(wallet.getUserId() == null ) {
      errorInput.add("User id can't be null");
    }
    if(!checkUser(wallet.getUserId())){
      errorInput.add("User not found with id : " + wallet.getUserId());
    }
    CustomResponse<Object> response;
    if(!errorInput.isEmpty()) {
      response = new CustomResponse<Object>(HttpStatus.BAD_REQUEST, "BAD REQUEST", "Failed to created wallet", errorInput);
      }
    else{
      response = new CustomResponse<Object>(HttpStatus.CREATED, "CREATED", "Successfully created wallet", walletRepository.save(wallet));
    }
    return response.toResponseEntity();
  }

  public ResponseEntity<CustomResponse<Object>> getWallet(Integer id) {
    CustomResponse<Object> response;
    Wallet wallet = checkWallet(id);
    if(wallet == null) {
      response = new CustomResponse<Object>(HttpStatus.NOT_FOUND, "NOT FOUND", "Wallet not found with id : " + id	, wallet);
    }
    else{
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "Successfully get wallet id : " + id, wallet);
    }
    return response.toResponseEntity();
  }

  public Wallet updateWallet(Integer id, Wallet wallet) {
    Wallet updatedWallet = checkWallet(id);

    if(wallet.getBalance() != null) {
      updatedWallet.setBalance(wallet.getBalance());
    }
    if(wallet.getName() != null) {
      updatedWallet.setName(wallet.getName());
    }
    updatedWallet.setUpdatedAt(Instant.now());
    return walletRepository.save(updatedWallet);
  }

  public ResponseEntity<CustomResponse<Object>> deleteWallet(Integer id, boolean isDelete) {
    List<String> errorInput = new ArrayList<>();
    Boolean isFound = true;
    Wallet walletToDelete = checkWallet(id);
    CustomResponse<Object> response;

    if(walletToDelete == null) {
      errorInput.add("Wallet not found with id : " + id);
      isFound = false;
    }
    else{
      if(isDelete) {
        if(walletToDelete.getDeletedAt() != null) {
          errorInput.add("Wallet already deleted with id : " + id);
        }
        walletToDelete.setDeletedAt(Instant.now());
      }
      else{
        if(walletToDelete.getDeletedAt() == null) {
          errorInput.add("Wallet with id : " + id + " is not deleted yet");
        }
        walletToDelete.setDeletedAt(null);
      }
      walletRepository.save(walletToDelete);
    }
    if(!errorInput.isEmpty()) {
      response = new CustomResponse<Object>(isFound ? HttpStatus.BAD_REQUEST : HttpStatus.NOT_FOUND, isFound ? "BAD REQUEST" : "NOT FOUND", "Failed to continue", errorInput);
    }else{
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "Successfully " + (isDelete ? "deleted" : "undeleted") + " a wallet with id : " + id, walletToDelete);
    }

    return response.toResponseEntity();
  }

  public Iterable<Wallet> getAllWallets() {
    return walletRepository.findAllByDeletedAtIsNullOrderByIdAsc();
  }

  public ResponseEntity<CustomResponse<Object>> getWalletsByUserId(Integer userId) {
    CustomResponse<Object> response;
    if(!checkUser(userId)) {
      response = new CustomResponse<Object>(HttpStatus.NOT_FOUND, "NOT FOUND", "User not found with id : " + userId, null);
    }
    else{
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "List of all wallets by user id : " + userId, walletRepository.findAllByUserIdAndDeletedAtIsNull(userId));
    }
    return response.toResponseEntity();
  }

  private Wallet checkWallet(Integer id){
    Optional<Wallet> wallet = walletRepository.findById(id);
    if (wallet.isEmpty()) {
      return null;
    }
    return wallet.get();
  }

  private Boolean checkUser(Integer userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      return false;
    }
    return true;
  }

}
