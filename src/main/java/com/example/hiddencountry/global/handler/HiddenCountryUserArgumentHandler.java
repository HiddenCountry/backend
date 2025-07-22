package com.example.hiddencountry.global.handler;

import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.jwt.JwtTokenProvider;
import com.example.hiddencountry.global.status.ErrorStatus;
import com.example.hiddencountry.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class HiddenCountryUserArgumentHandler implements HandlerMethodArgumentResolver {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(HiddenCountryUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var authentication = getAuthentication();

        if (authentication == null) {
            return null;
        }

        var accessToken = authentication.getCredentials().toString();

        var userId = jwtTokenProvider.parseAccessToken(accessToken);

        return userRepository.findById(userId).orElseThrow(ErrorStatus.NO_PERMISSION::serviceException);
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal()
                .equals("anonymousUser")) {
            return null;
        }
        return authentication;
    }
}
