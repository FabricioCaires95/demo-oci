package org.example.demoreceiver.service;

import org.example.demoreceiver.model.FileData;
import reactor.core.publisher.Mono;

public interface Notification {

    Mono<String> notification(FileData fileData);
}
