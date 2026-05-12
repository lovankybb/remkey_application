package com.washinggod.remkey.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.washinggod.remkey.configuration.properties.JwtConfig;
import com.washinggod.remkey.dto.request.AuthenticationRequest;
import com.washinggod.remkey.dto.request.InvalidateTokenRequest;
import com.washinggod.remkey.dto.request.RefreshTokenRequest;
import com.washinggod.remkey.dto.response.AuthenticationResponse;
import com.washinggod.remkey.entity.User;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.repository.UserRepository;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

  PasswordEncoder passwordEncoder;

  UserRepository userRepository;

  JwtConfig jwtConfig;

  IntrospectTokenService introspectTokenService;

  RedisTemplate<String, String> redisTemplate;

  public AuthenticationResponse authenticate(AuthenticationRequest request) {

    User user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(
                () -> {
                  return new AppException(ErrorCode.USER_NOT_EXIST);
                });

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
      throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

    return AuthenticationResponse.builder().token(this.generateToken(user)).build();
  }

  public void invalidateToken(InvalidateTokenRequest request) {
    try {
      SignedJWT signedJWT = introspectTokenService.verifyToken(request.getToken(), false);

      String invalidTokenKey = "blacklist:" + signedJWT.getJWTClaimsSet().getJWTID();
      long milliDuration =
          signedJWT.getJWTClaimsSet().getExpirationTime().getTime() - new Date().getTime();

      redisTemplate.opsForValue().set(invalidTokenKey, "1", Duration.ofMillis(milliDuration));

    } catch (ParseException | JOSEException e) {
      throw new AppException(ErrorCode.TOKEN_INVALID);
    }
  }

  public AuthenticationResponse refreshToken(RefreshTokenRequest request) {

    try {

      SignedJWT signedJWT = introspectTokenService.verifyToken(request.getToken(), true);
      String userId = signedJWT.getJWTClaimsSet().getSubject();

      User user =
          userRepository
              .findById(userId)
              .orElseThrow(
                  () -> {
                    return new AppException(ErrorCode.USER_NOT_EXIST);
                  });

      //            invalidate previous token
      String invalidTokenKey = "blacklist:" + signedJWT.getJWTClaimsSet().getJWTID();
      long milliDuration =
          signedJWT.getJWTClaimsSet().getExpirationTime().getTime() - new Date().getTime();

      redisTemplate.opsForValue().set(invalidTokenKey, "1", Duration.ofMillis(milliDuration));

      return AuthenticationResponse.builder().token(generateToken(user)).build();
    } catch (AppException | ParseException | JOSEException e) {
      throw new AppException(ErrorCode.TOKEN_INVALID);
    }
  }

  private String generateToken(User user) {

    JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

    JWTClaimsSet jwtClaimsSet =
        new JWTClaimsSet.Builder()
            .issuer("com.lovanky.remkey")
            .issueTime(new Date())
            .jwtID(UUID.randomUUID().toString())
            .subject(user.getId())
            .expirationTime(
                new Date(
                    Instant.now()
                        .plus(jwtConfig.getValidDuration(), ChronoUnit.DAYS)
                        .toEpochMilli()))
            .claim("scope", buildRole(user))
            .claim("username", user.getUsername())
            .build();

    JWSObject jwsObject = new JWSObject(jwsHeader, new Payload(jwtClaimsSet.toJSONObject()));

    try {
      jwsObject.sign(new MACSigner(jwtConfig.getSecretKey().getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      throw new AppException(ErrorCode.GENERATE_TOKEN_FAILED);
    }
  }

  private String buildRole(User user) {

    StringJoiner stringJoiner = new StringJoiner(" ");

    user.getRoles()
        .forEach(
            role -> {
              stringJoiner.add("ROLE_" + role.getName());
              role.getPermissions()
                  .forEach(
                      permission -> {
                        stringJoiner.add(permission.getName());
                      });
            });

    return stringJoiner.toString();
  }
}
