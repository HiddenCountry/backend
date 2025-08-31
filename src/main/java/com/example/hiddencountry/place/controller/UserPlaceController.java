package com.example.hiddencountry.place.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.place.service.UserPlaceService;
import com.example.hiddencountry.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class UserPlaceController {

	private final UserPlaceService userPlaceService;

	@Operation(
		summary = "북마크",
		description = ""
	)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PostMapping("/bookmark")
	public void saveBookmarkPlace(
		@Parameter(hidden = true) @HiddenCountryUser User user,
		@RequestParam Long id
	) {
		userPlaceService.addBookmarkPlace(user, id);
	}

	@Operation(
		summary = "북마크 해제",
		description = ""
	)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/bookmark")
	public void deleteBookmarkPlace(
		@Parameter(hidden = true) @HiddenCountryUser User user,
		@RequestParam Long id
	) {
		userPlaceService.removeBookmarkPlace(user, id);
	}



}
