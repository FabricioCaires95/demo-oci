package org.example.demoreceiver.service;

import org.example.demoreceiver.model.CloudProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AzureCloudStorageService implements CloudStorage{

    private static final Logger logger = LoggerFactory.getLogger(AzureCloudStorageService.class);

    @Override
    public void downloadFileFromBucket(String bucketName, String fileName) {
        logger.info("AZURE SERVICER INJECTED");
    }

    @Override
    public String getProvider() {
        return CloudProvider.AZURE.name();
    }
}
