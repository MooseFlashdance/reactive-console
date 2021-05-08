package com.example.reactiveconsole.utils;

import com.example.reactiveconsole.constants.RxConstants;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.MDC;
import reactor.core.publisher.Signal;

public class ContextUtils {

  public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {

    return signal -> {

      // ignore signals that aren't onNext
      if (!signal.isOnNext()) return;

      // fetch the correlation-id from the signal's context
      Optional<String> correlationId =
          signal.getContextView().getOrEmpty(RxConstants.CORRELATION_ID_KEY);

      // if the correlation-id is present set the MDC and do the logging
      // if the correlation-id isn't present just do the logging
      correlationId.ifPresentOrElse(
          cid -> {

            // use MDCCloseable so the MDC is automatically cleaned up
            try (MDC.MDCCloseable cMdc =
                MDC.putCloseable(RxConstants.CORRELATION_ID_KEY, " [" + cid + "]")) {
              logStatement.accept(signal.get());
            }
          },
          () -> logStatement.accept(signal.get()));
    };
  }
}
