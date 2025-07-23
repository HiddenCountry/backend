package com.example.hiddencountry.entity.place.domain.type;

import lombok.Getter;

/**
 * 관광지, 문화시설 등 콘텐츠 유형을 나타내는 열거형입니다.
 * 각 타입은 고유한 코드와 이름을 가집니다.
 */
@Getter
public enum ContentType {

	/** 관광지 */
	_12(12, "관광지"),
	/** 문화시설 */
	_14(14, "문화시설"),
	/** 행사/공연/축제 */
	_15(15, "행사/공연/축제"),
	/** 여행코스 */
	_25(25, "여행코스"),
	/** 레포츠 */
	_28(28, "레포츠"),
	/** 숙박 */
	_32(32, "숙박"),
	/** 쇼핑 */
	_38(38, "쇼핑"),
	/** 음식점 */
	_39(39, "음식점");


	private final int code;


	private final String name;

	ContentType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * 코드 값을 받아 해당하는 ContentType을 반환합니다.
	 * 코드에 해당하는 ContentType이 없으면 예외를 던집니다.
	 * @param code 콘텐츠 유형 코드
	 * @return 대응하는 ContentType
	 * @throws IllegalArgumentException 알 수 없는 코드인 경우
	 */
	public static ContentType fromCode(int code) {
		for (ContentType type : values()) {
			if (type.code == code) return type;
		}
		throw new IllegalArgumentException("Unknown code: " + code);
	}
}