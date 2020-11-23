package com.rwang5688.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.io.IOException;

import java.io.OutputStream;
import java.io.FileOutputStream;


import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class S3File {
    private static final Logger logger = LoggerFactory.getLogger(S3File.class);

    private S3Connection s3_connection;
    private S3Client s3;

    public S3File() {
        this.s3_connection = S3Connection.getInstance();
        this.s3 = this.s3_connection.getS3();
    }

    private boolean writeObjectFile(String filePath, byte[] objectData) {
        OutputStream fileOutputStream = null;
        try {
            // Write the data to a local file
            File file = new File(filePath);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(objectData);
        } catch (IOException e) {
            logger.error(e.toString());
            return false;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error(e.toString());
                    return false;
                }
            }
        }
        // success
        return true;
    }

    public boolean downloadObject(String bucketName, String objectKey, String filePath) {
        try {
            logger.info("S3File.downloadObject(): " +
                            "bucketName=" + bucketName + ", " +
                            "objectKey=" + objectKey + ", " +
                            "filePath=" + filePath + ".");

            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
            byte[] objectData = objectBytes.asByteArray();

            Boolean success = writeObjectFile(filePath, objectData);
            if (success) {
                logger.info("S3File.downloadObject(): writeObjectFile done.");
                return true;
            } else {
                logger.error("S3File.downloadObject(): writeObjectFile failed.");
                return false;
            }
        } catch (S3Exception e) {
            logger.error(e.awsErrorDetails().errorMessage());
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
            logger.error(e.toString());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error(e.toString());
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
            logger.error(e.getMessage());
            return "";
        }
    }
}
