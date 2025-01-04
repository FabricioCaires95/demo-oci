package org.example.demoreceiver;

import org.example.demoreceiver.model.Exchange;
import org.example.demoreceiver.service.ConsumerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {

    private final ConsumerService consumerService;

    public StartupRunner(ConsumerService consumerService) {
        this.consumerService = consumerService;

    }

    @Override
    public void run(String... args) throws Exception {
        Exchange e1 = new Exchange("fpbu-spacer-test", "legend-oci-teste.txt");
        //Exchange e2 = new Exchange("test.end2", "oci1.orange");
        List<Exchange> exchanges = List.of(e1);

        consumerService.startConsumers(exchanges);
    }
}
