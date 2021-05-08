package com.example.reactiveconsole.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePlaceholderFillRequest {
  private Integer storeNbr;
  private String rxId;
  private String orderLineItemId;
}
