package com.growtech.api.utils;

import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
   * Check if the date is a valid date
   * @param date
   * @param message
   */
  private static LocalDateTime checkDate(String date, String message){

    try {
      Instant instant = Instant.parse(date);
      return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    } catch (Exception e) {
      // If the date is not a valid date, return null
      throw new CustomException(HttpStatus.BAD_REQUEST, message);
    }
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

  /**
   * Get the page request from the request
   * @param request actual request
   * @return PageRequest
   */
  public static PageRequest getPageRequest(ServerRequest request, Integer defaultLimit) {
    // Retrieve the page and limit parameters from the request
    String page = request.queryParam("page").orElse("1");
    String limit = request.queryParam("limit").orElse(defaultLimit.toString());

    // Convert the page and limit to integers
    Integer pageNumber = ParamsUtil.checkPage(page);
    Integer limitNumber = ParamsUtil.checkLimit(limit);

    // Create a PageRequest object with the page and limit and return
    return PageRequest.of(pageNumber, limitNumber);
  }

  /**
   * Get the date filter from the request
   * @param request
   * @return Pair<LocalDateTime, LocalDateTime>
   */
  public static Pair<LocalDateTime, LocalDateTime> getDateFilter(ServerRequest request) {
    // Retrieve the date parameters from the request
    String dateAfter = request.queryParam("after").orElse(LocalDateTime.now().toInstant(ZoneOffset.UTC).toString());
    String dateBefore = request.queryParam("before").orElse(LocalDateTime.of(1970, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toString());

    // Check if the date parameters are not null
    LocalDateTime afterChecked = checkDate(dateAfter, "The after date is not a valid date");
    LocalDateTime beforeChecked = checkDate(dateBefore, "The before date is not a valid date");
    // Check if the after date is before the before date
    if (afterChecked.isBefore(beforeChecked)) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "The after date must be before the before date");
    }

    return Pair.of(beforeChecked, afterChecked);
  }
}
