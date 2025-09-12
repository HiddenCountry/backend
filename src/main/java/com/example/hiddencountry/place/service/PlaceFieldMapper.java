package com.example.hiddencountry.place.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;


@Component
public class PlaceFieldMapper {

	private final Map<String, String> fieldMap;

	public PlaceFieldMapper() {
		fieldMap = new HashMap<>();
		fieldMap.put("accomcount", "수용인원");
		fieldMap.put("chkbabycarriage", "유모차대여정보");
		fieldMap.put("chkcreditcard", "신용카드가능정보");
		fieldMap.put("chkpet", "애완동물동반가능정보");
		fieldMap.put("expagerange", "체험가능연령");
		fieldMap.put("expguide", "체험안내");
		fieldMap.put("heritage1", "세계문화유산유무");
		fieldMap.put("heritage2", "세계자연유산유무");
		fieldMap.put("heritage3", "세계기록유산유무");
		fieldMap.put("infocenter", "문의및안내");
		fieldMap.put("opendate", "개장일");
		fieldMap.put("parking", "주차시설");
		fieldMap.put("restdate", "쉬는날");
		fieldMap.put("useseason", "이용시기");
		fieldMap.put("usetime", "이용시간");

		// culture
		fieldMap.put("accomcountculture", "수용인원");
		fieldMap.put("chkbabycarriageculture", "유모차대여정보");
		fieldMap.put("chkcreditcardculture", "신용카드가능정보");
		fieldMap.put("chkpetculture", "애완동물동반가능정보");
		fieldMap.put("discountinfo", "할인정보");
		fieldMap.put("infocenterculture", "문의및안내");
		fieldMap.put("parkingculture", "주차시설");
		fieldMap.put("parkingfee", "주차요금");
		fieldMap.put("restdateculture", "쉬는날");
		fieldMap.put("usefee", "이용요금");
		fieldMap.put("usetimeculture", "이용시간");
		fieldMap.put("scale", "규모");
		fieldMap.put("spendtime", "관람소요시간");

		// festival
		fieldMap.put("agelimit", "관람가능연령");
		fieldMap.put("bookingplace", "예매처");
		fieldMap.put("discountinfofestival", "할인정보");
		fieldMap.put("eventenddate", "행사종료일");
		fieldMap.put("eventhomepage", "행사홈페이지");
		fieldMap.put("eventplace", "행사장소");
		fieldMap.put("eventstartdate", "행사시작일");
		fieldMap.put("festivalgrade", "축제등급");
		fieldMap.put("placeinfo", "행사장위치안내");
		fieldMap.put("playtime", "공연시간");
		fieldMap.put("program", "행사프로그램");
		fieldMap.put("spendtimefestival", "관람소요시간");
		fieldMap.put("sponsor1", "주최자정보");
		fieldMap.put("sponsor1tel", "주최자연락처");
		fieldMap.put("sponsor2", "주관사정보");
		fieldMap.put("sponsor2tel", "주관사연락처");
		fieldMap.put("subevent", "부대행사");
		fieldMap.put("usetimefestival", "이용요금");

		// tour course
		fieldMap.put("distance", "코스총거리");
		fieldMap.put("infocentertourcourse", "문의및안내");
		fieldMap.put("schedule", "코스일정");
		fieldMap.put("taketime", "코스총소요시간");
		fieldMap.put("theme", "코스테마");

		// leports
		fieldMap.put("accomcountleports", "수용인원");
		fieldMap.put("chkbabycarriageleports", "유모차대여정보");
		fieldMap.put("chkcreditcardleports", "신용카드가능정보");
		fieldMap.put("chkpetleports", "애완동물동반가능정보");
		fieldMap.put("expagerangeleports", "체험가능연령");
		fieldMap.put("infocenterleports", "문의및안내");
		fieldMap.put("openperiod", "개장기간");
		fieldMap.put("parkingfeeleports", "주차요금");
		fieldMap.put("parkingleports", "주차시설");
		fieldMap.put("reservation", "예약안내");
		fieldMap.put("restdateleports", "쉬는날");
		fieldMap.put("scaleleports", "규모");
		fieldMap.put("usefeeleports", "입장료");
		fieldMap.put("usetimeleports", "이용시간");

		// lodging
		fieldMap.put("accomcountlodging", "수용가능인원");
		fieldMap.put("checkintime", "입실시간");
		fieldMap.put("checkouttime", "퇴실시간");
		fieldMap.put("chkcooking", "객실내취사여부");
		fieldMap.put("foodplace", "식음료장");
		fieldMap.put("infocenterlodging", "문의및안내");
		fieldMap.put("parkinglodging", "주차시설");
		fieldMap.put("pickup", "픽업서비스");
		fieldMap.put("roomcount", "객실수");
		fieldMap.put("reservationlodging", "예약안내");
		fieldMap.put("reservationurl", "예약안내홈페이지");
		fieldMap.put("roomtype", "객실유형");
		fieldMap.put("scalelodging", "규모");
		fieldMap.put("subfacility", "부대시설 (기타)");
		fieldMap.put("barbecue", "바비큐장여부");
		fieldMap.put("beauty", "뷰티시설정보");
		fieldMap.put("beverage", "식음료장여부");
		fieldMap.put("bicycle", "자전거대여여부");
		fieldMap.put("campfire", "캠프파이어여부");
		fieldMap.put("fitness", "휘트니스센터여부");
		fieldMap.put("karaoke", "노래방여부");
		fieldMap.put("publicbath", "공용샤워실여부");
		fieldMap.put("publicpc", "공용 PC실여부");
		fieldMap.put("sauna", "사우나실여부");
		fieldMap.put("seminar", "세미나실여부");
		fieldMap.put("sports", "스포츠시설여부");
		fieldMap.put("refundregulation", "환불규정");

		// shopping
		fieldMap.put("chkbabycarriageshopping", "유모차대여정보");
		fieldMap.put("chkcreditcard", "신용카드가능정보");
		fieldMap.put("shopping", "쇼핑정보");
		fieldMap.put("chkpetshopping", "애완동물동반가능정보");
		fieldMap.put("culturecenter", "문화센터바로가기");
		fieldMap.put("fairday", "장서는날");
		fieldMap.put("infocentershopping", "문의및안내");
		fieldMap.put("opendateshopping", "개장일");
		fieldMap.put("opentime", "영업시간");
		fieldMap.put("parkingshopping", "주차시설");
		fieldMap.put("restdateshopping", "쉬는날");
		fieldMap.put("restroom", "화장실설명");
		fieldMap.put("saleitem", "판매품목");
		fieldMap.put("saleitemcost", "판매품목별가격");
		fieldMap.put("scaleshopping", "규모");
		fieldMap.put("shopguide", "매장안내");

		// food
		fieldMap.put("chkcreditcardfood", "신용카드가능정보");
		fieldMap.put("discountinfofood", "할인정보");
		fieldMap.put("firstmenu", "대표메뉴");
		fieldMap.put("infocenterfood", "문의및안내");
		fieldMap.put("kidsfacility", "어린이놀이방여부");
		fieldMap.put("opendatefood", "개업일");
		fieldMap.put("opentimefood", "영업시간");
		fieldMap.put("packing", "포장가능");
		fieldMap.put("parkingfood", "주차시설");
		fieldMap.put("reservationfood", "예약안내");
		fieldMap.put("restdatefood", "쉬는날");
		fieldMap.put("scalefood", "규모");
		fieldMap.put("seat", "좌석수");
		fieldMap.put("smoking", "금연/흡연여부");
		fieldMap.put("treatmenu", "취급메뉴");
		fieldMap.put("lcnsno", "인허가번호");
		// ... 나머지 100개
	}



		public String getTitle(String key) {
			return fieldMap.getOrDefault(key, key);
		}

		public boolean hasKey(String key) {
			return fieldMap.containsKey(key);
		}

		public Set<String> getAllKeys() {
			return fieldMap.keySet();
		}
}