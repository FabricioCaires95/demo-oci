package org.example.demoreceiver;


import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.example.demoreceiver.model.Data;
import org.example.demoreceiver.model.FileData;
import org.example.demoreceiver.service.Notification;
import org.example.demoreceiver.service.NotificationServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebClientTest {

    private Notification notification;

    private static MockWebServer mockWebServer;

    private FileData fileData;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        WebClient webClient = WebClient.builder().build();
        notification = new NotificationServiceImpl(webClient);

        String endpoint = mockWebServer.url("/").toString();
        Data data = new Data("/opt/fste/transfer", "ARQ.TD00", endpoint);
        fileData =  new FileData("http://localhost:8080", "RTC_SYSTEM", data, false, "teste");
    }

    @AfterEach
    void tearDownServer() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void test_getExchangeSuccess() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                        .setBody("Success")
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setHeader("Authorization", "xxxxx")
                .setResponseCode(200));

        var response = notification.notification(fileData);

        StepVerifier.create(response)
                .expectNext("Success")
                .verifyComplete();

        var request = mockWebServer.takeRequest();

    }

}
