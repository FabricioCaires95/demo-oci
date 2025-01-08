package org.example.demoreceiver.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class ExchangeDataFactory {

    private HashMap<String, ExchangeDataProvider> providers = new HashMap<>();

    public ExchangeDataFactory(List<ExchangeDataProvider> exchangeDataProviders) {
        for (ExchangeDataProvider provider : exchangeDataProviders) {
            providers.put(provider.getClass().getSimpleName(), provider);
        }
    }

    public ExchangeDataProvider getExchangeDataProvider(String providerName) {
        var provider =  providers.get(providerName);
        if (provider == null) {
            throw new IllegalArgumentException("Provider " + providerName + " not found");
        }
        return provider;
    }
}
