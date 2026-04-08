package com.example.iam.util;


import com.example.exception.ErrorResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.stereotype.Component;


import java.io.IOException;

import java.time.LocalDateTime;


//Clase S6

@Component

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String exceptionMsg = (String) request.getAttribute("msg");


        if(exceptionMsg == null){

            exceptionMsg = "Token not found or invalid";

        }


        ErrorResponse errorTemplate = new ErrorResponse(exceptionMsg, HttpStatus.UNAUTHORIZED.value(),  request.getRequestURI(), LocalDateTime.now());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setContentType("application/json");

        response.getWriter().write(convertObjectToJson(errorTemplate));

    }


    private String convertObjectToJson(Object object) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        mapper.findAndRegisterModules();

        return mapper.writeValueAsString(object);

    }

}
