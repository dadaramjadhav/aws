package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class SnsPublisher {

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;
    private final String topicArn;

    public SnsPublisher(SnsClient snsClient,
            ObjectMapper objectMapper,
            @Value("${app.sns.topic-arn}") String topicArn) {
        this.snsClient = snsClient;
        this.objectMapper = objectMapper;
        this.topicArn = topicArn;
    }

    public void publish(Employee employee) throws Exception {
        // employee.setJoiningDate(LocalDate.of(2021, 05, 31));
        String message = objectMapper.writeValueAsString(employee);

        String messageGroupId = "employee-group";
        String messageDeduplicationId = UUID.randomUUID().toString();

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .messageGroupId(messageGroupId)
                .messageDeduplicationId(messageDeduplicationId)
                .build();

        snsClient.publish(request);

        System.out.println("Published message to FIFO SNS: " + employee);
    }
}
