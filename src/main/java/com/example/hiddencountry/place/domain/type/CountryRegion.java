package com.example.hiddencountry.place.domain.type;

public enum CountryRegion {

	NORTH_AMERICA("북아메리카"),
	EUROPE("유럽"),
	ASIA("아시아"),
	AFRICA("아프리카"),
	SOUTH_AMERICA("남아메리카"),
	OCEANIA("오세아니아"),
	TURKEY("터키"),
	CHINA("중국"),
	MONGOLIA("몽골"),
	ARAB("아랍"),
	INDIA("인도"),
	SOUTHEAST_ASIA("동남아"),
	JAPAN("일본");

	private final String displayName;

	CountryRegion(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
