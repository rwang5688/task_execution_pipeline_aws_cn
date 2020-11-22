package com.rwang5688.dal;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

public final class DynamoDBConnection {
    private static DynamoDBConnection db_connection = null;
    private DynamoDbClient db = null;
    private DynamoDbEnhancedClient enhancedClient = null;

    private DynamoDBConnection() {
        Region region = Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable()));
        this.db = DynamoDbClient.builder()
                                .region(region)
                                .build();
    }

    public static DynamoDBConnection getInstance() {
        if (db_connection == null)
            db_connection = new DynamoDBConnection();

        return db_connection;
    }

    public DynamoDbClient getDb() {
        if (db_connection == null)
            db_connection = getInstance();

        return db_connection.db;
    }

    public DynamoDbEnhancedClient getEnhancedClient() {
        if (this.enhancedClient == null)
            this.enhancedClient = DynamoDbEnhancedClient.builder()
                                                        .dynamoDbClient(this.db)
                                                        .build();

        return this.enhancedClient;
    }
}

