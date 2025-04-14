package com.proyecto.apisensores.utils;

import com.proyecto.apisensores.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class ParamsUtil {
  public static Integer checkPage(String page) {
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

  public static Integer checkLimit(String limit) {
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
}
