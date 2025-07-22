package com.example.hiddencountry.global.filter;

import com.example.hiddencountry.global.jwt.JwtTokenProvider;
import com.example.hiddencountry.global.status.ErrorStatus;
import com.example.hiddencountry.global.util.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private final JwtTokenProvider jwtUtil;
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Override
	protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
		var path = req.getRequestURI();
		var shouldSkip = SecurityConstants.ALLOW_URLS.stream()
			.anyMatch(pattern -> antPathMatcher.match(pattern, path));

		log.debug("Should skip JwtFilter for path {}: {}", path, shouldSkip);
		return shouldSkip;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		if (shouldNotFilter(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = resolveAccessToken(request);

		if (accessToken == null) {
			throw ErrorStatus.NOT_EXIST_ACCESSTOKEN.serviceException();
		}

		if (!jwtUtil.validateToken(accessToken)) {
			log.debug("invalid accessToken: {}", accessToken);
			throw ErrorStatus.INVALID_ACCESSTOKEN.serviceException();
		}

		SecurityContextHolder.getContext()
				.setAuthentication(jwtUtil.getAuthentication(accessToken));

		filterChain.doFilter(request, response);
	}

	private String resolveAccessToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");

		if (authorization != null && authorization.startsWith("Bearer ")) {
			return authorization.split(" ")[1];
		}

		return null;
	}

}

