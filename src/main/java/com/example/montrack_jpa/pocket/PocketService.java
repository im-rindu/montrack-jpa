package com.example.montrack_jpa.pocket;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.montrack_jpa.CustomResponse;
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
    if(pocket.getLimitAmount() == null) {
      errorInput.add("Limit Amount can't be empty");
    }else if(pocket.getLimitAmount() < 0) {
      errorInput.add("Limit Amount can't be negative, only 0 or more number allowed");
    }
    if(pocket.getWalletId() == null ) {
      errorInput.add("Wallet id can't be null");
    }
    else if(!checkWallet(pocket.getWalletId())){
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

  public ResponseEntity<CustomResponse<Object>> getPockets() {
    CustomResponse<Object> response;
    response = new CustomResponse<Object>(HttpStatus.OK, "OK", "List of all pockets", pocketRepository.findAllByDeletedAtIsNull());
    return response.toResponseEntity();
  }

  public ResponseEntity<CustomResponse<Object>> getPocketsByWalletId(Integer walletId) {
    CustomResponse<Object> response;
    if(!checkWallet(walletId)) {
      response = new CustomResponse<Object>(HttpStatus.NOT_FOUND, "NOT FOUND", "Wallet not found with id : " + walletId, null);
    }
    else{
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "List of all pockets by wallet id : " + walletId, pocketRepository.findAllByWalletIdAndDeletedAtIsNull(walletId));
    }
    return response.toResponseEntity();
  }

  public ResponseEntity<CustomResponse<Object>> getPocket(Integer id) {
    CustomResponse<Object> response;
    Pocket pocket = checkPocket(id);
    if(pocket == null) {
      response = new CustomResponse<Object>(HttpStatus.NOT_FOUND, "NOT FOUND", "Pocket not found with id : " + id	, pocket);
    }
    else{
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "Successfully get pocket id : " + id, pocket);
    }
    return response.toResponseEntity();
  }

  public ResponseEntity<CustomResponse<Object>> updatePocket(Integer id, Pocket pocket) {
    List<String> errorInput = new ArrayList<>();
    Boolean isFound = true;
    Pocket pocketToUpdate = checkPocket(id);
    CustomResponse<Object> response;

    if(pocketToUpdate == null) {
      errorInput.add("Pocket not found with id : " + id);
      isFound = false;
    }
    else{
      pocketToUpdate.setName(pocket.getName());
      pocketToUpdate.setLimitAmount(pocket.getLimitAmount());
      pocketToUpdate.setWalletId(pocket.getWalletId());
      pocketRepository.save(pocketToUpdate);
    }
    if(!errorInput.isEmpty()) {
      response = new CustomResponse<Object>(isFound ? HttpStatus.BAD_REQUEST : HttpStatus.NOT_FOUND, isFound ? "BAD REQUEST" : "NOT FOUND", "Failed to continue", errorInput);
    }
    else{
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "Successfully updated pocket with id : " + id, pocketToUpdate);
    }
    return response.toResponseEntity();
  }

  public ResponseEntity<CustomResponse<Object>> deletePocket(Integer id, boolean isDelete) {
    List<String> errorInput = new ArrayList<>();
    Boolean isFound = true;
    Pocket pocketToDelete = checkPocket(id);
    CustomResponse<Object> response;

    if(pocketToDelete == null) {
      errorInput.add("Pocket not found with id : " + id);
      isFound = false;
    }
    else{
      if(isDelete) {
        if(pocketToDelete.getDeletedAt() != null) {
          errorInput.add("Pocket already deleted with id : " + id);
        }
        pocketToDelete.setDeletedAt(Instant.now());
      }
      else{
        if(pocketToDelete.getDeletedAt() == null) {
          errorInput.add("Pocket with id : " + id + " is not deleted yet");
        }
        pocketToDelete.setDeletedAt(null);
      }
      pocketRepository.save(pocketToDelete);
    }
    if(!errorInput.isEmpty()) {
      response = new CustomResponse<Object>(isFound ? HttpStatus.BAD_REQUEST : HttpStatus.NOT_FOUND, isFound ? "BAD REQUEST" : "NOT FOUND", "Failed to delete pocket", errorInput);
    }
    else{
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "Successfully " + (isDelete ? "delete" : "restore") + " a pocket with id : " + id, null);
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

  private Pocket checkPocket(Integer id) {
    Optional<Pocket> pocket = pocketRepository.findById(id);
    if (pocket.isEmpty()) {
      return null;
    }
    return pocket.get();
  }
}
