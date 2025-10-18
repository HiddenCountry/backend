package com.example.hiddencountry.place.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.hiddencountry.global.base.BaseEntity;
import com.example.hiddencountry.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TravelCourse extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name; // 코스 이름

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	@NotNull
	private User user;

	@Column(name = "first_image", nullable = true, length = 500)
	private String firstImage;

	@Column(name = "view_count", nullable = false)
	private long viewCount = 0; // 기본값 0

	@OneToMany(mappedBy = "travelCourse", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("orderIndex ASC")
	@Builder.Default
	private List<TravelCoursePlace> travelCoursePlaces = new ArrayList<>();

	public void addPlace(Place place, int orderIndex) {
		TravelCoursePlace coursePlace = new TravelCoursePlace(this, place, orderIndex);
		travelCoursePlaces.add(coursePlace);
	}

	public void updateFirstImage(String newFirstImage) {
		if (newFirstImage == null || newFirstImage.isBlank()) {
			throw new IllegalArgumentException("첫 이미지 URL은 null이나 빈 문자열일 수 없습니다.");
		}
		this.firstImage = newFirstImage;
	}
}