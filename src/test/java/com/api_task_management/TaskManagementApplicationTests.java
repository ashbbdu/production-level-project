package com.api_task_management;

import com.api_task_management.user.dto.request.CreateUserRequest;
import com.api_task_management.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class TaskManagementApplicationTests {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	public CreateUserRequest testRequest () {
		CreateUserRequest request = new CreateUserRequest();
		request.setEmail("ashish@example.com");
		request.setFirstName("Ashish");
		request.setLastName("Srivastava");
		request.setPassword(passwordEncoder.encode("SecurePassword@123"));

		return request;
	}


	@Test
	void contextLoads() {
	}

//	@Test
	void testUserCreation () {
		userService.createUser(testRequest());
	}

}
