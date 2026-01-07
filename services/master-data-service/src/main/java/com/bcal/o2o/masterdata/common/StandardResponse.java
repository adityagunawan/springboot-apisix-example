package com.bcal.o2o.masterdata.common;

public class StandardResponse<T> {

  private boolean success;
  private String message;
  private T data;

  public StandardResponse() {
  }

  public StandardResponse(boolean success, String message, T data) {
    this.success = success;
    this.message = message;
    this.data = data;
  }

  public static <T> StandardResponse<T> success(T data) {
    return new StandardResponse<>(true, "Success", data);
  }

  public static <T> StandardResponse<T> error(String message) {
    return new StandardResponse<>(false, message, null);
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
