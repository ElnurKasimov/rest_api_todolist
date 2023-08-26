package com.softserve.itacademy.todolist.security;

import com.softserve.itacademy.todolist.model.ExtendedUser;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
    Collection<GrantedAuthority> authorities = Collections.singletonList(
              new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
    return new ExtendedUser(
                    user.getEmail(), user.getPassword(), authorities, user.getId()
            );
    }

}