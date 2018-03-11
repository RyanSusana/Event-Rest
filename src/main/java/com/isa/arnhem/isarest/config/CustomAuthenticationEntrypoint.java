package com.isa.arnhem.isarest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isa.arnhem.isarest.dto.Response;
import com.isa.arnhem.isarest.dto.ResponseMessage;
import com.isa.arnhem.isarest.dto.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntrypoint extends BasicAuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());

        PrintWriter writer = response.getWriter();

        Response responseMessage = ResponseMessage.builder().message("Invalid login credentials").property("exceptionName", e.getClass().getSimpleName()).property("exceptionMessage", e.getMessage()).type(ResponseType.UNAUTHORIZED).build();

        response.setStatus(responseMessage.getStatusCode());
        writer.println(objectMapper.writeValueAsString(responseMessage));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("ISA - API");
        super.afterPropertiesSet();
    }
}