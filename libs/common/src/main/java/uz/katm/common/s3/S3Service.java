package uz.katm.common.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "s3.access-key")
public class S3Service {

    private final S3Client s3Client;

    @Value("${s3.bucket-name}")
    private String bucketName;

    public String upload(String folder, String originalFilename, InputStream inputStream, long size, String contentType) {
        String key = folder + "/" + UUID.randomUUID() + "_" + originalFilename;
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName).key(key).contentType(contentType).build();
        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, size));
        log.info("Uploaded file to S3: {}", key);
        return key;
    }

    public InputStream download(String key) {
        GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(key).build();
        return s3Client.getObject(request);
    }

    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
        log.info("Deleted file from S3: {}", key);
    }

    public String getPresignedUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}
