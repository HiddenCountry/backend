package com.example.hiddencountry.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "review_image")
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImage {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 500)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id", referencedColumnName = "id", nullable = false)
	@NotNull
	private Review review;

	public ReviewImage(String url) {
		this.url = url;
	}

}
