package com.example.montrack_jpa.pocket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.montrack_jpa.CustomResponse;
import com.example.montrack_jpa.pocket.entity.Pocket;
import com.example.montrack_jpa.pocket.service.PocketService;

@RestController
@RequestMapping("/api/v1/pocket")
public class PocketController {
  private PocketService pocketService;

  public PocketController(PocketService pocketService) {
    this.pocketService = pocketService;
  }

  @PostMapping
  public ResponseEntity<CustomResponse<Object>> createPocket(@RequestBody Pocket pocket) {
    return pocketService.createPocket(pocket);
  }

  @GetMapping
  public ResponseEntity<CustomResponse<Object>> getPockets() {
    return pocketService.getPockets();
  }

  @GetMapping("/wallet/{walletId}")
  public ResponseEntity<CustomResponse<Object>> getPocketsByWalletId(@PathVariable Integer walletId) {
    return pocketService.getPocketsByWalletId(walletId);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomResponse<Object>> getPocket(@PathVariable Integer id) {
    return pocketService.getPocket(id);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomResponse<Object>> updatePocket(@PathVariable Integer id, @RequestBody Pocket pocket) {
    return pocketService.updatePocket(id, pocket);
  }

  @PutMapping("/{id}/delete")
  public ResponseEntity<CustomResponse<Object>> deletePocket(@PathVariable Integer id) {
    return pocketService.deletePocket(id, true);
  }

  @PutMapping("/{id}/restore")
  public ResponseEntity<CustomResponse<Object>> restorePocket(@PathVariable Integer id) {
    return pocketService.deletePocket(id, false);
  }  

}
