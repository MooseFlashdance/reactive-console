package com.example.reactiveconsole.model;

import java.util.Random;
import lombok.Data;

@Data
public class RxNbrAssignment {
  private final Integer lastRxNbr;

  public RxNbrAssignment() {
    this.lastRxNbr = new Random().nextInt(9999999);
  }
}
