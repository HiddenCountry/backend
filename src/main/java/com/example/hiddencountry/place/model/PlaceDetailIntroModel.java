package com.example.hiddencountry.place.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PlaceDetailIntroModel {

	private String contentid;
	private String contenttypeid;

	// 숙박 관련
	private String roomcount;
	private String roomtype;
	private String checkintime;
	private String checkouttime;
	private String subfacility;
	private String refundregulation;
	private String infocenterlodging;
	private String parkinglodging;

	// 관광/체험 관련
	private String heritage1;
	private String heritage2;
	private String heritage3;
	private String opendate;
	private String restdate;
	private String expguide;
	private String expagerange;
	private String accomcount;
	private String parking;
	private String chkbabycarriage;
	private String chkpet;
	private String chkcreditcard;

	// 기타 공통
	private String beverage;
	private String sauna;
	private String sports;
	private String barbecue;

	// getters & setters
}
