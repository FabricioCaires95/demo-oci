package org.example.demoreceiver.service;

import org.example.demoreceiver.model.Exchange;
import org.example.demoreceiver.model.exception.ExchangeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ExchangeService implements ExchangeDataProvider {

    private final WebClient webClient;

    @Value("${fpb.endpoint.url}")
    private String url;

    public ExchangeService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<Exchange>> getExchanges() {
        System.out.println("Fetching exchanges via API");
        String token = "asdahdgajhdgasjhdagsd";

        try {
            return webClient.get()
                    .uri(url)
                    .headers(httpHeaders -> {
                        httpHeaders.set("Content-Type", "application/json");
                        httpHeaders.set("Authorization", token);
                    })
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse
                                    .bodyToMono(String.class)
                                    .flatMap(error -> Mono.error(new ExchangeException(error))))
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });
        }catch (WebClientResponseException e) {
            throw new ExchangeException("Error while fetching exchanges via API");
        }
    }

    @Override
    @Cacheable("exchanges")
    public List<Exchange> fetchExchange() {
        return getExchanges().block();
    }
}
