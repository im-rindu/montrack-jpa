package com.example.montrack_jpa.pocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.montrack_jpa.CustomResponse;
import com.example.montrack_jpa.user.User;
import com.example.montrack_jpa.wallet.Wallet;
import com.example.montrack_jpa.wallet.WalletRepository;

@Service
public class PocketService {
  private PocketRepository pocketRepository;
  private WalletRepository walletRepository;

  public PocketService(PocketRepository pocketRepository, WalletRepository walletRepository) {
    this.pocketRepository = pocketRepository;
    this.walletRepository = walletRepository;
  }

  public ResponseEntity<CustomResponse<Object>> createPocket(Pocket pocket) {
    List<String> errorInput = new ArrayList<>();
    if(pocket.getName().isBlank()) {
      errorInput.add("Name can't be blank or empty");
    }
    if(pocket.getLimitAmount() < 0) {
      errorInput.add("Limit Amount can't be negative, only 0 or more number allowed");
    }
    if(pocket.getWalletId() == null ) {
      errorInput.add("Wallet id can't be null");
    }
    if(!checkWallet(pocket.getWalletId())){
      errorInput.add("Wallet not found with id : " + pocket.getWalletId());
    }
    CustomResponse<Object> response;
    if(!errorInput.isEmpty()) {
      response = new CustomResponse<Object>(HttpStatus.BAD_REQUEST, "BAD REQUEST", "Failed to created pocket", errorInput);
      }
    else{
      response = new CustomResponse<Object>(HttpStatus.CREATED, "CREATED", "Successfully created pocket", pocketRepository.save(pocket));
    }
    return response.toResponseEntity();
  }

  

  private Boolean checkWallet(Integer walletId){
    Optional<Wallet> wallet = walletRepository.findById(walletId);
    if (wallet.isEmpty()) {
      return false;
    }
    return true;
  }
}
