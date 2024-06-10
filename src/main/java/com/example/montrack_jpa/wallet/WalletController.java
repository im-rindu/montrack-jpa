package com.example.montrack_jpa.wallet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.montrack_jpa.CustomResponse;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
  private WalletService walletService;

  public WalletController(WalletService walletService) {
    this.walletService = walletService;
  }

  @GetMapping
  public ResponseEntity<CustomResponse<Iterable<Wallet>>> getAllWallets() {
    CustomResponse<Iterable<Wallet>> response = new CustomResponse<Iterable<Wallet>>(HttpStatus.OK, "OK", "List of all wallets", walletService.getAllWallets());
    
    return response.toResponseEntity();
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<CustomResponse<Object>> getWallet(@PathVariable Integer id) {
    return walletService.getWallet(id);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<CustomResponse<Object>> getWalletsByUserId(@PathVariable Integer userId) {
    return walletService.getWalletsByUserId(userId);
  }
    
  @PostMapping
  public ResponseEntity<CustomResponse<Object>> createWallet(@RequestBody Wallet wallet) {
    return walletService.createWallet(wallet);
    }
    
  @PutMapping("/{id}")
  public ResponseEntity<CustomResponse<Wallet>> updateWallet(@PathVariable Integer id, @RequestBody Wallet wallet) {
    CustomResponse<Wallet> response = new CustomResponse<Wallet>(HttpStatus.OK, "OK", "Successfully updated wallet with id : " + id, walletService.updateWallet(id, wallet));
    
    return response.toResponseEntity();
  }

  @PutMapping("/{id}/delete")
  public ResponseEntity<CustomResponse<Object>> deleteWallet(@PathVariable Integer id) {
    return walletService.deleteWallet(id, true);
  }
    
  @PutMapping("/{id}/undelete")
  public ResponseEntity<CustomResponse<Object>> unDeleteWallet(@PathVariable Integer id) {
    return walletService.deleteWallet(id, false);
  }

}
