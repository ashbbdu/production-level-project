package com.api_task_management.user;

import com.api_task_management.common.exception.BusinessException;
import com.api_task_management.common.exception.ResourceAlreadyExistsException;
import com.api_task_management.common.exception.ResourceNotFoundException;
import com.api_task_management.user.dto.response.UserResponse;
import com.api_task_management.user.dto.type.UserRole;
import com.api_task_management.user.entity.UserEntity;
import com.api_task_management.user.repository.UserRepository;
import com.api_task_management.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void shouldReturnUserById () {
        Long userId = 5L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("ashish@example.com");
        user.setFirstName("Ashish");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(userId);

        assertEquals(userId , response.getId());
        assertEquals("ashish@example.com" , response.getEmail());
        assertEquals("Ashish" , response.getFirstName());

        // Verify: Check repository interaction
        verify(userRepository).findById(userId);
    }

    @Test
    public void shouldThrowExceptionWhenUserDoesNotExist () {
        Long userId = 99L;

//        so i can say () -> userService.getUserById(userId) by execting this we are expecting ResourceNotFoundException.class this to trigger ?
        //Yes, you're very close! Just one small correction in your wording.
        //
        //You can say:
        //
        //“When JUnit executes () -> userService.getUserById(userId), we expect it to throw ResourceNotFoundException. JUnit checks whether that expected exception occurs.”
        //
        //Let's look at the code again:
        //
        //assertThrows(
        //    ResourceNotFoundException.class,
        //    () -> userService.getUserById(userId)
        //);
        //
        //ResourceNotFoundException.class — specifies the exception type we expect.
        //
        //() -> userService.getUserById(userId) — provides the code JUnit should execute.
        //
        //assertThrows() — executes that code and verifies whether the expected exception is thrown.
        //
        //Important: ResourceNotFoundException.class doesn't trigger the exception. Your service triggers it when the user doesn't exist. JUnit checks whether the service throws the expected exception.
        //
        //Think of it like this:
        //
        //JUnit executes your service call.
        //
        //The service doesn't find the user.
        //
        //The service throws ResourceNotFoundException.
        //
        //JUnit catches the exception and marks the test as passed.
        //
        //You've understood the core concept of exception testing! Next, we can move on to Mockito's verify() method.

        when(userRepository.findById(userId)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class , () -> userService.getUserById(userId));

    }

    @Test
    public void shouldApproveUserAndAssignRole () {
        Long userId = 5L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("ashish@example.com");
        user.setFirstName("Ashish");
        user.setRole(UserRole.USER);
        user.setEnabled(false); // if we set it to true then we will get user already enabled exception

        //we have saved the user in the Java Memory and not with using when
//        2. Break down the Mockito line
//Part A: when(...)
//when(userRepository.findById(userId))
//
//
//This identifies the repository call whose behavior we want to configure.
//
//
//Part B: .thenReturn(...)
//.thenReturn(Optional.of(user));
//
//
//This tells Mockito what to return when that repository call occurs: the user you created, wrapped in an Optional.
//
//
//In plain English, the whole line means:
//"Mockito, whenever my service calls userRepository.findById(5L), return the user object I created above."
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserResponse response = userService.approveUser(userId , UserRole.USER);

        assertEquals(true , response.isEnabled());
        assertEquals(user.getRole() , response.getRole());

    }

    @Test
    public void shouldThrowExceptionWhenUserAlreadyEnabled () {
        Long userId = 100L;

        UserEntity user = new UserEntity();
        user.setEnabled(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ResourceAlreadyExistsException.class , () -> userService.approveUser(userId , UserRole.USER));
    }

    @Test
    public void shouldThrowExceptionWhenRoleIsNull () {
        Long userId = 100L;

        assertThrows(BusinessException.class , () -> userService.approveUser(userId , null));
    }

    @Test
    public void shouldDisableUser () {
        Long userId = 100L;
        UserEntity user = new UserEntity();

        user.setId(userId);
        user.setEmail("ashish@example.com");
        user.setFirstName("Ashish");
        user.setEnabled(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user)); // it creates

        UserResponse response = userService.disableUser(userId);

//        assertEquals(false , response.isEnabled());
        assertFalse(response.isEnabled()); // this is the simpler reason of the above line

    }

    @Test
    public void shouldThrowExceptionWhenUserAlreadyDisabled () {
        Long userId = 101L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("ashish@example.com");
        user.setFirstName("Ashish");
        user.setEnabled(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ResourceAlreadyExistsException.class , () -> userService.disableUser(userId));

    }

    @Test
    public void shouldThrowUserDoesNotExistWhileDisabling () {
        Long userId = 101L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class , () -> userService.disableUser(userId));

    }

}
