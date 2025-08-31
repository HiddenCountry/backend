package com.example.hiddencountry.place.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.pagination.PaginationModel;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.place.model.PlaceThumbnailModel;
import com.example.hiddencountry.place.service.UserPlaceService;
import com.example.hiddencountry.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class UserPlaceController {

	private final UserPlaceService userPlaceService;

	@Operation(
		summary = "북마크",
		description = ""
	)
	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/place")
	public ApiResponse saveBookmarkPlace(
		@Parameter(hidden = true) @HiddenCountryUser User user,
		@RequestParam Long id
	) {
		userPlaceService.addBookmarkPlace(user, id);
		return ApiResponse.onSuccess(SuccessStatus.OK,null);
	}

	@Operation(
		summary = "북마크 해제",
		description = ""
	)
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("/place")
	public ApiResponse deleteBookmarkPlace(
		@Parameter(hidden = true) @HiddenCountryUser User user,
		@RequestParam Long id
	) {
		userPlaceService.removeBookmarkPlace(user, id);
		return ApiResponse.onSuccess(SuccessStatus.OK,null);
	}

	@Operation(
		summary = "마이페이지 북마크",
		description = ""
	)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/places")
	public ApiResponse<PaginationModel<PlaceThumbnailModel>> getMyBookmarkPlaces(
		@Parameter(hidden = true) @HiddenCountryUser User user,
		@RequestParam @NotNull @Parameter(description = "페이지 번호 - 0 부터 시작", required = true, example = "0") Integer page,
		@RequestParam @NotNull @Parameter(description = "한 페이지 크기", required = true, example = "9") Integer size
	) {
		return ApiResponse.onSuccess(SuccessStatus.OK,userPlaceService.getUserBookmarkPlaces(user, page, size));
	}

}
