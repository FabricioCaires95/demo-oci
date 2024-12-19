package org.example.demoreceiver;

import org.example.demoreceiver.model.Exchange;
import org.example.demoreceiver.service.ExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExchangeServiceTest {

    @InjectMocks
    private ExchangeService exchangeService;

    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    private WebClient.RequestBodySpec requestBodyMock;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

    @Mock
    private CustomMinimalForTestResponseSpec customMinimalForTestResponseSpec;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRequestSuccess() {
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri("http://localhost:8080/exchanges")).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.onStatus(any(Predicate.class), any(Function.class))).thenReturn(responseMock);
        when(responseMock.bodyToMono(ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockExchangeListResponse()));

        var response = exchangeService.getExchanges();

        StepVerifier.create(response)
                .expectNextMatches(exchanges -> exchanges.size() == 3)
                .verifyComplete();
    }


    @Test
    public void testRequestSuccess2() {
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri("http://localhost:8080/exchanges")).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(customMinimalForTestResponseSpec);
        when(customMinimalForTestResponseSpec.getStatus()).thenReturn(HttpStatus.OK);
        when(customMinimalForTestResponseSpec.onStatus(any(Predicate.class), any(Function.class))).thenCallRealMethod();
        when(customMinimalForTestResponseSpec.bodyToMono(ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockExchangeListResponse()));

        var response = exchangeService.getExchanges();

        StepVerifier.create(response)
                .expectNextMatches(exchanges -> exchanges.size() == 3)
                .verifyComplete();
    }


    private List<Exchange> mockExchangeListResponse() {
        return List.of(new Exchange("test1", "edr.str"),
                new Exchange("test2", "edr.str"),
                new Exchange("test3", "edr.str"));
    }

    abstract class CustomMinimalForTestResponseSpec implements WebClient.ResponseSpec {

        public abstract HttpStatus getStatus();

        public WebClient.ResponseSpec onStatus(Predicate<HttpStatusCode> statusPredicate, Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction) {
            if (statusPredicate.test(this.getStatus())) exceptionFunction.apply(ClientResponse.create(HttpStatus.OK).build()).block();
            return this;
        }

    }


}

