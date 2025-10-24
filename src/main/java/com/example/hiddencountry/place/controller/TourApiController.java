package com.example.hiddencountry.place.controller;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/tour")
public class TourApiController {

	private final WebClient webClient = WebClient.create();

	@Value("${tour-api.secret-key}")
	private String serviceKey;

	private String appName = URLEncoder.encode("숨은나라찾기", StandardCharsets.UTF_8);

	/**
	 * 1. 위치 기반 관광정보
	 */
	@GetMapping("/location")
	public ResponseEntity<JsonNode> getLocation(@RequestParam String mapX,
		@RequestParam String mapY,
		@RequestParam String radius,
		@RequestParam String arrange,
		@RequestParam String numOfRows,
		@RequestParam String pageNo) {

		String url = "https://apis.data.go.kr/B551011/KorService2/locationBasedList2"
			+ "?MobileOS=WEB"
			+ "&MobileApp=" + appName
			+ "&serviceKey=" + serviceKey
			+ "&_type=json"
			+ "&mapX=" + mapX
			+ "&mapY=" + mapY
			+ "&radius=" + radius
			+ "&arrange=" + arrange
			+ "&numOfRows=" + numOfRows
			+ "&pageNo=" + pageNo;

		JsonNode result = webClient.get()
			.uri(URI.create(url))
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();

		return ResponseEntity.ok(result); // JSON 그대로 반환
	}

	@GetMapping("/detailImage")
	public ResponseEntity<JsonNode> getDetailImage(
		@RequestParam String contentId,
		@RequestParam(required = false, defaultValue = "Y") String imageYN,
		@RequestParam(required = false, defaultValue = "1") String pageNo,
		@RequestParam(required = false, defaultValue = "30") String numOfRows) {

		String url = "https://apis.data.go.kr/B551011/KorService2/detailImage2"
			+ "?serviceKey=" + serviceKey
			+ "&MobileApp=" + appName
			+ "&MobileOS=ETC"
			+ "&_type=json"
			+ "&contentId=" + contentId
			+ "&imageYN=" + imageYN
			+ "&pageNo=" + pageNo
			+ "&numOfRows=" + numOfRows;

		JsonNode result = WebClient.create()
			.get()
			.uri(URI.create(url))
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();

		return ResponseEntity.ok(result);
	}
}
