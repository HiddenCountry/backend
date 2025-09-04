package com.example.hiddencountry.place.domain.type;

import lombok.Getter;

@Getter
public enum AreaCode {

	SEOUL("1", "서울"),
	INCHEON("2", "인천"),
	DAEJEON("3", "대전"),
	DAEGU("4", "대구"),
	GWANGJU("5", "광주"),
	BUSAN("6", "부산"),
	ULSAN("7", "울산"),
	SEJONG("8", "세종특별자치시"),
	GYEONGGI("31", "경기도"),
	GANGWON("32", "강원특별자치도"),
	CHUNGBUK("33", "충청북도"),
	CHUNGNAM("34", "충청남도"),
	GYEONGBUK("35", "경상북도"),
	GYEONGNAM("36", "경상남도"),
	JEONBUK("37", "전북특별자치도"),
	JEONNAM("38", "전라남도"),
	JEJU("39", "제주특별자치도");

	private final String code; // DB에 저장될 값
	private final String name; // 사용자에게 보여줄 이름

	AreaCode(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static AreaCode fromCode(String code) {
		for (AreaCode region : values()) {
			if (region.code.equals(code)) {
				return region;
			}
		}
		throw new IllegalArgumentException("Unknown code: " + code);
	}
}
