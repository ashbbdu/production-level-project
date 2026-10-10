package com.api_task_management.security;

import com.api_task_management.common.exception.ResourceNotFoundException;
import com.api_task_management.user.entity.UserEntity;
import com.api_task_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return (UserDetails) userRepository.findByEmail(username); this is casting but we are using our Custom service
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() ->
                    new UsernameNotFoundException("User with email : " + username + " not found !")
                );
        return new CustomUserDetails(user);
    }
}
