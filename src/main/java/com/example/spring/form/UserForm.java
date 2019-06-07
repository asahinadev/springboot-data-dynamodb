package com.example.spring.form;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.spring.entity.Roles;
import com.example.spring.validation.group.Create;
import com.example.spring.validation.group.Save;

import lombok.Data;

@Data
public class UserForm {

	String id;

	@NotEmpty(groups = { Save.class, Create.class })
	@Pattern(regexp = "[\\w]+", groups = { Save.class, Create.class })
	@Length(min = 4, max = 16, groups = { Save.class, Create.class })
	String username;

	@NotEmpty(groups = Create.class)
	@Email(groups = { Save.class, Create.class })
	@Length(min = 4, max = 255, groups = { Save.class, Create.class })
	String email;

	@NotEmpty(groups = { Save.class, Create.class })
	@Pattern(regexp = "[\\w]+", groups = { Save.class, Create.class })
	@Length(min = 8, max = 16, groups = { Save.class, Create.class })
	String password;

	boolean enabled;

	boolean locked;

	LocalDateTime credentialsExpired;

	LocalDateTime accountExpired;

	List<Roles> authorities;
}
