package com.example.reactiveconsole;

import static com.example.reactiveconsole.utils.ContextUtils.logOnNext;

import com.example.reactiveconsole.constants.RxConstants;
import com.example.reactiveconsole.service.RxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;
import reactor.tools.agent.ReactorDebugAgent;
import reactor.util.context.Context;

@RequiredArgsConstructor
@SpringBootApplication
@Slf4j
public class ReactiveConsoleApplication implements CommandLineRunner {

  private final RxService rxService;

  public static void main(String[] args) {
    ReactorDebugAgent.init();
    SpringApplication.run(ReactiveConsoleApplication.class, args);
  }

  @Override
  public void run(String... args) {

    Mono.just("Starting main task!")
        .doOnEach(logOnNext(log::info))
        .then(rxService.createRx(false, true, "MORPHINE"))
        .doOnEach(logOnNext(rx -> log.info("Finished main task!")))
        .contextWrite(Context.of(RxConstants.CORRELATION_ID_KEY, "request-123"))
        .block();
  }
}
