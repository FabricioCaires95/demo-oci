package org.example.demoreceiver.service;

import org.example.demoreceiver.model.CloudProvider;
import org.example.demoreceiver.model.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class ConsumerService {

    private static Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    private final ExecutorService executorService;
    private final ConcurrentKafkaListenerContainerFactory<String, String> kafkaMessageListener;
    private final CloudStorageStrategyManager cloudStorageStrategyManager;

    public ConsumerService(ExecutorService executorService,
                           ConcurrentKafkaListenerContainerFactory<String, String> kafkaMessageListener,
                           CloudStorageStrategyManager cloudStorageStrategyManager) {
        this.executorService = executorService;
        this.kafkaMessageListener = kafkaMessageListener;
        this.cloudStorageStrategyManager = cloudStorageStrategyManager;
    }

    public void startConsumers(List<Exchange> exchanges) {
        for (Exchange exchange : exchanges) {
            executorService.submit(() -> {
                startConsumer(exchange);
            });
        }
    }

    private void startConsumer(Exchange exchange) {
        ConcurrentMessageListenerContainer<String, String> container = kafkaMessageListener.createContainer(exchange.streamingPool());
        container.setupMessageListener((MessageListener<String, String>) data -> {
            logger.info("Current thread: {}", Thread.currentThread().getName());
            logger.info("Received message {}", data);

            CloudStorage cloudStorage = cloudStorageStrategyManager.getStrategy(CloudProvider.getCloudProvider(1));

            cloudStorage.downloadFileFromBucket(exchange.bucketName(), exchange.streamingPool());
        });
        container.start();
    }

}
