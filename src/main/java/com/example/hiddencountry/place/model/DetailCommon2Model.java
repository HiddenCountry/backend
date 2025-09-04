package com.example.hiddencountry.place.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailCommon2Model {
	private String contentid;
	private String contenttypeid;
	private String title;
	private String createdtime;
	private String modifiedtime;
	private String tel;
	private String telname;
	private String homepage;
	private String firstimage;
	private String firstimage2;
	private String cpyrhtDivCd;
	private String areacode;
	private String sigungucode;
	private String lDongRegnCd;
	private String lDongSignguCd;
	private String lclsSystm1;
	private String lclsSystm2;
	private String lclsSystm3;
	private String cat1;
	private String cat2;
	private String cat3;
	private String addr1;
	private String addr2;
	private String zipcode;
	private String mapx;
	private String mapy;
	private String mlevel;
	private String overview;
}

