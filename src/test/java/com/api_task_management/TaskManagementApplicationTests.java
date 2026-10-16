package com.api_task_management;

import com.api_task_management.user.dto.request.CreateUserRequest;
import com.api_task_management.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaskManagementApplicationTests {


	@Test
	void contextLoads() {
	}

	@Test
	void shouldReturnFive () {
		int result = 2 + 3;
		assertEquals(5, result);
	}

}
