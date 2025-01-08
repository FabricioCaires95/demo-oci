package org.example.demoreceiver.service;

import org.example.demoreceiver.model.Exchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ExchangeService implements ExchangeDataProvider {

    private final WebClient webClient;

    public ExchangeService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<Exchange>> getExchanges() {
        System.out.println("Fetching exchanges via API");
        try {
            return webClient.get()
                    .uri("http://localhost:8080/exchanges")
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse
                                    .bodyToMono(String.class)
                                    .flatMap(error -> Mono.error(new RuntimeException(error))))
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });
        }catch (WebClientResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Cacheable("exchanges")
    public List<Exchange> fetchExchange() {
        return getExchanges().block();
    }
}
