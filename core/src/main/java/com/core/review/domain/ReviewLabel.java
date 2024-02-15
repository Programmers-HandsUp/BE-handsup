package com.core.review.domain;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

	@Column(name = "review_label_value")
	@Enumerated(STRING)
	private ReviewLabelValue value;
}
