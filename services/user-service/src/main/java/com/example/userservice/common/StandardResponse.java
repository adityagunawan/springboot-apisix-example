package com.example.userservice.common;

public class StandardResponse<T> {

  private int statusCode;
  private String message;
  private Object error;
  private T value;

  public StandardResponse() {
  }

  public StandardResponse(int statusCode, String message, Object error, T value) {
    this.statusCode = statusCode;
    this.message = message;
    this.error = error;
    this.value = value;
  }

  public static <T> StandardResponse<T> success(int statusCode, String message, T value) {
    return new StandardResponse<>(statusCode, message, null, value);
  }

  public static <T> StandardResponse<T> error(int statusCode, String message, Object error) {
    return new StandardResponse<>(statusCode, message, error, null);
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getError() {
    return error;
  }

  public void setError(Object error) {
    this.error = error;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }
}
