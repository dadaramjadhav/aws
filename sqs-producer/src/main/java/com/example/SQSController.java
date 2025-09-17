package com.example;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/sqs")
public class SQSController {

    private final SQSProducer sqsProducer;

    @PostMapping("/send")
    public String sendMessage(@RequestBody Employee emp) {
        sqsProducer.sendMessage(emp);

        return "message sent: " + emp;
    }

}