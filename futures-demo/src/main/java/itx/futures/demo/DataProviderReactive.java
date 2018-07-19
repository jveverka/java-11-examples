package itx.futures.demo;

import itx.futures.demo.dto.FlowData;

import java.util.concurrent.Flow;

public interface DataProviderReactive extends Flow.Publisher<FlowData>, AutoCloseable {
}
