package com.example.montrack_jpa.pocket;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class PocketController {
  private PocketService pocketService;

  public PocketController(PocketService pocketService) {
    this.pocketService = pocketService;
  }

  
}
