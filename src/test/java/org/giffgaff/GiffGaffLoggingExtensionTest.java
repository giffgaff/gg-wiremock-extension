package org.giffgaff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import wiremock.org.slf4j.MDC;

class GiffGaffLoggingExtensionTest {

  private static final String TRACE_ID_HEADER = "X-B3-TraceId";
  private static final String SPAN_ID_HEADER = "X-B3-SpanId";
  private static final String TRACE_JSON_VALUE = "traceId";
  private static final String SPAN_JSON_VALUE = "spanId";

  @Test
  void mdcUsesLogbackAdapter() {

    Request request =
        new RequestBuilder()
            .build();

    sendRequestToExtension(request);

    assertNotNull(MDC.getMDCAdapter());
    assertEquals(MDC.getMDCAdapter().getClass().getSimpleName(), "LogbackMDCAdapter");
  }

  @Test
  void mdcStoresTraceId() {

    Request request =
        new RequestBuilder()
            .withHeader(TRACE_ID_HEADER, "abc-123")
            .build();

    sendRequestToExtension(request);

    assertEquals("abc-123", MDC.get(TRACE_JSON_VALUE));
  }

  @Test
  void mdcStoresSpanId() {

    Request request = new RequestBuilder()
        .withHeader(TRACE_ID_HEADER, "abc-123")
        .withHeader(SPAN_ID_HEADER, "def-456")
        .build();

    sendRequestToExtension(request);

    assertEquals("def-456", MDC.get(SPAN_JSON_VALUE));
  }

  private void sendRequestToExtension(Request request) {
    GiffGaffLoggingExtension giffGaffLoggingExtension = new GiffGaffLoggingExtension();
    giffGaffLoggingExtension.filter(request);
  }


  private class RequestBuilder {

    List<HttpHeader> httpHeaderList = new ArrayList<>();

    RequestBuilder withHeader(String key, String value) {
      httpHeaderList.add(new HttpHeader(key, value));
      return this;
    }

    Request build() {
      HttpHeaders headers = new HttpHeaders(httpHeaderList);
      return new LoggedRequest(
          "",
          null,
          null,
          null,
          headers,
          null,
          false,
          null,
          null,
          null,
          null
      );
    }
  }
}
