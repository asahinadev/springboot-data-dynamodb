package com.example.spring.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class LocalDateTimeConverter
		implements DynamoDBTypeConverter<String, LocalDateTime> {

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	@Override
	public String convert(LocalDateTime object) {

		if (object == null) {
			return null;
		}

		return object.format(formatter);
	}

	@Override
	public LocalDateTime unconvert(String object) {

		if (object == null) {
			return null;
		}

		return LocalDateTime.parse(object, formatter);
	}

}
