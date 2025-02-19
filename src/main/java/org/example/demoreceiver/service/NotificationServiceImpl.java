package org.example.demoreceiver.service;

import org.example.demoreceiver.model.FileData;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class NotificationServiceImpl implements Notification{

    private final WebClient webClient;

    public NotificationServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<String> notification(FileData fileData) {
        String url = fileData.data().callbackEndpoint();
        String token = "xxxx";

        return webClient.post().uri(url)
                .headers(httpHeaders -> {
                    httpHeaders.set("Content-Type", "application/json");
                    httpHeaders.set("Authorization", token);
                    httpHeaders.set("possibleDuplication", String.valueOf(fileData.possibleDuplication()));
                    httpHeaders.set("requester", fileData.requester());
                }).body(Mono.just(fileData), FileData.class)
                .retrieve()
                .bodyToMono(String.class);
    }
}
