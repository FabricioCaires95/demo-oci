package org.example.demoreceiver.service;

import org.example.demoreceiver.configuration.KafkaConfig;
import org.example.demoreceiver.model.Exchange;
import org.example.demoreceiver.model.FileData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class ConsumerService {

    private static Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    private final ExecutorService executorService;
    private final KafkaConfig kafkaConfig;
    private final CloudStorageStrategyManager cloudStorageStrategyManager;
    private final Notification notification;

    public ConsumerService(ExecutorService executorService,
                           KafkaConfig kafkaConfig,
                           CloudStorageStrategyManager cloudStorageStrategyManager, Notification notification) {
        this.executorService = executorService;
        this.kafkaConfig = kafkaConfig;
        this.cloudStorageStrategyManager = cloudStorageStrategyManager;
        this.notification = notification;
    }

    public void startConsumers(List<Exchange> exchanges) {
        for (Exchange exchange : exchanges) {
            executorService.submit(() -> {
                logger.info("Starting consumer for exchange {}", exchange);
                ConcurrentKafkaListenerContainerFactory<String, FileData> kafkaFactory = kafkaConfig.consumerFactory(exchange.bootstrapServer());
                startConsumer(exchange, kafkaFactory);
            });
        }
    }

    private void startConsumer(Exchange exchange, ConcurrentKafkaListenerContainerFactory<String, FileData> factory) {
        ConcurrentMessageListenerContainer<String, FileData> container = factory.createContainer(exchange.streamingPool());
        final CustomMessageProcessor customMessageProcessor = new CustomMessageProcessor(cloudStorageStrategyManager, notification, exchange);
        container.setupMessageListener(customMessageProcessor);
        container.start();
    }

}
