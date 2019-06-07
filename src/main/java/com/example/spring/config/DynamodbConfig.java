package com.example.spring.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.AwsRegionProvider;
import com.amazonaws.regions.DefaultAwsRegionProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DynamodbConfig implements InitializingBean {

	@Autowired
	DynamodbProperties properties;

	AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

	AwsRegionProvider regionProvider = new DefaultAwsRegionProviderChain();

	AmazonDynamoDB amazonDynamoDB;

	@Bean
	public AWSCredentials amazonAWSCredentials() {

		return credentialsProvider.getCredentials();
	}

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {

		if (amazonDynamoDB != null) {
			return amazonDynamoDB;
		}

		AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClient.builder();

		builder.withCredentials(credentialsProvider);

		if (StringUtils.isNotEmpty(properties.endPoint)) {

			log.debug("developements");

			String defaultRegion = regionProvider.getRegion();
			builder.withEndpointConfiguration(
					new EndpointConfiguration(
							properties.getEndPoint(),
							StringUtils.defaultString(properties.getRegion(), defaultRegion)));
		} else {
			log.debug("production");
		}

		return amazonDynamoDB = builder.build();
	}

	@Bean
	public DynamoDBMapper dynamoDBMapper() {

		return new DynamoDBMapper(amazonDynamoDB(), dynamoDBMapperConfig());
	}

	@Bean
	public DynamoDBMapperConfig dynamoDBMapperConfig() {

		return DynamoDBMapperConfig.DEFAULT;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		TableUtils.createTableIfNotExists(
				amazonDynamoDB(),
				users());
	}

	private CreateTableRequest users() {

		List<AttributeDefinition> attributeDefinitions = new ArrayList<>();

		attributeDefinitions.add(column("id", ScalarAttributeType.S));
		attributeDefinitions.add(column("username", ScalarAttributeType.S));
		attributeDefinitions.add(column("email", ScalarAttributeType.S));

		List<KeySchemaElement> keySchema = new ArrayList<>();
		keySchema.add(key("id", KeyType.HASH));

		List<GlobalSecondaryIndex> globalSecondaryIndexs = new ArrayList<>();
		globalSecondaryIndexs.add(index("users_email", "email", KeyType.HASH));
		globalSecondaryIndexs.add(index("users_username", "username", KeyType.HASH));

		return new CreateTableRequest()
				.withTableName("users")
				.withKeySchema(keySchema)
				.withAttributeDefinitions(attributeDefinitions)
				.withGlobalSecondaryIndexes(globalSecondaryIndexs)
				.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
	}

	private AttributeDefinition column(String attributeName, ScalarAttributeType attributeType) {

		return new AttributeDefinition(attributeName, attributeType);
	}

	private KeySchemaElement key(String attributeName, KeyType keyType) {

		return new KeySchemaElement(attributeName, keyType);
	}

	private GlobalSecondaryIndex index(String indexName, String attributeName, KeyType keyType) {

		return new GlobalSecondaryIndex()
				.withIndexName(indexName)
				.withKeySchema(key(attributeName, keyType))
				.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
				.withProjection(new Projection().withProjectionType(ProjectionType.ALL));
	}

}
