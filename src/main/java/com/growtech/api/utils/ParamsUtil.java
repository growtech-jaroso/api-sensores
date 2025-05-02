package com.growtech.api.utils;

import com.growtech.api.exceptions.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;

public class ParamsUtil {
  private static Integer checkPage(String page) {
    // Check if the page is a number
    Integer pageNumber = null;

    try {
      pageNumber = Integer.parseInt(page);
    } catch (NumberFormatException e) {
      // If the page is not a number, return null
      throw new CustomException(HttpStatus.BAD_REQUEST, "The page must be a number");
    }

    // Check if the page is greater than 0
    if (pageNumber <= 0) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "The page must be greater than 0");
    }

    return pageNumber - 1;
  }

  private static Integer checkLimit(String limit) {
    // Check if the page is a number
    Integer limitNumber = null;

    try {
      limitNumber = Integer.parseInt(limit);
    } catch (NumberFormatException e) {
      // If the page is not a number, return null
      throw new CustomException(HttpStatus.BAD_REQUEST, "The limit must be a number");
    }

    // Check if the page is greater than 0
    if (limitNumber < 0) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "The limit must be greater than 0");
    }

    return limitNumber;
  }

  /**
   * Get the page request from the request
   * @param request actual request
   * @return PageRequest
   */
  public static PageRequest getPageRequest(ServerRequest request) {
    // Retrieve the page and limit parameters from the request
    String page = request.queryParam("page").orElse("1");
    String limit = request.queryParam("limit").orElse("10");

    // Convert the page and limit to integers
    Integer pageNumber = ParamsUtil.checkPage(page);
    Integer limitNumber = ParamsUtil.checkLimit(limit);

    // Create a PageRequest object with the page and limit and return
    return PageRequest.of(pageNumber, limitNumber);
  }
}
