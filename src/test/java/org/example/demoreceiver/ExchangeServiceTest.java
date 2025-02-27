package org.example.demoreceiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.example.demoreceiver.model.Exchange;
import org.example.demoreceiver.model.exception.ExchangeException;
import org.example.demoreceiver.service.ExchangeService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExchangeServiceTest {

    private ExchangeService exchangeService;

    private static MockWebServer mockWebServer;

    String endpoint;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        endpoint = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder()
                .baseUrl(endpoint)
                .build();
        exchangeService = new ExchangeService(webClient);
    }


    @Test
    public void testRequestSuccess() throws JsonProcessingException {
        var exchanges = mockExchangeListResponse();

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(exchanges))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        var response = exchangeService.fetchExchange();

        assertNotNull(response);
        assertEquals(3, response.size());
        assertEquals("test1", response.getFirst().bucketName());
    }


    @Test
    public void testRequestFailure() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Error while fetching exchanges via API"));

        var exception = assertThrows(ExchangeException.class, () -> exchangeService.fetchExchange());

        assertTrue(exception.getMessage().contains("Error while fetching exchanges via API"));
    }


    private List<Exchange> mockExchangeListResponse() {
        return List.of(new Exchange("test1", "edr.str0", 133, 1, "namsp", "endpt", "bootsra1"),
                new Exchange("test2", "edr.str1", 8788, 1, "namsp1", "endpt1", "bootsra2"),
                new Exchange("test3", "edr.str2", 133, 1, "namesp2", "endpoint2", "bootsra3"));
    }
}

