package com.washinggod.remkey.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.washinggod.remkey.configuration.properties.JwtConfig;
import com.washinggod.remkey.dto.request.IntrospectRequest;
import com.washinggod.remkey.dto.response.IntrospectResponse;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IntrospectTokenService {

  JwtConfig jwtConfig;

  RedisTemplate<String, String> redisTemplate;

  public IntrospectResponse introspect(IntrospectRequest request) {

    boolean valid = true;

    try {
      SignedJWT signedJWT = this.verifyToken(request.getToken(), false);
    } catch (AppException | ParseException | JOSEException e) {
      valid = false;
    }

    return IntrospectResponse.builder().valid(valid).build();
  }

  public SignedJWT verifyToken(String token, boolean isRefresh)
      throws ParseException, JOSEException {

    if (Objects.isNull(token)) throw new AppException(ErrorCode.TOKEN_INVALID);

    SignedJWT signedJWT = SignedJWT.parse(token);

    JWSVerifier verifier = new MACVerifier(jwtConfig.getSecretKey().getBytes());

    boolean verified = signedJWT.verify(verifier);

    Date expiredTime =
        (isRefresh)
            ? new Date(
                signedJWT
                    .getJWTClaimsSet()
                    .getIssueTime()
                    .toInstant()
                    .plus(jwtConfig.getRefreshable(), ChronoUnit.DAYS)
                    .toEpochMilli())
            : signedJWT.getJWTClaimsSet().getExpirationTime();

    if (!verified || expiredTime.before(new Date()))
      throw new AppException(ErrorCode.TOKEN_INVALID);

    String invalidTokenKey = "blacklist:" + signedJWT.getJWTClaimsSet().getJWTID();
    if (redisTemplate.hasKey(invalidTokenKey)) throw new AppException(ErrorCode.TOKEN_INVALID);

    return signedJWT;
  }
}
