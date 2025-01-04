package org.example.demoreceiver.service;

import org.example.demoreceiver.model.CloudProvider;
import org.springframework.stereotype.Service;

@Service
public class AwsCloudStorageService implements CloudStorage {

    @Override
    public void downloadFileFromBucket(String bucketName, String fileName) {
        System.out.println("AWS Service Injected");
    }

    @Override
    public String getProvider() {
        return CloudProvider.AWS.name();
    }
}
