package com.rwang5688.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;


public class S3File {
    private static final Logger logger = LoggerFactory.getLogger(S3File.class);

    private S3Connection s3_connection;
    private S3Client s3;

    public S3File() {
        this.s3_connection = S3Connection.getInstance();
        this.s3 = this.s3_connection.getS3();
    }

    private boolean writeObjectFile(String filePath, byte[] objectData) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(filePath);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(objectData);
        } catch (IOException e) {
            logger.error("S3File.writeObjectFile: " + e.toString());
            return false;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error("S3File.writeObjectFile: " + e.toString());
                    return false;
                }
            }
        }
        return true;
    }

    public boolean downloadObject(String bucketName, String objectKey, String filePath) {
        try {
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
            byte[] objectData = objectBytes.asByteArray();
            Boolean success = writeObjectFile(filePath, objectData);

            if (success) {
                logger.info("S3File.downloadObject: Successfully downloaded " +
                                                    "bucketName=" + bucketName + ", " +
                                                    "objectKey=" + objectKey + ", " +
                                                    "filePath=" + filePath + ".");
                return true;
            } else {
                logger.info("S3File.downloadObject: Failed to download " +
                                                    "bucketName=" + bucketName + ", " +
                                                    "objectKey=" + objectKey + ", " +
                                                    "filePath=" + filePath + ".");
                return false;
            }
        } catch (S3Exception e) {
            logger.error("S3File.downloadObject: " + e.awsErrorDetails().errorMessage());
            return false;
        }
    }

    private byte[] readObjectFile(String filePath) {
        byte[] bytesArray = null;
        FileInputStream fileInputStream = null;
        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException e) {
            logger.error("S3File.readObjectFile: " + e.toString());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error("S3File.readObjectFile: " + e.toString());
                }
            }
        }
        return bytesArray;
    }

    public String uploadObject(String bucketName, String objectKey, String filePath) {
        try {
            byte[] objectData = readObjectFile(filePath);
            PutObjectRequest request = PutObjectRequest.builder()
                                                    .bucket(bucketName)
                                                    .key(objectKey)
                                                    .build();
            RequestBody requestBody = RequestBody.fromBytes(objectData);
            PutObjectResponse response = s3.putObject(request, requestBody);
            return response.eTag();
        } catch (S3Exception e) {
            logger.error("S3File.uploadObject: " + e.awsErrorDetails().errorMessage());
            return "";
        }
    }
}

