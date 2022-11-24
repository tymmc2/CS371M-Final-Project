package com.example.stockapp.stockview;

import java.util.ArrayList;
import java.util.List;

public class LineGraphCache {
    private static LineGraphCache cacheInstance;
    private static List<LGCacheItem> cache;

    private LineGraphCache() { }

    private static void createCache() {
        cacheInstance = new LineGraphCache();
        cache = new ArrayList<>();
    }

    public static LineGraphCache getLineGraphCache() {
        if (cacheInstance == null)
            createCache();
        return cacheInstance;
    }

    public void addToCache(String symbol, List<Float> values) {
        if (cacheInstance == null)
            createCache();
        cache.add(new LGCacheItem(values, symbol));
    }

    public List<Float> getCacheValues(String symbol) {
        if (cacheInstance == null)
            createCache();
        for (LGCacheItem item : cache)
            if (item.getSymbol().equals(symbol))
                return item.getValues();
        return null;
    }
}
