package com.rwang5688.s3;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.services.s3.S3Client;

public class S3Connection {
    private static S3Connection s3_connection = null;
    private S3Client s3 = null;

    private S3Connection() {
        Region region = Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable()));
        this.s3 = S3Client.builder()
                        .region(region)
                        .build();
    }

    public static S3Connection getInstance() {
        if (s3_connection == null)
            s3_connection = new S3Connection();

        return s3_connection;
    }

    public S3Client getS3() {
        if (s3_connection == null)
            s3_connection = getInstance();

        return s3_connection.s3;
    }
}
