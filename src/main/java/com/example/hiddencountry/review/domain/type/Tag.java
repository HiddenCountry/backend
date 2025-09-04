package com.example.hiddencountry.review.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 리뷰에 사용되는 태그를 나타내는 열거형입니다.
 * 각 태그는 설명(description)과 태그 종류(TagType)를 포함합니다.
 */
@Getter
@RequiredArgsConstructor
public enum Tag {

	// 음식 관련 태그
	/** 음식이 맛있어요 */
	TASTE_GOOD("음식이 맛있어요", TagType.FOOD),
	/** 양이 많아요 */
	LARGE_PORTION("양이 많아요", TagType.FOOD),
	/** 현지 맛에 가까워요 */
	LOCAL_STYLE("현지 맛에 가까워요", TagType.FOOD),
	/** 특별한 메뉴가 많아요 */
	UNIQUE_MENU("특별한 메뉴가 많아요", TagType.FOOD),
	/** 비싼 만큼 가치 있어요 */
	WORTH_PRICE("비싼 만큼 가치 있어요", TagType.FOOD),

	// 분위기 관련 태그
	/** 시설이 깔끔해요 */
	CLEAN_FACILITY("시설이 깔끔해요", TagType.MOOD),
	/** 아늑해요 */
	COZY("아늑해요", TagType.MOOD),
	/** 컨셉이 독특해요 */
	UNIQUE_CONCEPT("컨셉이 독특해요", TagType.MOOD),
	/** 사진이 잘 나와요 */
	GOOD_PHOTO("사진이 잘 나와요", TagType.MOOD),
	/** 외국 느낌 낭낭 */
	FOREIGN_VIBE("외국 느낌 낭낭", TagType.MOOD),
	/** 뷰가 좋아요 */
	NICE_VIEW("뷰가 좋아요", TagType.MOOD),
	/** 차분한 분위기에요 */
	CALM("차분한 분위기에요", TagType.MOOD),

	// 기타 태그
	/** 주차하기 편해요 */
	EASY_PARKING("주차하기 편해요", TagType.ETC),
	/** 친절해요 */
	KIND("친절해요", TagType.ETC),
	/** 청결해요 */
	CLEAN("청결해요", TagType.ETC),
	/** 사람이 많아요 */
	CROWDED("사람이 많아요", TagType.ETC),
	/** 오래 머무르기 좋아요 */
	GOOD_TO_STAY("오래 머무르기 좋아요", TagType.ETC),
	/** 데이트하기 좋아요 */
	GOOD_FOR_DATE("데이트하기 좋아요", TagType.ETC),
	/** 아이와 가기 좋아요 */
	GOOD_FOR_KIDS("아이와 가기 좋아요", TagType.ETC),
	/** 화장실이 깨끗해요 */
	CLEAN_TOILET("화장실이 깨끗해요", TagType.ETC);

	/**
	 * 태그의 설명입니다.
	 */
	private final String description;

	/**
	 * 태그의 분류 타입입니다.
	 */
	private final TagType type;
}

