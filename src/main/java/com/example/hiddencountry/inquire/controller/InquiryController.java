package com.example.hiddencountry.inquire.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.inquire.model.InquiryRequest;
import com.example.hiddencountry.inquire.service.InquiryService;
import com.example.hiddencountry.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryController {

	private final InquiryService inquiryService;

	@Operation(
		summary = "장소 요청 API",
		description = "사용자가 제목과 내용을 입력하여 새로운 장소를 요청합니다."
	)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<Long> createInquiry(
		@Valid @RequestBody InquiryRequest request,
		@Parameter(hidden = true) @HiddenCountryUser User user
	) {
		return ApiResponse.onSuccess(
			SuccessStatus.PLACE_REQUEST_SUCCESS,
			inquiryService.saveInquiry(request, user).getId()
		);
	}

	// 스웨거 추가 설명 예시
	//	@io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
	// 		@io.swagger.v3.oas.annotations.responses.ApiResponse(
	// 			responseCode = "200",
	// 			description = "문의 생성 성공",
	// 			content = @io.swagger.v3.oas.annotations.media.Content(
	// 				schema = @io.swagger.v3.oas.annotations.media.Schema(
	// 					implementation = com.example.hiddencountry.global.model.ApiResponse.class
	// 				)
	// 			)
	// 		),
	// 		@io.swagger.v3.oas.annotations.responses.ApiResponse(
	// 			responseCode = "400",
	// 			description = "잘못된 요청 (제목/내용이 비어있거나 유효성 검증 실패)"
	// 		),
	// 		@io.swagger.v3.oas.annotations.responses.ApiResponse(
	// 			responseCode = "401",
	// 			description = "인증 실패 (로그인 필요)"
	// 		),
	// 		@io.swagger.v3.oas.annotations.responses.ApiResponse(
	// 			responseCode = "500",
	// 			description = "서버 오류"
	// 		)
	// 	})


}
