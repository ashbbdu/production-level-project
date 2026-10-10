package com.api_task_management.security;

import com.api_task_management.user.dto.type.UserRole;
import com.api_task_management.user.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

   private final UserEntity user;

   public CustomUserDetails(UserEntity user) {
      this.user = user;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      UserRole role = user.getRole();
      if(role == null) return List.of();
      return List.of(new SimpleGrantedAuthority("ROLE_"+ role.toString()));
   }

   @Override
   public String getPassword() {
      return user.getPassword();
   }

   @Override
   public String getUsername() {
      return user.getEmail();
   }

   @Override
   public boolean isEnabled() {
      return user.isEnabled();
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }
}