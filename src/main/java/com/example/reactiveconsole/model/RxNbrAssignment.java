package com.example.reactiveconsole.model;

import lombok.Data;

import java.util.Random;

@Data
public class RxNbrAssignment {
    private final Integer lastRxNbr;

    public RxNbrAssignment() {
        this.lastRxNbr = new Random().nextInt(9999999);
    }
}
