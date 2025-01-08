package org.example.demoreceiver.service;

import org.example.demoreceiver.model.Exchange;

import java.util.List;

public interface ExchangeDataProvider {

    List<Exchange> fetchExchange();

}
