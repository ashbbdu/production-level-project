package com.api_task_management.auth.service;

import com.api_task_management.auth.dto.request.LoginRequest;
import com.api_task_management.auth.dto.response.LoginResponse;
import com.api_task_management.common.exception.BusinessException;
import com.api_task_management.common.exception.ResourceNotFoundException;
import com.api_task_management.security.jwt.JwtService;
import com.api_task_management.user.entity.UserEntity;
import com.api_task_management.user.repository.UserRepository;
import com.api_task_management.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Override
    public LoginResponse login(LoginRequest request) {
//
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                    new ResourceNotFoundException("User not found !")
                );

//        change this to 405 Not Allowed

//        why we handled this here because if a disabled user is trying to login , it will go through

//        The reason is that this check belongs to your JWT authentication flow, but the stack trace you shared earlier came from your login flow. These are two separate execution paths.
        //Let's trace exactly what's happening in your application.
        //1. When a user logs in
        //Your AuthServiceImpl.login() calls:
        //authenticationManager.authenticate(
        //        new UsernamePasswordAuthenticationToken(
        //                request.getEmail(),
        //                request.getPassword()
        //        )
        //);

//        Spring Security checks whether the account is enabled during authenticate(). If the account is disabled, it throws:
        //DisabledException: User is disabled
        //
        //
        //Your JWT filter's account-status check doesn't handle this because the exception occurs in the login flow, not after the filter has successfully loaded a user.
        //You've addressed this case by checking user.isEnabled() before calling authenticate() in AuthServiceImpl.


//        When a user sends an existing JWT
        //This is where your JWT filter's check applies.
        //The flow is:
        //Request with Bearer token
        //
        //JWT filter validates token
        //
        //Load user through UserDetailsService
        //
        //Check user.isEnabled()
        //
        //If disabled: write 401 and return
        //The request must not continue to the controller.
        //
        //
        //Your code is correct for this path:
        //if (!user.isEnabled()
        //        || !user.isAccountNonLocked()
        //        || !user.isAccountNonExpired()
        //        || !user.isCredentialsNonExpired()) {
        //
        //    writeUnauthorized(
        //            request,
        //            response,
        //            "User account is not available"
        //    );
        //    return;
        if(!user.isEnabled()) {
            throw  new BusinessException("Your account is disabled or inactive. Please contact the administrator.");
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
        );

        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());
        return new LoginResponse(token);
    }
}
