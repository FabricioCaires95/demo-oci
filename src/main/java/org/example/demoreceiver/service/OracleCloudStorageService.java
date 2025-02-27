package org.example.demoreceiver.service;

import com.oracle.bmc.model.BmcException;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import org.example.demoreceiver.configuration.OracleAuthenticatorConfig;
import org.example.demoreceiver.model.CloudProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class OracleCloudStorageService implements CloudStorage {

    private static final Logger logger = LoggerFactory.getLogger(OracleCloudStorageService.class);
    private final OracleAuthenticatorConfig oracleAuthenticatorConfig;

    @Value("${pathToSaveFile}")
    private String filePath;

    public OracleCloudStorageService(OracleAuthenticatorConfig oracleAuthenticatorConfig) {
        this.oracleAuthenticatorConfig = oracleAuthenticatorConfig;
    }

    @Override
    public void downloadFileFromBucket(String bucketName, String fileName) {
        logger.info("OCI service injected");

        try (final ObjectStorage objectStorageClient = oracleAuthenticatorConfig.createObjectStorage()) {

            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .namespaceName("griawvbnokp5")
                    .bucketName(bucketName)
                    .objectName(fileName)
                    .build();

            final GetObjectResponse response = objectStorageClient.getObject(objectRequest);
            copyFileToFileServer(response.getInputStream(), fileName);

        } catch (BmcException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String getProvider() {
        return CloudProvider.OCI.name();
    }

    public void copyFileToFileServer(InputStream data, String fileName) {
        logger.info("Starting to copy file: [{}] to file server: [{}] ", fileName, filePath);

        final File file = new File(filePath + fileName);

        try (final InputStream inputStream = data; final FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            logger.info("File saved successfully at: {} ", filePath);

        } catch (IOException e) {
            logger.error("Error occurred while copying file to file server: ", e);
        }
    }

}
