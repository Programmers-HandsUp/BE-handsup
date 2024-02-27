package dev.handsup.image.service;

import static java.util.Objects.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import dev.handsup.common.exception.ValidationException;
import dev.handsup.image.exception.ImageErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;
	private static final String s3FolderName = "images";

	private static final List<String> FILE_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".JPG",
		".JPEG", ".PNG", ".webp", ".WEBP");

	public List<String> uploadImages(List<MultipartFile> multipartFileList) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		List<String> imageUrls = new ArrayList<>();

		for (MultipartFile multipartFile : multipartFileList) {
			String fileName = createFileName(multipartFile.getOriginalFilename());
			objectMetadata.setContentLength(multipartFile.getSize());
			objectMetadata.setContentType(multipartFile.getContentType());

			try (InputStream inputStream = multipartFile.getInputStream()) {
				amazonS3.putObject(
					new PutObjectRequest(bucket + "/" + s3FolderName, fileName, inputStream,
						objectMetadata)
						.withCannedAcl(CannedAccessControlList.PublicRead)
				);
				imageUrls.add(amazonS3.getUrl(bucket + "/" + s3FolderName, fileName).toString());
			} catch (IOException exception) {
				throw new ValidationException(ImageErrorCode.FAILED_TO_UPLOAD);
			}
		}
		return imageUrls;
	}

	public void deleteImages(List<String> imageUrls) {
		try {
			imageUrls.forEach(
				imageUrl -> amazonS3.deleteObject(bucket + "/" + s3FolderName, imageUrl)
			);
		} catch (SdkClientException exception) {
			throw new ValidationException(ImageErrorCode.FAILED_TO_REMOVE);
		}
	}

	private String createFileName(String fileName) {
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	private String getFileExtension(String fileName) {
		if (isNull(fileName) || fileName.isBlank()) {
			throw new ValidationException(ImageErrorCode.EMPTY_FILE_NAME);
		}

		String extension = fileName.substring(fileName.lastIndexOf("."));
		if (!FILE_EXTENSIONS.contains(extension)) {
			throw new ValidationException(ImageErrorCode.INVALID_FILE_EXTENSION);
		}
		return extension;
	}
}
