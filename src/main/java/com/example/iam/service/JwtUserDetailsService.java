package com.example.iam.service;

import com.example.iam.entity.UserEntity;
import com.example.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

@RequiredArgsConstructor

public class JwtUserDetailsService implements UserDetailsService {


    private final UserRepository repo;


    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = repo.findOneByUsername(username);


        if(user == null){

            throw new UsernameNotFoundException("User not found: " + username);

        }


        List<GrantedAuthority> roles = new ArrayList<>();

        user.getRoles().forEach(role -> roles.add(new SimpleGrantedAuthority(role.getName())));


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);

    }

}