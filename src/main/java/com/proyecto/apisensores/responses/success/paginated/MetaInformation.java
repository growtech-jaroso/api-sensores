package com.proyecto.apisensores.responses.success.paginated;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;

@Getter
public class MetaInformation {
  @JsonProperty("total_items")
  private final Long totalItems;

  @JsonProperty("item_count")
  private final Integer itemCount;

  @JsonProperty("items_per_page")
  private final Integer itemsPerPage;

  @JsonProperty("total_pages")
  private final Long totalPages;

  @JsonProperty("current_page")
  private final Integer currentPage;

  public MetaInformation(PageRequest pageRequest, Long totalItems, Integer limit, Integer pageSize) {
    this.itemsPerPage = limit;
    // Calculate the total pages
    this.totalPages = Math.ceilDiv(totalItems, pageRequest.getPageSize());
    this.totalItems = totalItems;
    this.itemCount = pageSize;
    // Calculate the current page adding 1 because pagination is zero-based
    this.currentPage = pageRequest.getPageNumber() + 1;
  }
}
