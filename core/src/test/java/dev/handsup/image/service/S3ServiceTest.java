package dev.handsup.image.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.amazonaws.services.s3.AmazonS3;

import dev.handsup.common.exception.ValidationException;
import dev.handsup.image.exception.ImageErrorCode;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

	@Mock
	private AmazonS3 amazonS3;

	@InjectMocks
	private S3Service s3Service;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(s3Service, "bucket", "bucket-name");
	}

	@DisplayName("[이미지를 s3에 업로드하고 url을 반환받을 수 있다.]")
	@Test
	void uploadImages() {
	    //given
		MockMultipartFile file1 = new MockMultipartFile("file1", "test1.jpg", "image/jpeg",
			"file content".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file2", "test2.jpg", "image/jpeg",
			"file content".getBytes());
		String expectedUri = "https://aws.com/bucket-name/images/";

		given(amazonS3.getUrl(any(), any()))
			.willAnswer(invocation -> {
				String bucketName = invocation.getArgument(0);
				String fileName = invocation.getArgument(1);
				System.out.println("fileName = " + fileName);
				return new java.net.URL("https", "aws.com",
					"/" + bucketName + "/" + fileName);
			});

	    //when
		List<String> uploadedUrls = s3Service.uploadImages(List.of(file1, file2));

	    //then
		assertAll(
			() -> assertThat(uploadedUrls).hasSize(2),
			() -> assertThat(uploadedUrls.get(0)).contains(expectedUri),
			() -> assertThat(uploadedUrls.get(1)).contains(expectedUri)
		);
	}



	@DisplayName("[이미지 파일 확장자가 유효하지 않을 경우, 예외를 반환한다.]")
	@Test
	void uploadImages_fail() {
	    //given
		MockMultipartFile file = new MockMultipartFile("file1", "test1.wow", "image/jpeg",
			"file content".getBytes());

		//when, then
		assertThatThrownBy(() -> s3Service.uploadImages(List.of(file)))
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(ImageErrorCode.INVALID_FILE_EXTENSION.getMessage());
	}

	@DisplayName("[원본 파일 이름이 0일 경우, 예외를 반환한다.]")
	@Test
	void uploadImages_fail2() {
		//given
		MockMultipartFile file = new MockMultipartFile("file1", "", "image/jpeg",
			"file content".getBytes());

		//when, then
		assertThatThrownBy(() -> s3Service.uploadImages(List.of(file)))
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(ImageErrorCode.EMPTY_FILE_NAME.getMessage());
	}

	@DisplayName("[업로드된 이미지를 각각 삭제할 수 있다.]")
	@Test
	void deleteImages() {
	    //given
	    List<String> imageUrlsToDelete = List.of(
			"https://example.com/bucket-name/images/random-uuid.jpg",
			"https://example.com/bucket-name/images/random-uuid.jpg"
		);

	    //when
	    s3Service.deleteImages(imageUrlsToDelete);

	    //then
		verify(amazonS3, times(imageUrlsToDelete.size())).deleteObject(any(), any()); // url 개수만큼 메서드 호출 보장
	}


}
