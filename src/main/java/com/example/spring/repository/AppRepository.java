package com.example.spring.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;

public interface AppRepository<ENTITY, HASH_KEY, RANGE_KEY> {

	public PaginatedQueryList<ENTITY> query(DynamoDBQueryExpression<ENTITY> queryExpression);

	public int count(DynamoDBQueryExpression<ENTITY> queryExpression);

	public int count(DynamoDBScanExpression scanExpression);

	public int count();

	public ENTITY load(HASH_KEY hashKey, RANGE_KEY rangeKey);

	public ENTITY load(HASH_KEY hashKey);

	public PaginatedScanList<ENTITY> scan();

	public PaginatedScanList<ENTITY> scan(DynamoDBScanExpression scanExpression);

	public Map<String, List<Object>> batchLoad(Map<Class<?>, List<KeyPair>> itemsToGet);

	public void save(ENTITY entity);

	public void batchSave(List<ENTITY> entities);

	public void delete(ENTITY entity);

	public void batchDelete(List<ENTITY> entities);

	public PaginatedQueryList<ENTITY> query(QueryRequest queryRequest);

	public List<ENTITY> findBy(String key, String value, String id);

	public Optional<ENTITY> findById(HASH_KEY id);

	public boolean existsById(HASH_KEY id);

	public List<ENTITY> findAll();

	public List<ENTITY> findAllOrderBy(Comparator<ENTITY> comparator);

	public void deleteAll();
}
