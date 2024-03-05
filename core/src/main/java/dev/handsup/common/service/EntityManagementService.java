package dev.handsup.common.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import dev.handsup.common.exception.CommonErrorCode;
import dev.handsup.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntityManagementService {

	public <T, ID> T getEntity(JpaRepository<T, ID> repository, ID id) {
		return repository.findById(id)
			.orElseThrow(() -> new NotFoundException(CommonErrorCode.NOT_FOUND_BY_ID));
	}
}
