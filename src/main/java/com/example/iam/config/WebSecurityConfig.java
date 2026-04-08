package com.example.iam.config;


import com.example.iam.util.JwtAuthenticationEntryPoint;

import com.example.iam.filter.JwtRequestFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//Clase S7

@Configuration

@EnableWebSecurity

@EnableMethodSecurity //Para usar @PreAuthorize en los controladores

@RequiredArgsConstructor

public class WebSecurityConfig {


    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final UserDetailsService jwtUserDetailsService;

    private final JwtRequestFilter jwtRequestFilter;


    @Bean

    public AuthenticationManager authenticationManager(

            AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();

    }


    @Bean

    public static PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();

    }


    @Autowired

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());

    }


    @Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //Desde Spring Boot 3.1+


        return http

                .csrf(AbstractHttpConfigurer::disable)

                .formLogin(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(req -> req

                        .requestMatchers("/login").permitAll()

                        .requestMatchers("/mail/**").permitAll()

                        .anyRequest().authenticated()

                )

                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                .build();

    }


}

