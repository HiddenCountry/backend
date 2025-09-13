package com.example.hiddencountry.inquire.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor       // 기본 생성자 자동 생성
@AllArgsConstructor      // 모든 필드 포함 생성자 자동 생성
public class InquiryRequest {

	@NotBlank(message = "제목은 비워둘 수 없습니다.")
	@Size(max = 20, message = "제목은 최대 20자 이내여야 합니다.")
	private String title;

	@NotBlank(message = "내용은 비워둘 수 없습니다.")
	@Size(max = 1000, message = "내용은 최대 1000자 이내여야 합니다.")
	private String content;

}
