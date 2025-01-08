package org.example.demoreceiver;

import org.example.demoreceiver.service.ExchangeDataFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExchangeDataFileTest {

    private ExchangeDataFile exchangeDataFile = new ExchangeDataFile();

    @Test
    public void testReadExchangeDataFromFileSuccess() {

        var result = exchangeDataFile.fetchExchange();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }
}
