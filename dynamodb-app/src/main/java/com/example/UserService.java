package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

	private final DynamoDbClient dynamoDbClient;
	private final String TABLE_NAME = "Users";

	public void createTableIfNotExists() {
		try {
			dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName(TABLE_NAME).build());
		} catch (ResourceNotFoundException e) {
			dynamoDbClient.createTable(CreateTableRequest.builder().tableName(TABLE_NAME)
					.keySchema(KeySchemaElement.builder().attributeName("userId").keyType(KeyType.HASH).build())
					.attributeDefinitions(AttributeDefinition.builder().attributeName("userId")
							.attributeType(ScalarAttributeType.S).build())
					.billingMode(BillingMode.PAY_PER_REQUEST).build());
		}
	}

	public void createUser(User user) {
		log.info("Creating user: {}", user);
		Map<String, AttributeValue> item = new HashMap<>();
		item.put("userId", AttributeValue.fromS(user.getUserId()));
		item.put("name", AttributeValue.fromS(user.getName()));
		item.put("email", AttributeValue.fromS(user.getEmail()));
		dynamoDbClient.putItem(PutItemRequest.builder().tableName(TABLE_NAME).item(item).build());
	}

	public List<User> getAllUsers() {
		log.info("Getting all users");
		 ScanResponse scanResponse = dynamoDbClient.scan(
		            ScanRequest.builder()
		                       .tableName(TABLE_NAME)
		                       .build());

		    return scanResponse.items().stream()
		            .map(item -> {
		                User user = new User();
		                user.setUserId(item.get("userId").s());
		                user.setName(item.get("name").s());
		                user.setEmail(item.get("email").s());
		                return user;
		            })
		            .toList();
	}

	public User getUserById(String id) {
		log.info("Getting user by id: {}", id);
		Map<String, AttributeValue> key = Map.of("userId", AttributeValue.fromS(id));
		GetItemResponse resp = dynamoDbClient.getItem(r -> r.tableName(TABLE_NAME).key(key));
		if (resp.hasItem()) {
			Map<String, AttributeValue> item = resp.item();
			User user = new User();
			user.setUserId(item.get("userId").s());
			user.setName(item.get("name").s());
			user.setEmail(item.get("email").s());
			return user;
		}
		return null;
	}

}
