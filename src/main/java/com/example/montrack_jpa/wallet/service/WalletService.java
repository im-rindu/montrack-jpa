package com.example.montrack_jpa.wallet.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.montrack_jpa.CustomResponse;
import com.example.montrack_jpa.trade.Trade;
import com.example.montrack_jpa.trade.TradeRepository;
import com.example.montrack_jpa.user.entity.User;
import com.example.montrack_jpa.user.repository.UserRepository;
import com.example.montrack_jpa.wallet.entity.Wallet;
import com.example.montrack_jpa.wallet.repository.WalletRepository;

@Service
public class WalletService {
  private WalletRepository walletRepository;
  private UserRepository userRepository;
  private TradeRepository tradeRepository;

  public WalletService(WalletRepository walletRepository, UserRepository userRepository, TradeRepository tradeRepository) {
    this.walletRepository = walletRepository;
    this.userRepository = userRepository;
    this.tradeRepository = tradeRepository;
  }

  public ResponseEntity<CustomResponse<Object>> createWallet(Wallet wallet) {
    List<String> errorInput = new ArrayList<>();
    if(wallet.getName().isBlank()) {
      errorInput.add("Name can't be blank or empty");
    }
    if(wallet.getBalance() == null) {
      errorInput.add("Balance can't be empty");
    }
    else if(wallet.getBalance() < 0){
      errorInput.add("Balance can't be negative");
    }
    if(wallet.getUserId() == null ) {
      errorInput.add("User id can't be null");
    }
    else if(checkUser(wallet.getUserId()) == null) {
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
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "Successfully " + (isDelete ? "deleted" : "restore") + " a wallet with id : " + id, walletToDelete);
    }

    return response.toResponseEntity();
  }

  public Iterable<Wallet> getAllWallets() {
    return walletRepository.findAllByDeletedAtIsNullOrderByIdAsc();
  }

  public ResponseEntity<CustomResponse<Object>> getWalletsByUserId(Integer userId) {
    CustomResponse<Object> response;
    User user = checkUser(userId);
    if(user == null) {
      response = new CustomResponse<Object>(HttpStatus.NOT_FOUND, "NOT FOUND", "User not found with id : " + userId, null);
    }
    else{
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "List of all wallets by user id : " + userId + ", active wallet : " + user.getActiveWallet(), walletRepository.findAllByUserIdAndDeletedAtIsNull(userId));
    }
    return response.toResponseEntity();
  }

  @SuppressWarnings("null")
  public ResponseEntity<CustomResponse<Object>> changeActiveWallet(Integer userId, Integer walletId) {
    List<String> errorInput = new ArrayList<>();
    Boolean isFound = true;
    User user = checkUser(userId);
    Wallet wallet = checkWallet(walletId);

    if(user == null) {
      errorInput.add("User not found with id : " + userId);
      isFound = false;
    }
    if(wallet == null) {
      errorInput.add("Wallet not found with id : " + walletId);
      isFound = false;
    }
    else if(wallet.getUserId() != userId){
      errorInput.add("Wallet did not belong to user with id : " + userId);
    }

    CustomResponse<Object> response;

    if(errorInput.isEmpty()) {
      user.setActiveWallet(walletId);
      userRepository.save(user);
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "Changed active wallet", user);
    }
    else{
      response = new CustomResponse<Object>(isFound ? HttpStatus.BAD_REQUEST : HttpStatus.NOT_FOUND, isFound ? "BAD REQUEST" : "NOT FOUND", "Failed to continue", errorInput);
    }

    return response.toResponseEntity();
  }

  public ResponseEntity<CustomResponse<Object>> lastestTrades(Integer walletId, Integer page, Integer pageSize, String startDate, String endDate) {
    CustomResponse<Object> response;
    Pageable pageable = PageRequest.of(page, pageSize);
    Page<Trade> trades;

    if(startDate != null && endDate != null) {
      Instant startTime = LocalDate.parse(startDate).atStartOfDay(ZoneOffset.UTC).toInstant();
      Instant endTime = LocalDate.parse(endDate).atStartOfDay(ZoneOffset.UTC).toInstant();
  
      trades = tradeRepository.findLastTransaction( walletId, startTime, endTime, pageable);
    }
    else if(startDate == null && endDate != null) {
      Instant endTime = LocalDate.parse(endDate).atStartOfDay(ZoneOffset.UTC).toInstant();
      trades = tradeRepository.findLastTransaction( walletId, pageable, endTime);
    }
    else if(endDate == null && startDate != null) {
      Instant startTime = LocalDate.parse(startDate).atStartOfDay(ZoneOffset.UTC).toInstant();
      trades = tradeRepository.findLastTransaction( walletId, startTime, pageable);
    }
    else{
      trades = tradeRepository.findLastTransaction( walletId, pageable);
    }
    
    if(checkWallet(walletId) == null) {
      response = new CustomResponse<Object>(HttpStatus.NOT_FOUND, "NOT FOUND", "Wallet not found with id : " + walletId, null);
    }
    else{
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "Show "+ trades.getNumberOfElements() + " trades at page : " + page + " from " + (trades.getTotalPages() - 1), trades.getContent());
    }
    return response.toResponseEntity();
  }

  public ResponseEntity<CustomResponse<Object>> getSummaryWallet(Integer userId, String range) {
    CustomResponse<Object> response;
    User user = checkUser(userId);
    LocalDate dateRange = LocalDate.now();
    
    switch (range.toLowerCase()) {
      case "week":
        dateRange = dateRange.minusDays(7);
        break;
      case "month":
        dateRange = dateRange.minusMonths(1);
        break;
      case "year":
        dateRange = dateRange.minusYears(1);
        break;
      case "day":
        dateRange = dateRange.minusDays(1);
        break;
      default:
        range = "all time";
        dateRange = dateRange.minusYears(100);
        break;
    }

    if(user == null) {
      response = new CustomResponse<Object>(HttpStatus.NOT_FOUND, "NOT FOUND", "User not found with id : " + userId	, user);
    }
    else{
      Wallet wallet = checkWallet(user.getActiveWallet());
      response = new CustomResponse<Object>(HttpStatus.OK, "OK", "Successfully get summary wallet id : " + wallet.getId() + ", this " + range, tradeRepository.getSumTransactionByType(wallet.getId(), dateRange.atStartOfDay().toInstant(ZoneOffset.UTC)));
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

  private User checkUser(Integer userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      return null;
    }
    return user.get();
  }



}
