package org.giffgaff;


import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ServeEventListener;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import org.slf4j.MDC;

public class GiffGaffExtension implements ServeEventListener {

    private static final String TRACE_JSON_VALUE = "traceId";
    private static final String SPAN_JSON_VALUE = "spanId";
    private static final String TRACE_ID_HEADER = "X-B3-TraceId";
    private static final String SPAN_ID_HEADER = "X-B3-SpanId";

    @Override
    public String getName() {
        return "giffgaff-extension";
    }

    @Override
    public void beforeMatch(ServeEvent serveEvent, Parameters parameters) {
        LoggedRequest request = serveEvent.getRequest();

        MDC.put(TRACE_JSON_VALUE, request.getHeader(TRACE_ID_HEADER));
        MDC.put(SPAN_JSON_VALUE, request.getHeader(SPAN_ID_HEADER));
    }

    @Override
    public void afterComplete(ServeEvent serveEvent, Parameters parameters) {
        MDC.clear();
    }
}
