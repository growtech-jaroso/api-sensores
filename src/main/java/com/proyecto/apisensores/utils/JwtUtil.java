package com.proyecto.apisensores.utils;

import com.proyecto.apisensores.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class JwtUtil {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Value("${app.security.jwt.secret}")
  private String jwtSecret;

  @Value("${app.security.jwt.expiration}")
  private Long jwtDurationSeconds;

  /**
   * Generate a JWT token
   * @param user user to generate the token for
   * @return the generated token
   */
  public String generateToken(User user) {
    return Jwts.builder()
      .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), Jwts.SIG.HS256)
      .header()
      .add("typ", "JWT").and()
      .subject(user.getId())
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + (jwtDurationSeconds * 1000)))
      .claim("username", user.getUsername())
      .claim("email", user.getEmail())
      .compact();
  }

  /**
   * Check if the token is valid
   * @param token token to check
   * @return true if the token is valid, false otherwise
   */
  public boolean isValidToken(String token) {
    if (!StringUtils.hasLength(token)) return false; // There is no token

    try {
      JwtParser validator = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build();
      validator.parseSignedClaims(token);
      return true;
    } catch (SignatureException e) {
      log.warn("Error in token signature");
    } catch (MalformedJwtException | UnsupportedJwtException e) {
      log.warn("Incorrect token");
    } catch (ExpiredJwtException e) {
      log.warn("Expired token");
    } catch (Exception e) {
      log.error("Unexpected token error");
    }
    return false;

  }

  /**
   * Get the email from the token
   * @param token token to get the email from
   * @return email from the token
   */
  public String getEmailFromToken(String token) {
    JwtParser parser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build();
    Jws<Claims> claims = parser.parseSignedClaims(token);
    return claims.getPayload().get("email").toString();
  }

  /**
   * Get the username from the token
   * @param token token to get the username from
   * @return username from the token
   */
  public String getUsernameFromToken(String token) {
    JwtParser parser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build();
    Jws<Claims> claims = parser.parseSignedClaims(token);
    return claims.getPayload().get("username").toString();
  }
}
