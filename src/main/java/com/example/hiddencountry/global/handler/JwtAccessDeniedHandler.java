package com.example.hiddencountry.global.handler;

import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAccessDeniedHandler implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException)
		throws IOException
	{
		res.setContentType("application/json; charset=UTF-8");
		res.setStatus(403);

		// JSON 직렬화
		ObjectMapper objectMapper = new ObjectMapper();

		Exception exception = (Exception) req.getAttribute("exception");

		if (exception != null) {
			res.getWriter().write(objectMapper.writeValueAsString(
					ApiResponse.onFailure(ErrorStatus.NOT_AUTHORIZED, exception.getMessage())
			));
		} else {
			res.getWriter().write(objectMapper.writeValueAsString(
					ApiResponse.onFailure(ErrorStatus.NOT_AUTHORIZED)
			));
		}
	}
}
