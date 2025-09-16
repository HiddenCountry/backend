package com.example.hiddencountry.place.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.hiddencountry.global.status.ErrorStatus;
import com.example.hiddencountry.place.model.DetailCommon2Model;
import com.example.hiddencountry.place.model.InfoItemModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class KorApiService {

	private final PlaceFieldMapper placeFieldMapper;

	@Value("${tour-api.secret-key}")
	private String serviceKey;

	private String appName = URLEncoder.encode("숨은나라찾기", StandardCharsets.UTF_8);


	public Mono<List<InfoItemModel>> getDetailIntro(Long contentId, Integer contentTypeId) {

		String url = "http://apis.data.go.kr/B551011/KorService2/detailIntro2"
			+ "?MobileOS=WEB"
			+ "&MobileApp=" + appName
			+ "&serviceKey=" + serviceKey
			+ "&_type=json"
			+ "&contentId=" + contentId
			+ "&contentTypeId=" + contentTypeId;

		return  WebClient
			.create(url).get()
			.uri(URI.create(url))
			.retrieve()
			.bodyToMono(JsonNode.class)
			.map(root -> {
				JsonNode itemNode = root.path("response")
					.path("body")
					.path("items")
					.path("item");
				// item이 배열이면 첫 번째 요소 가져오기
				if (itemNode.isArray() && !itemNode.isEmpty()) {
					itemNode = itemNode.get(0);
				}
				try {
					List<InfoItemModel> data = new ArrayList<>();

					for (String key : placeFieldMapper.getAllKeys()) {
						String value = itemNode.path(key).asText(null);
						if (value != null && !value.isBlank() && !value.equals("0")) {
							data.add(new InfoItemModel(placeFieldMapper.getTitle(key), value));
						}
					}

					return data;
				} catch (Exception e) {
					throw new RuntimeException("DTO 변환 실패", e);
				}
			});
	}
	public String getDetailOverview(Long contentId) {
		String url = "http://apis.data.go.kr/B551011/KorService2/detailCommon2"
			+ "?MobileOS=WEB"
			+ "&MobileApp=" + appName
			+ "&serviceKey=" + serviceKey
			+ "&_type=json"
			+ "&contentId=" + contentId;

		return WebClient.create(url).get()
			.uri(URI.create(url))
			.retrieve()
			.bodyToMono(JsonNode.class)
			.map(root -> {
				JsonNode itemNode = root.path("response")
					.path("body")
					.path("items")
					.path("item");

				// item이 배열이면 첫 번째 요소 가져오기
				if (itemNode.isArray() && itemNode.size() > 0) {
					itemNode = itemNode.get(0);
				}

				// overview 필드만 추출
				return itemNode.path("overview").asText("");
			}).block();
	}

	public DetailCommon2Model getDetailOverview2(Long contentId) {
		String url = "http://apis.data.go.kr/B551011/KorService2/detailCommon2"
			+ "?MobileOS=ETC"
			+ "&MobileApp=AppTest"
			+ "&serviceKey=" + serviceKey
			+ "&_type=json"
			+ "&contentId=" + contentId;

		return WebClient.create(url).get()
			.uri(URI.create(url))
			.retrieve()
			.bodyToMono(JsonNode.class)
			.map(root -> {
				JsonNode itemNode = root.path("response")
					.path("body")
					.path("items")
					.path("item");

				if (itemNode.isMissingNode() || itemNode.isNull() || (itemNode.isArray() && itemNode.isEmpty())) {
					throw ErrorStatus.PLACE_NOT_FOUND.serviceException();
				}

				// 배열이면 첫 번째 요소 사용
				if (itemNode.isArray()) {
					itemNode = itemNode.get(0);
				}

				// JsonNode -> DetailCommon2Model 매핑
				ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.treeToValue(itemNode, DetailCommon2Model.class);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("DetailCommon2Model 매핑 실패", e);
				}
			})
			.block();
	}
}
