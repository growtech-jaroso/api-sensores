package com.growtech.api.responses.success;

import com.growtech.api.responses.Response;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataResponse<T> extends Response {
  private final T data;

  public DataResponse(HttpStatus status, T data) {
    super(status);
    this.data = data;
  }
}
