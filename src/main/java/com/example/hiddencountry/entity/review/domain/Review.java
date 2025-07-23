package com.example.hiddencountry.entity.review.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.hiddencountry.entity.user.User;
import com.example.hiddencountry.entity.place.domain.Place;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "review")
@AllArgsConstructor
@NoArgsConstructor
public class Review {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 1000)
	private String content;

	@Max(5)
	@Min(0)
	private Integer score;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id", referencedColumnName = "id", nullable = false)
	@NotNull
	private Place place;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	@NotNull
	private User user;

	@Builder.Default
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ReviewImage> images = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ReviewTag> tags = new ArrayList<>();


}
