package com.example.spring.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.example.spring.util.ClassUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AppRepositoryImpl<ENTITY, HASH_KEY, RANGE_KEY> {

	@Autowired
	protected DynamoDBMapper dynamoDBMapper;

	@Autowired
	protected DynamoDBMapperConfig dynamoDBMapperConfig;

	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	protected Class<ENTITY> domainClass() {

		return ClassUtil.getGenericType(this, AppRepositoryImpl.class, 0);
	}

	public PaginatedQueryList<ENTITY> query(DynamoDBQueryExpression<ENTITY> queryExpression) {

		return dynamoDBMapper.query(domainClass(), queryExpression);
	}

	public int count(DynamoDBQueryExpression<ENTITY> queryExpression) {

		return dynamoDBMapper.count(domainClass(), queryExpression);
	}

	public int count(DynamoDBScanExpression scanExpression) {

		return dynamoDBMapper.count(domainClass(), scanExpression);
	}

	public int count() {

		DynamoDBQueryExpression<ENTITY> queryExpression = new DynamoDBQueryExpression<>();
		return dynamoDBMapper.count(domainClass(), queryExpression);
	}

	public ENTITY load(HASH_KEY hashKey, RANGE_KEY rangeKey) {

		return dynamoDBMapper.load(domainClass(), hashKey, rangeKey);
	}

	public ENTITY load(HASH_KEY hashKey) {

		return dynamoDBMapper.load(domainClass(), hashKey);
	}

	public PaginatedScanList<ENTITY> scan() {

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		return dynamoDBMapper.scan(domainClass(), scanExpression);
	}

	public PaginatedScanList<ENTITY> scan(DynamoDBScanExpression scanExpression) {

		return dynamoDBMapper.scan(domainClass(), scanExpression);
	}

	public Map<String, List<Object>> batchLoad(Map<Class<?>, List<KeyPair>> itemsToGet) {

		return dynamoDBMapper.batchLoad(itemsToGet);
	}

	public void save(ENTITY entity) {

		dynamoDBMapper.save(entity);
	}

	public void batchSave(List<ENTITY> entities) {

		dynamoDBMapper.batchSave(entities);
	}

	public void delete(ENTITY entity) {

		dynamoDBMapper.delete(entity);
	}

	public void batchDelete(List<ENTITY> entities) {

		dynamoDBMapper.batchDelete(entities);
	}

	public PaginatedQueryList<ENTITY> query(QueryRequest queryRequest) {

		QueryResult queryResult = amazonDynamoDB.query(queryRequest);
		return new PaginatedQueryList<ENTITY>(dynamoDBMapper, domainClass(), amazonDynamoDB, queryRequest, queryResult,
				dynamoDBMapperConfig.getPaginationLoadingStrategy(), dynamoDBMapperConfig);
	}

	public List<ENTITY> findBy(String key, String value, String id) {

		Map<String, AttributeValue> eav = new HashMap<>();
		Map<String, String> ean = new HashMap<>();

		DynamoDBScanExpression query = new DynamoDBScanExpression();

		eav.put(":" + key, new AttributeValue().withS(value));
		StringBuilder builder = new StringBuilder();

		builder.append(String.format("#_%s = :%s", key, key));
		ean.put("#_" + key, key);

		if (!StringUtils.isEmpty(id)) {
			eav.put(":id", new AttributeValue().withS(id));
			builder.append(" AND NOT id = :id");
		}

		log.debug(builder.toString());

		query.withFilterExpression(builder.toString());
		query.withExpressionAttributeValues(eav);
		query.withExpressionAttributeNames(ean);

		return scan(query);
	}

	public List<ENTITY> index(String indexName, String key, String value) {

		HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":v1", new AttributeValue().withS(value));

		DynamoDBQueryExpression<ENTITY> queryExpression = new DynamoDBQueryExpression<>();
		queryExpression.withIndexName(indexName)
				.withConsistentRead(false)
				.withKeyConditionExpression(key + " = :v1")
				.withExpressionAttributeValues(eav);

		return dynamoDBMapper.query(domainClass(), queryExpression);
	}

	public Optional<ENTITY> findById(HASH_KEY id) {

		ENTITY users = load(id);
		if (users == null) {
			return Optional.empty();
		}
		return Optional.of(users);
	}

	public boolean existsById(HASH_KEY id) {

		return findById(id).isPresent();
	}

	public List<ENTITY> findAll() {

		return scan();
	}

	public List<ENTITY> findAllOrderBy(Comparator<ENTITY> comparator) {

		List<ENTITY> ret = new ArrayList<>(scan());
		Collections.sort(ret, comparator);
		return ret;
	}

	public void deleteAll() {

		Iterable<ENTITY> entities = findAll();
		for (ENTITY entity : entities) {
			delete(entity);
		}
	}

}
