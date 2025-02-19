package org.example.demoreceiver;

import org.example.demoreceiver.model.Exchange;
import org.example.demoreceiver.service.ConsumerService;
import org.example.demoreceiver.service.ExchangeDataFactory;
import org.example.demoreceiver.service.ExchangeDataProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {

    private final ConsumerService consumerService;
    private final ExchangeDataFactory exchangeDataFactory;

    public StartupRunner(ConsumerService consumerService, ExchangeDataFactory exchangeDataFactory) {
        this.consumerService = consumerService;
        this.exchangeDataFactory = exchangeDataFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        ExchangeDataProvider exchangeDataProvider = exchangeDataFactory.getExchangeDataProvider("ExchangeDataFile");

        //Exchange e2 = new Exchange("test.end2", "oci1.orange");
        List<Exchange> exchanges = exchangeDataProvider.fetchExchange();

        consumerService.startConsumers(exchanges);
    }
}
