package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.ResponseDto.S3UrlResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${AWS_BUCKET_NAME}")
    private String bucketName;

    public S3Service(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }
    public S3UrlResponse generateGetPresignedUrl(String menuItemId, String restaurantUserId) {

        String filePath = "restaurant/" + restaurantUserId + "/menu/" + menuItemId + ".jpg";
        S3UrlResponse urlResponse = new S3UrlResponse();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();

        // you can change expiration time here
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        urlResponse.setS3Url(presignedRequest.url().toString());
        return urlResponse;
    }

    public S3UrlResponse generatePutPresignedUrl(String menuItemId, String restaurantUserId) {
        String filePath = "restaurant/" + restaurantUserId + "/menu/" + menuItemId + ".jpg";
        S3UrlResponse urlResponse = new S3UrlResponse();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();


        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        urlResponse.setS3Url(presignedRequest.url().toString());
        return urlResponse;
    }
}
