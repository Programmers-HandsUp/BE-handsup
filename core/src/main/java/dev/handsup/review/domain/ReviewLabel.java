package dev.handsup.review.domain;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class ReviewLabel {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "review_label_id")
	private Long id;

	@Column(name = "review_label_value", nullable = false)
	private String value;

	@Builder
	private ReviewLabel(String value) {
		this.value = value;
	}

	//==테스트용 생성자==//
	public ReviewLabel(Long id, String value) {
		this.id = id;
		this.value = value;
	}

	public static ReviewLabel from(String value) {
		return ReviewLabel.builder()
			.value(value)
			.build();
	}

}
