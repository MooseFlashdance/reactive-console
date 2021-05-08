package com.example.reactiveconsole.service;

import static com.example.reactiveconsole.utils.ContextUtils.logOnNext;

import com.example.reactiveconsole.model.Rx;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RxService {

  private static final Integer MIN_BARCODE_ORIGINAL_RX = 1;
  private static final Integer MAX_BARCODE_ORIGINAL_RX = 10000;
  private static final Integer MIN_BARCODE_ERX = 10000;
  private static final Integer MAX_BARCODE_ERX = 20000;
  private static final Integer MIN_RX_NBR = 2000000;
  private static final Integer MAX_RX_NBR = 8999999;

  public Mono<Rx> createRx(Boolean isIMZ, Boolean isERx, String product) {

    return Mono.just("Starting createRx")
        .doOnEach(logOnNext(log::info))
        .then(Mono.just(new Rx(isIMZ, isERx, product)))
        .doOnEach(logOnNext(rx -> log.info("New Rx ID [{}]", rx.getId())))
        .flatMap(rx -> setBarcode(rx, isIMZ, isERx))
        .flatMap(rx -> setRxNbr(rx, product))
        .flatMap(this::save)
        .doOnEach(logOnNext(rx -> log.info("Finished createRx")));
  }

  public Mono<Rx> save(Rx rx) {

    Mono<Rx> result;
    Mono<String> startMsg = Mono.just("Starting save");

    try {

      result =
          startMsg
              .doOnEach(logOnNext(log::info))
              .then(Mono.just(rx.clone()))
              .flatMap(
                  newRx -> {
                    newRx.setVersion(UUID.randomUUID().toString());
                    return Mono.just(newRx);
                  })
              .doOnEach(logOnNext(rx1 -> log.info("Rx saved successfully")));

    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
      result = Mono.empty();
    }

    return result.doOnEach(logOnNext(rx1 -> log.info("Finished save")));
  }

  public Mono<Rx> setRxNbr(Rx rx, String product) {

    Mono<Rx> result;
    Mono<String> startMsg = Mono.just("Starting setRxNbr");

    if (product != null && product.length() > 0) {

      result =
          startMsg
              .doOnEach(logOnNext(log::info))
              .doOnEach(logOnNext(msg -> log.info("Product found. Need to set the Rx number")))
              .then(getRxNbr())
              .flatMap(
                  rxNbr -> {
                    rx.setRxNbr(rxNbr);
                    return Mono.just(rx);
                  })
              .doOnEach(logOnNext(rx1 -> log.info("Set Rx number [{}]", rx1.getRxNbr())));
    } else {

      result =
          startMsg
              .doOnEach(logOnNext(log::info))
              .doOnEach(logOnNext(msg -> log.info("Skipping Rx number assignment")))
              .then(Mono.just(rx));
    }

    return result.doOnEach(logOnNext(rx1 -> log.info("Finished setRxNbr")));
  }

  public Mono<Integer> getRxNbr() {

    Mono<String> startMsg = Mono.just("Starting getRxNbr");

    return startMsg
        .doOnEach(logOnNext(log::info))
        .then(Mono.just(new Random().nextInt(MAX_RX_NBR - MIN_RX_NBR) + MIN_RX_NBR))
        .delayElement(Duration.ofSeconds(1))
        .doOnEach(logOnNext(rxNbr -> log.info("Got Rx number [{}]", rxNbr)))
        .doOnEach(logOnNext(rxNbr -> log.info("Finished getRxNbr")));
  }

  public Mono<Rx> setBarcode(Rx rx, Boolean isIMZ, Boolean isERx) {

    Mono<Rx> result;
    Mono<String> startMsg = Mono.just("Starting setBarCode");

    if (isIMZ) {

      result =
          startMsg
              .doOnEach(logOnNext(log::info))
              .doOnEach(logOnNext(msg -> log.info("Skipping barcode creation for IMZ")))
              .then(Mono.just(rx));
    } else {

      result =
          startMsg
              .doOnEach(logOnNext(log::info))
              .doOnEach(logOnNext(msg -> log.info("Barcode needs to be created")))
              .then(getBarcode(isERx))
              .flatMap(
                  barcode -> {
                    rx.setBarcode(barcode);
                    return Mono.just(rx);
                  })
              .doOnEach(logOnNext(rx1 -> log.info("New barcode [{}]", rx.getBarcode())));
    }

    return result.doOnEach(logOnNext(rx1 -> log.info("Finished setBarCode")));
  }

  public Mono<Integer> getBarcode(Boolean isERx) {

    Mono<Integer> result;
    Mono<String> startMsg = Mono.just("Starting getBarCode");

    if (isERx) {

      result =
          startMsg
              .doOnEach(logOnNext(log::info))
              .doOnEach(logOnNext(msg -> log.info("Getting barcode for ERx")))
              .then(
                  Mono.just(
                      new Random().nextInt(MAX_BARCODE_ERX - MIN_BARCODE_ERX) + MIN_BARCODE_ERX))
              .delayElement(Duration.ofSeconds(1));
    } else {

      result =
          startMsg
              .doOnEach(logOnNext(log::info))
              .doOnEach(logOnNext(msg -> log.info("Getting barcode for non-ERx")))
              .then(
                  Mono.just(
                      new Random().nextInt(MAX_BARCODE_ORIGINAL_RX - MIN_BARCODE_ORIGINAL_RX)
                          + MIN_BARCODE_ORIGINAL_RX))
              .delayElement(Duration.ofSeconds(1));
    }

    return result
        .doOnEach(logOnNext(barcode -> log.info("Got barcode [{}]", barcode)))
        .doOnEach(logOnNext(barcode -> log.info("Finished getBarcode with barcode [{}]", barcode)));
  }
}
