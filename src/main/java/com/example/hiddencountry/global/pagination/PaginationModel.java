package com.example.hiddencountry.global.pagination;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationModel<T> {

	List<T> content;
	int totalPage;
	long totalElement;
	int currentPage;
	int currentSize;

	public static <T> PaginationModel<T> toPaginationModel(List<T> content ,Page page) {
		return PaginationModel.<T>builder()
			.content(content)
			.totalElement(page.getTotalElements())
			.currentPage(page.getNumber())
			.currentSize(page.getSize())
			.totalPage(page.getTotalPages())
			.build();
	}
}
