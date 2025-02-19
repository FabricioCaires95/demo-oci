package org.example.demoreceiver.model;

public record Exchange(String bucketName, String streamingPool, Integer exchangeCode, Integer cloudId, String namespace, String endpoint, String bootstrapServer) {
}
