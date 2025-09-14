package com.example.hiddencountry.place.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.hiddencountry.place.domain.type.AreaCode;
import com.example.hiddencountry.place.domain.type.Cat1;
import com.example.hiddencountry.place.domain.type.ContentType;
import com.example.hiddencountry.place.domain.type.CountryRegion;
import com.example.hiddencountry.place.domain.type.Season;
import com.example.hiddencountry.review.domain.type.Tag;

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

	@Convert(converter = AreaCodeConverter.class)
	private AreaCode areaCode;

	@Column(name = "first_image", nullable = false,length = 500)
	private String firstImage;

	@Column(name = "first_image2", nullable = false,length = 500)
	private String firstImage2;

	private String cpyrhtDivCd;

	@Embedded
	private Location location;

	@NotNull
	@Column(name = "title", nullable = false, length = 150)
	private String title;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "r_season", nullable = false, length = 10)
	private Season rSeason; // 추천계절

	@OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PlaceCountry> placeCountries;

	@NotNull
	@Column(name = "review_count", nullable = false)
	private long reviewCount;

	@NotNull
	@Column(name = "review_score_average", nullable = false)
	private float reviewScoreAverage;

	@NotNull
	@Column(name = "view_count", nullable = false)
	private long viewCount;

	@Column(name = "top_hashtag1", length = 50)
	@Enumerated(EnumType.STRING)
	private Tag topHashtag1;

	@Column(name = "top_hashtag2", length = 50)
	@Enumerated(EnumType.STRING)
	private Tag topHashtag2;

	public void changeSeason(Season newSeason) {
		if (newSeason == null) {
			throw new IllegalArgumentException("추천 계절은 null일 수 없습니다.");
		}
		this.rSeason = newSeason;
	}

	public void changeTopHashtags(Tag top1, Tag top2) {
		this.topHashtag1 = top1;
		this.topHashtag2 = top2;
	}

	public void changeReviewStats(long count, float average) {
		this.reviewCount = count;
		this.reviewScoreAverage = average;
	}

	public void addPlaceCountries(CountryRegion... regions) {
		if (this.placeCountries == null) {
			this.placeCountries = new ArrayList<>();
		}

		for (CountryRegion region : regions) {
			PlaceCountryId id = new PlaceCountryId(this.id, region);
			PlaceCountry placeCountry = new PlaceCountry(id, this);
			this.placeCountries.add(placeCountry);
		}
	}

}