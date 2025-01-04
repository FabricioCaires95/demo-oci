package org.example.demoreceiver.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CloudStorageStrategyManager {

    private final Map<String, CloudStorage> strategies = new HashMap<>();

    public CloudStorageStrategyManager(List<CloudStorage> cloudStorageImplementations) {
        for (CloudStorage cloudStorage : cloudStorageImplementations) {
            strategies.put(cloudStorage.getProvider(), cloudStorage);
        }
    }

    public CloudStorage getStrategy(String providerName) {
        CloudStorage strategy = strategies.get(providerName);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for provider: " + providerName);
        }
        return strategy;
    }
}
