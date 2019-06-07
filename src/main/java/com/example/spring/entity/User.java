package com.example.spring.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.example.spring.converter.LocalDateTimeConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "users")
public class User
		implements UserDetails {

	@DynamoDBHashKey
	@DynamoDBTyped(DynamoDBAttributeType.S)
	String id;

	@DynamoDBAttribute
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "users_username", attributeName = "username")
	@DynamoDBTyped(DynamoDBAttributeType.S)
	String username;

	@DynamoDBAttribute
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "users_email", attributeName = "email")
	@DynamoDBTyped(DynamoDBAttributeType.S)
	String email;

	@DynamoDBAttribute
	@DynamoDBTyped(DynamoDBAttributeType.S)
	String password;

	@DynamoDBAttribute
	@DynamoDBTyped(DynamoDBAttributeType.BOOL)
	boolean enabled;

	@DynamoDBAttribute
	@DynamoDBTyped(DynamoDBAttributeType.BOOL)
	boolean locked;

	@DynamoDBAttribute(attributeName = "credentials_expired")
	@DynamoDBTyped(DynamoDBAttributeType.S)
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	LocalDateTime credentialsExpired;

	@DynamoDBAttribute(attributeName = "account_expired")
	@DynamoDBTyped(DynamoDBAttributeType.S)
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	LocalDateTime accountExpired;

	@DynamoDBIgnore
	List<Roles> authorities;

	@DynamoDBIgnore
	public List<Roles> getAuthorities() {

		if (authorities == null || authorities.isEmpty()) {
			return Arrays.asList(Roles.GUEST);
		}
		return authorities;
	}

	@Override
	@DynamoDBIgnore
	public boolean isAccountNonExpired() {

		if (accountExpired == null) {
			return true;
		}
		return accountExpired.isAfter(LocalDateTime.now());
	}

	@Override
	@DynamoDBIgnore
	public boolean isAccountNonLocked() {

		return !locked;
	}

	@Override
	@DynamoDBIgnore
	public boolean isCredentialsNonExpired() {

		if (credentialsExpired == null) {
			return true;
		}
		return credentialsExpired.isAfter(LocalDateTime.now());
	}

}
