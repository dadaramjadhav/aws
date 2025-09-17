package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.operations.SqsTemplate;

@Service
public class SQSProducer {

    private SqsTemplate sqsTemplate;
    private final String queueUrl;
    @Autowired
    private ObjectMapper objectMapper;

    public SQSProducer(SqsTemplate sqsTemplate, @Value("${app.sqs.employee-queue}") String queueUrl) {
        this.sqsTemplate = sqsTemplate;
        this.queueUrl = queueUrl;
    }

    public void sendMessage(Employee emp) {
        try {
            String json = objectMapper.writeValueAsString(emp);
            sqsTemplate.send(builder -> builder.queue(queueUrl).payload(json));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("message sent: " + emp);

    }
}
