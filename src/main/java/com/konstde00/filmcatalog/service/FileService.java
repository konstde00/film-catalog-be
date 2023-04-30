package com.konstde00.filmcatalog.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.konstde00.filmcatalog.model.enums.ItemType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.amazonaws.services.s3.model.CannedAccessControlList.Private;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileService {

    @Value("${aws.s3.bucket}")
    private String doSpaceName;

    @Value("${aws.s3.endpoint}")
    private String doSpaceEndpoint;

    private final AmazonS3 s3Client;

    @SneakyThrows
    public void updateUsersAvatar(Long userId, MultipartFile photo) {

        String filePath = "users/" + userId + "/avatar";

        upload(filePath, photo);
    }

    @SneakyThrows
    public void updateItemPhoto(Long id, ItemType itemType, MultipartFile photo) {

        String filePath = itemType.getS3ItemPrefix() + "/" + id + "/photo";

        upload(filePath, photo);
    }

    @SneakyThrows(IOException.class)
    public void upload(String path, MultipartFile file) {

        log.info("'upload' invoked with path - {} and file - {} ", path, file);

        var metadata = createMetadata(file);

        s3Client.putObject(new PutObjectRequest(doSpaceName, path, file.getInputStream(), metadata)
                .withCannedAcl(Private));

        var OUTER_URL = "https://" + doSpaceName + "." + doSpaceEndpoint + "/";
        var url = OUTER_URL + path;

        log.info("'upload' returned - {} ", url);
    }

    @SneakyThrows(IOException.class)
    public void uploadDefaultImage(Long id, ItemType itemType) {

        log.info("'uploadDefaultImage' invoked with filmId - {} ", id);

        var path = itemType.getS3ItemPrefix() + "/" + id + "/photo";
        File file = new File("/Users/kostiantyndementiev/IdeaProjects/filmcatalog/src/main/resources/img.png");
        var metadata = createMetadata(file);

        s3Client.putObject(new PutObjectRequest(doSpaceName, path, new FileInputStream(file), metadata)
                .withCannedAcl(Private));

        log.info("'uploadDefaultImage' returned 'Success'");
    }

    @SneakyThrows(IOException.class)
    private ObjectMetadata createMetadata(File file) {

        log.info("'createMetadata' invoked");

        var metadata = new ObjectMetadata();
        metadata.setContentLength(new FileInputStream(file).available());
        metadata.setContentType("image/x-png");
        log.info("'createMetadata' returned 'Success'");

        return metadata;
    }

    @SneakyThrows(IOException.class)
    private ObjectMetadata createMetadata(MultipartFile multipartFile) {

        log.info("'createMetadata' invoked");

        var metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getInputStream().available());

        if (multipartFile.getContentType() != null && !"".equals(multipartFile.getContentType())) {
            metadata.setContentType(multipartFile.getContentType());
        }

        log.info("'createMetadata' returned 'Success'");

        return metadata;
    }
}
