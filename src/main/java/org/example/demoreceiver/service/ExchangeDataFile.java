package org.example.demoreceiver.service;

import org.example.demoreceiver.model.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeDataFile implements ExchangeDataProvider {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeDataFile.class);


    @Override
    public List<Exchange> fetchExchange() {
        System.out.println("Fetching exchanges via FILE");
        var exchanges = readDataFromFile();

        if (exchanges.isEmpty()) {
            throw new RuntimeException("No exchanges found");
        }
        return exchanges;
    }

    private List<Exchange> readDataFromFile() {
        File file = new File("/home/spacer/IdeaProjects/demo-receiver/wscloud.properties");
        List<Exchange> exchangeList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                Exchange exchange = parseExchange(line);
                exchangeList.add(exchange);
            }
            return exchangeList;

        } catch (IOException e) {
            logger.error("Error to read file: {}", e.getMessage());
            return Collections.emptyList();
        }

    }

    private Exchange parseExchange(String line) {
        Map<String, String> properties = new HashMap<>();

        String[] pairs = line.split("\\|");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                properties.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        String bucketName = properties.get("bucketName");
        String endpoint = properties.get("endpoint");
        Integer cloud = Integer.parseInt(properties.get("cloud"));
        Integer exchangeCode = Integer.parseInt(properties.get("exchangeCode"));
        String streamingPool = properties.get("streamingPool");
        String namespace = properties.get("namespace");
        String bootstrapServer = properties.get("bootstrapServer");

        return new Exchange(bucketName, endpoint, exchangeCode, cloud, streamingPool, namespace, bootstrapServer);
    }

}
