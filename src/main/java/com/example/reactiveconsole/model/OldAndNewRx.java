package com.example.reactiveconsole.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OldAndNewRx {
  private Rx oldRx;
  private Rx newRx;
}
