package dev.handsup.common.domain;

import lombok.Getter;

@Getter
public enum CacheType {
	USERS(
		"users",      	// 캐시 이름: users
		10 * 60,       				// 만료 시간: 10 분
		10000         				// 최대 갯수: 10000
	);

	CacheType(
		String cacheName,
		int expireSecondsAfterWrite,
		int maximumSize
	) {
		this.cacheName = cacheName;
		this.expireAfterWrite = expireSecondsAfterWrite;
		this.maximumSize = maximumSize;
	}

	private final String cacheName;
	private final int expireAfterWrite;
	private final int maximumSize;
}
