package dev.handsup.common.dto;

import static lombok.AccessLevel.*;

import org.springframework.data.domain.Slice;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class CommonMapper {

	public static <T> PageResponse<T> toPageResponse(Slice<T> page) {
		return PageResponse.of(page);
	}
}
