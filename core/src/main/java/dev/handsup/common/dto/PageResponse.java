package dev.handsup.common.dto;

import java.util.List;

import org.springframework.data.domain.Slice;

public record PageResponse<T>(
	List<T> content,
	long size,
	boolean hasNext
) {
	public static <T> PageResponse<T> of(Slice<T> page) {
		return new PageResponse<>(
			page.getContent(),
			page.getNumberOfElements(),
			page.hasNext()
		);
	}
}
