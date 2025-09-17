package com.example;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Service
public class SQSConsumer {
    @Autowired
    private ObjectMapper objectMapper;

    @SqsListener(value = "${app.sqs.employee-queue}")
    public void receiveMessage(String json) {
        try {
            Employee emp = objectMapper.readValue(json, Employee.class);
            System.out.println("Received: " + emp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
