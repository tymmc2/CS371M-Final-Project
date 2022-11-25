package com.example.stockapp.search;

import com.example.stockapp.stockcard.Stock;
import com.example.stockapp.stockcard.StockData;
import com.example.stockapp.stockservice.StockAPI;
import com.example.stockapp.stockview.LineGraphCache;

import java.util.ArrayList;
import java.util.List;

public class AllStockCache {
    private static AllStockCache cacheInstance;
    private static List<StockData> cache;

    private AllStockCache() { }

    private static void createCache() {
        cacheInstance = new AllStockCache();
        cache = new ArrayList<>();
        cacheInstance.loadCache();
    }

    public static AllStockCache getAllStockCache() {
        if (cacheInstance == null)
            createCache();
        return cacheInstance;
    }

    public void loadCache() {
        if (cacheInstance == null)
            createCache();
        StockAPI api = new StockAPI();
        api.fetchAllStocks();
    }

    public void updateCache(List<StockData> newList) {
        if (cacheInstance == null)
            createCache();
        cache.clear();
        cache.addAll(newList);
    }

    public List<StockData> getAllStocks() { return cache; }

    public StockData getStock(String symbol) {
        if (cacheInstance == null)
            createCache();
        for (StockData item : cache) {
            if (item.getStockName().equals(symbol)) {
                return item;
            }
        }
        return null;
    }
}
