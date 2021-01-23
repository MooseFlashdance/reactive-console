package com.example.reactiveconsole;

import com.example.reactiveconsole.service.RxService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@SpringBootApplication
public class ReactiveConsoleApplication implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(ReactiveConsoleApplication.class);

  private final RxService rxService;

  public static void main(String[] args) {
    SpringApplication.run(ReactiveConsoleApplication.class, args);
  }

  @Override
  public void run(String... args) {
    LOG.info("STARTING APPLICATION");

    rxService
        .createRx(false, true, "MORPHINE")
        .flatMap(
            rx -> {
              LOG.info(rx.toString());
              return Mono.just(rx);
            })
        .block();

    LOG.info("FINISHED APPLICATION");
  }
}
