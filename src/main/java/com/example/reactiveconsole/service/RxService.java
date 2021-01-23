package com.example.reactiveconsole.service;

import com.example.reactiveconsole.model.Rx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Service
public class RxService {

  private static final Integer MIN_BARCODE_ORIGINAL_RX = 1;
  private static final Integer MAX_BARCODE_ORIGINAL_RX = 10000;
  private static final Integer MIN_BARCODE_ERX = 10000;
  private static final Integer MAX_BARCODE_ERX = 20000;
  private static final Integer MIN_RX_NBR = 2000000;
  private static final Integer MAX_RX_NBR = 8999999;

  private static final Logger LOG = LoggerFactory.getLogger(RxService.class);

  public Mono<Rx> createRx(Boolean isIMZ, Boolean isERx, String product) {

    return Mono.just(new Rx(isIMZ, isERx, product))
        .flatMap(rx -> setBarcode(rx, isIMZ, isERx))
        .flatMap(rx -> setRxNbr(rx, product))
        .flatMap(rx -> publishRxEvent(rx, save(rx)));
  }

  public Mono<Rx> publishRxEvent(Rx preUpdateRx, Mono<Rx> postUpdateRx) {
    LOG.info("Old version: " + preUpdateRx.getVersion());
    return postUpdateRx.flatMap(
        rx -> {
          LOG.info("New version: " + rx.getVersion());
          return Mono.just(rx);
        });
  }

  public Mono<Rx> save(Rx rx) {
    Rx savedRx = null;

    try {
      savedRx = rx.clone();
      savedRx.setVersion(UUID.randomUUID().toString());
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

    return savedRx != null ? Mono.just(savedRx).delayElement(Duration.ofSeconds(1)) : Mono.empty();
  }

  public Mono<Rx> setRxNbr(Rx rx, String product) {
    if (product != null && product.length() > 0) {
      return getRxNbr()
          .flatMap(
              rxNbr -> {
                rx.setRxNbr(rxNbr);
                return Mono.just(rx);
              });
    } else {
      return Mono.just(rx);
    }
  }

  public Mono<Integer> getRxNbr() {
    return Mono.just(new Random().nextInt(MAX_RX_NBR - MIN_RX_NBR) + MIN_RX_NBR)
        .delayElement(Duration.ofSeconds(2));
  }

  public Mono<Rx> setBarcode(Rx rx, Boolean isIMZ, Boolean isERx) {
    if (isIMZ) {
      return Mono.just(rx);
    } else {
      return getBarcode(isERx)
          .flatMap(
              barcode -> {
                rx.setBarcode(barcode);
                return Mono.just(rx);
              });
    }
  }

  public Mono<Integer> getBarcode(Boolean isERx) {
    if (isERx) {
      return Mono.just(new Random().nextInt(MAX_BARCODE_ERX - MIN_BARCODE_ERX) + MIN_BARCODE_ERX)
          .delayElement(Duration.ofSeconds(1));
    } else {
      return Mono.just(
              new Random().nextInt(MAX_BARCODE_ORIGINAL_RX - MIN_BARCODE_ORIGINAL_RX)
                  + MIN_BARCODE_ORIGINAL_RX)
          .delayElement(Duration.ofSeconds(1));
    }
  }
}
