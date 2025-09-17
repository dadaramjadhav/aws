package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@Component
public class SnsSubscriptionConfig {

    private final SnsClient snsClient;

    @Value("${app.sns.topic-arn}")
    private String topicArn;

    @Value("${app.sqs.queue-url}")
    private String queueUrl;

    public SnsSubscriptionConfig(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void subscribeQueueToTopic() {
        String queueArn = queueUrl;

        SubscribeRequest request = SubscribeRequest.builder()
                .protocol("sqs")
                .endpoint(queueArn)
                .returnSubscriptionArn(true)
                .topicArn(topicArn)
                .build();

        snsClient.subscribe(request);

        System.out.println("Subscribed SQS queue to SNS topic");
    }
}
