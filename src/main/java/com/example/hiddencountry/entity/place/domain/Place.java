package com.example.hiddencountry.entity.place.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.hiddencountry.entity.place.domain.type.Cat1;
import com.example.hiddencountry.entity.place.domain.type.ContentType;
import com.example.hiddencountry.entity.place.domain.type.CountryRegion;
import com.example.hiddencountry.entity.place.domain.type.Season;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Place {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "addr1", nullable = false)
	private String addr1;

	@Column(name = "addr2", nullable = true)
	private String addr2;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "cat1", nullable = false, length = 10)
	private Cat1 cat1;

	@NotNull
	@Column(name = "content_id", nullable = false)
	private Long contentId;

	@Convert(converter = ContentTypeConverter.class)
	private ContentType contentType;

	@Column(name = "first_image", nullable = false,length = 500)
	private String firstImage;

	@Column(name = "first_image2", nullable = false,length = 500)
	private String firstImage2;

	private String cpyrhtDivCd;

	@Embedded
	private Location location;

	@NotNull
	@Column(name = "title", nullable = false, length = 50)
	private String title;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "r_season", nullable = false, length = 10)
	private Season rSeason; // 추천계절

	@OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PlaceCountry> placeCountries = new ArrayList<>();

	public void addPlaceCountries(CountryRegion... regions) {
		if (this.placeCountries == null) {
			this.placeCountries = new ArrayList<>();
		}

		for (CountryRegion region : regions) {
			this.placeCountries.add(new PlaceCountry(null, this, region));
		}
	}

}


/** place 초반 저장시, 하단 코드 실행시 하나 저장됨 - 리뷰 부터 구현 하게되면, 다음 코드를 한번만 실해해서 데이터를 넣어주세요!  */
// 		Place place = Place.builder()
// 			.addr1("주소1")
// 			.addr2("주소2")
// 			.cat1(Cat1.A02)
// 			.contentId(100L)
// 			.contentType(ContentType._12)
// 			.firstImage("image1")
// 			.firstImage2("image2")
// 			.cpyrhtDivCd("123")
// 			.location(Location.builder()
// 				.mapx(100.123131231313131)
// 				.mapy(100.1231231313232)
// 				.build())
// 			.title("t1")
// 			.rSeason(Season.AUTUMN)
// 			.build();
//
// 		place.addPlaceCountries(
// 			CountryRegion.ASIA, CountryRegion.EUROPE, CountryRegion.JAPAN
// 		);
//
// 		Place saved = placeRepository.save(place);