package com.example.common.web;

import com.example.common.response.StandardResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType, Class converterType) {
    // Apply only to app packages to avoid touching actuator, etc.
    return returnType.getContainingClass().getPackageName().startsWith("com.example");
  }

  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {

    if (body instanceof StandardResponse<?> alreadyWrapped) {
      return alreadyWrapped;
    }

    int status = 200;
    if (response instanceof ServletServerHttpResponse servletResponse) {
      status = servletResponse.getServletResponse().getStatus();
    }

    String message = status >= 400 ? "error" : "success";
    return StandardResponse.success(status, message, body);
  }
}
