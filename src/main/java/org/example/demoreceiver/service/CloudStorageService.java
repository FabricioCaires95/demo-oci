package org.example.demoreceiver.service;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class CloudStorageService {

    private static final Logger logger = LoggerFactory.getLogger(CloudStorageService.class);

    public void downloadFileFromBucket(String bucketName, String fileName) {

        try {
            final ConfigFileReader.ConfigFile configFileReader = ConfigFileReader.parseDefault();
            final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFileReader);

            ObjectStorageClient objectStorageClient = ObjectStorageClient
                    .builder()
                    .region(Region.SA_SAOPAULO_1)
                    .build(provider);

            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .namespaceName("griawvbnokp5")
                    .bucketName(bucketName)
                    .objectName(fileName)
                    .build();

            final GetObjectResponse response = objectStorageClient.getObject(objectRequest);
            logger.info(String.valueOf(response.get__httpStatusCode__()));
            if (200 == response.get__httpStatusCode__()) {
                copyFileToFileServer(response.getInputStream(), fileName);
                objectStorageClient.close();
            } else {
                objectStorageClient.close();
                throw new RuntimeException("Failed to download file from bucket: " + bucketName);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void copyFileToFileServer(InputStream inputStream, String fileName) {
        String directoryPath = "/opt/oci/file";
        logger.info("Starting to copy file: [{}] to file server: [{}] ", fileName, directoryPath);
        File file = new File(directoryPath, fileName);

        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            logger.error("Error occurred while copying file to file server: ", e);
            throw new RuntimeException(e);
        }

    }

}
