package org.example.demoreceiver;

import org.example.demoreceiver.configuration.OracleAuthenticatorConfig;
import org.example.demoreceiver.model.CloudProvider;
import org.example.demoreceiver.service.AwsCloudStorageService;
import org.example.demoreceiver.service.AzureCloudStorageService;
import org.example.demoreceiver.service.CloudStorageStrategyManager;
import org.example.demoreceiver.service.OracleCloudStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CloudStorageStrategyManagerTest {

    private CloudStorageStrategyManager cloudStorageStrategyManager;

    @BeforeEach
    void setup() {
        cloudStorageStrategyManager = new CloudStorageStrategyManager(List.of(new OracleCloudStorageService(new OracleAuthenticatorConfig()), new AzureCloudStorageService(), new AwsCloudStorageService()));
    }

    @Test
    public void testStrategyForOracleCloud() {
        var oracleImpl = cloudStorageStrategyManager.getStrategy(CloudProvider.getCloudProvider(1));

        assertNotNull(oracleImpl);
        assertEquals(CloudProvider.OCI.name(), oracleImpl.getProvider());
    }


    @Test
    public void testStrategyForAzureCloud() {
        var azureImpl = cloudStorageStrategyManager.getStrategy(CloudProvider.getCloudProvider(2));

        assertNotNull(azureImpl);
        assertEquals(CloudProvider.AZURE.name(), azureImpl.getProvider());
    }

    @Test
    public void testStrategyForAwsCloud() {
        var azureImpl = cloudStorageStrategyManager.getStrategy(CloudProvider.getCloudProvider(3));

        assertNotNull(azureImpl);
        assertEquals(CloudProvider.AWS.name(), azureImpl.getProvider());
    }
}
