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
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class UserPlaceController {

	private final UserPlaceService userPlaceService;

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PostMapping("/bookmark")
	public void saveBookmarkPlace(
		@HiddenCountryUser User user,
		@RequestParam Long id
	) {
		userPlaceService.addBookmarkPlace(user, id);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/bookmark")
	public void deleteBookmarkPlace(
		@HiddenCountryUser User user,
		@RequestParam Long id
	) {
		userPlaceService.removeBookmarkPlace(user, id);
	}

}
