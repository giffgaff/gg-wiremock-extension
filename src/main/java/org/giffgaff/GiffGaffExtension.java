package org.giffgaff;


import static com.github.tomakehurst.wiremock.common.LocalNotifier.notifier;

import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilterAction;
import com.github.tomakehurst.wiremock.extension.requestfilter.StubRequestFilter;
import com.github.tomakehurst.wiremock.http.Request;
import java.io.File;
import org.slf4j.MDC;

public class GiffGaffExtension extends StubRequestFilter {

  private static final String TRACE_JSON_VALUE = "traceId";
  private static final String SPAN_JSON_VALUE = "spanId";
  private static final String TRACE_ID_HEADER = "X-B3-TraceId";
  private static final String SPAN_ID_HEADER = "X-B3-SpanId";

  @Override
  public String getName() {
    return "giffgaff-extension";
  }


  @Override
  public RequestFilterAction filter(Request request) {
    MDC.put(TRACE_JSON_VALUE, request.getHeader(TRACE_ID_HEADER));
    MDC.put(SPAN_JSON_VALUE, "test2");
    notifier().error("Request trace id: " + request.getHeader(TRACE_ID_HEADER));
    notifier().error("Request span id: " + request.getHeader(SPAN_ID_HEADER));

    String classpath = System.getProperty("java.class.path");
    String[] classpathEntries = classpath.split(File.pathSeparator);
    for (String entry : classpathEntries) {
      notifier().error("Classpath entry: " + entry);
    }
    return RequestFilterAction.continueWith(request);
  }
}
