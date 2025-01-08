package org.example.demoreceiver;

import org.example.demoreceiver.service.ExchangeDataFactory;
import org.example.demoreceiver.service.ExchangeDataFile;
import org.example.demoreceiver.service.ExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExchangeDataFactoryTest {

    private ExchangeDataFactory exchangeDataFactory;


    @BeforeEach
    void setup() {
        exchangeDataFactory = new ExchangeDataFactory(List.of(new ExchangeDataFile(), new ExchangeService(WebClient.builder().build())));
    }

    @Test
    public void testGetExchangeDataFromFile() {

        var exchangeDataProvider = exchangeDataFactory.getExchangeDataProvider("ExchangeDataFile");

        assertNotNull(exchangeDataProvider);
        assertEquals(exchangeDataProvider.getClass().getSimpleName(), ExchangeDataFile.class.getSimpleName());
    }

    @Test
    public void testGetExchangeDataFromApi() {

        var exchangeDataProvider = exchangeDataFactory.getExchangeDataProvider("ExchangeService");

        assertNotNull(exchangeDataProvider);
        assertEquals(exchangeDataProvider.getClass().getSimpleName(), ExchangeService.class.getSimpleName());
    }

}
