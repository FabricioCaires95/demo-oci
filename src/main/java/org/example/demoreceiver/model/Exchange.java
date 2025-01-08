package org.example.demoreceiver.model;

public record Exchange(String bucketName, String streamingPool, Integer cloudId, String namespace, String endpoint) {
}
