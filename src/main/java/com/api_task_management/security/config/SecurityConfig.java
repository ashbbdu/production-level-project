package com.api_task_management.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

//    In memory user detailsservice
@Bean
public AuthenticationProvider authenticationProvider(
        @Qualifier("users") UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {

    DaoAuthenticationProvider provider =
            new DaoAuthenticationProvider(userDetailsService);

    provider.setPasswordEncoder(passwordEncoder);

    return provider;
}
    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filter (HttpSecurity http , AuthenticationProvider authenticationProvider) {
//        http
//
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/api/auth/user/**")
//                        .permitAll().anyRequest().authenticated())
////                authenticationProvider(authenticationProvider)
//                ;
//
//
//        return http.build();
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/user/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }


    @Bean
    public UserDetailsService users (PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin@yopmail.com")
                .password(passwordEncoder.encode("abcde"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user@yopmail.com")
                .password(passwordEncoder.encode("abcde"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin , user);
    }

    @Bean
    public AuthenticationManager getAuthenticationManager (AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }
}
