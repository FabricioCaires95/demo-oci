package org.example.demoreceiver;

import org.example.demoreceiver.model.Exchange;
import org.example.demoreceiver.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoReceiverApplication {

	@Autowired
	private ConsumerService consumerService;

	public static void main(String[] args) {
		SpringApplication.run(DemoReceiverApplication.class, args);
	}
}
