package org.example.demoreceiver.service;

public interface CloudStorage {

    void downloadFileFromBucket(String bucketName, String fileName);

    String getProvider();
}
