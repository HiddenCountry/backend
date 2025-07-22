package com.example.hiddencountry.global.jwt;

import com.example.hiddencountry.global.status.ErrorStatus;
import com.example.hiddencountry.user.domain.User;
import com.example.hiddencountry.user.model.response.AuthorizationToken;
import com.example.hiddencountry.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    private JwtParser jwtParser;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        jwtParser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();
    }

    public AuthorizationToken createTokenInfo(User user) {
        String refreshToken = createRefreshToken(user);
        RefreshTokenHolder.setRefreshToken(refreshToken, user.getId());
        boolean isFirstLogin = (user.getNickname().equals("hiddencountry-new-kakao-user"));
        return new AuthorizationToken(createAccessToken(user), refreshToken, isFirstLogin);
    }

    public String createAccessToken(User user) {
        var claims = Jwts.claims()
                .add("id", user.getId())
                .build();

        return createToken(claims, accessTokenValidityInSeconds);
    }

    public String createRefreshToken(User user) {
        var claims = Jwts.claims()
                .add("id", user.getId())
                .build();

        return createToken(claims, refreshTokenValidityInSeconds);
    }

    private String createToken(Claims claims, long validityInSeconds) {
        var now = new Date();
        var validity = new Date(now.getTime() + validityInSeconds * 1000);
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        var claims = jwtParser.parseSignedClaims(token)
                .getPayload();
        var userId = claims.get("id", Long.class);

        var user = userRepository.findById(userId).orElseThrow(ErrorStatus.NOT_AUTHORIZED::serviceException);
        var principal = CustomUserDetails.from(user);

        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
        // return new UsernamePasswordAuthenticationToken(principal, token);
    }

    public Long parseAccessToken(String token) {
        var claims = jwtParser.parseSignedClaims(token)
                .getPayload();

        return claims.get("id", Long.class);
    }
}
