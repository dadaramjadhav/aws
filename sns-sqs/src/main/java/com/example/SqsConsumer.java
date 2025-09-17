package com.example;

import org.springframework.stereotype.Service;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Service
public class SqsConsumer {

    @SqsListener("${app.sqs.queue-url}")
    public void receive(Employee employee) {
        System.out.println("Received message: " + employee);
    }
}
