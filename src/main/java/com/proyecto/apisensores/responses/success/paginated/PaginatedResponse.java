package com.proyecto.apisensores.responses.success.paginated;

import com.proyecto.apisensores.responses.success.DataResponse;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class PaginatedResponse<T> extends DataResponse<List<T>> {

  private final MetaInformation meta;

  public PaginatedResponse(
    HttpStatus status,
    Long totalItems,
    PageRequest pageRequest,
    List<T> data
  ) {
    super(status, data);
    this.meta = new MetaInformation(pageRequest, totalItems, pageRequest.getPageSize(), data.size());
  }
}
