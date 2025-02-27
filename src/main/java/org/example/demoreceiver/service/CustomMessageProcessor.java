package org.example.demoreceiver.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.demoreceiver.model.CloudProvider;
import org.example.demoreceiver.model.Exchange;
import org.example.demoreceiver.model.FileData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

public class CustomMessageProcessor implements AcknowledgingMessageListener<String, FileData> {

    private static Logger logger = LoggerFactory.getLogger(CustomMessageProcessor.class);

    private final CloudStorageStrategyManager cloudStorageStrategyManager;
    private final Notification notification;
    private final Exchange exchange;

    public CustomMessageProcessor(CloudStorageStrategyManager cloudStorageStrategyManager, Notification notification, Exchange exchange) {
        this.cloudStorageStrategyManager = cloudStorageStrategyManager;
        this.notification = notification;
        this.exchange = exchange;
    }

    @Override
    public void onMessage(ConsumerRecord<String, FileData> data, Acknowledgment acknowledgment) {
        try {
            logger.info("Current thread: {}", Thread.currentThread().getName());
            logger.info("Received message {}", data);

            var messageData = data.value();

            CloudStorage cloudStorage = cloudStorageStrategyManager.getStrategy(CloudProvider.getCloudProvider(exchange.cloudId()));

            cloudStorage.downloadFileFromBucket(exchange.bucketName(), exchange.streamingPool());

            //notification.notification(messageData);

            acknowledgment.acknowledge();
        } catch (KafkaException e) {
            logger.error("Error processing message; {}", e.getMessage());
        }

    }
}
