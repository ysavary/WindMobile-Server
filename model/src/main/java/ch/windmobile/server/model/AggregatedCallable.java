package ch.windmobile.server.model;

import java.util.concurrent.Callable;

public abstract class AggregatedCallable<V> implements Callable<V> {
    private final String dataSourceKey;

    public AggregatedCallable(String dataSourceKey) {
        this.dataSourceKey = dataSourceKey;
    }

    public String getDataSourceKey() {
        return dataSourceKey;
    }
}