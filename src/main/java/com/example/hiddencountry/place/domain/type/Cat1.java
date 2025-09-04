package com.example.hiddencountry.place.domain.type;

/**
 * 주요 카테고리를 나타내는 열거형입니다.
 * 각 카테고리는 코드(code), 이름(name), 정렬순서(rnum)와 매핑됩니다.
 *
 * <p>예시:
 * <ul>
 *     <li>A01 - 자연</li>
 *     <li>A02 - 인문(문화/예술/역사)</li>
 *     <li>A03 - 레포츠</li>
 *     <li>A04 - 쇼핑</li>
 *     <li>A05 - 음식</li>
 *     <li>B02 - 숙박</li>
 * </ul>
 *
 * <p>※ 참고: C01(추천코스)는 현재 enum에 포함되지 않음
 */
public enum Cat1 {

	/** A01 - 자연  */
	A01,

	/** A02 - 인문(문화/예술/역사) */
	A02,

	/** A03 - 레포츠 */
	A03,

	/** A04 - 쇼핑 */
	A04,

	/** A05 - 음식 */
	A05,

	/** B02 - 숙박 */
	B02,

}
