package com.rwang5688;

import java.lang.StringBuilder;
import java.util.Map;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.lambda.model.GetAccountSettingsRequest;
import software.amazon.awssdk.services.lambda.model.GetAccountSettingsResponse;
import software.amazon.awssdk.services.lambda.model.ServiceException;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import software.amazon.awssdk.services.lambda.model.AccountUsage;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.rwang5688.dal.TaskTable;
import com.rwang5688.dal.Task;
import com.rwang5688.dal.IssueTable;
import com.rwang5688.dal.Issue;
import com.rwang5688.s3.S3File;

// Handler value: example.Handler
public class GenerateTaskSummaryHandler implements RequestHandler<SQSEvent, String>{
  private static final Logger logger = LoggerFactory.getLogger(GenerateTaskSummaryHandler.class);
  private static final LambdaAsyncClient lambdaClient = LambdaAsyncClient.create();
  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public GenerateTaskSummaryHandler()
  {
    CompletableFuture<GetAccountSettingsResponse> accountSettings = lambdaClient.getAccountSettings(GetAccountSettingsRequest.builder().build());
    try {
      GetAccountSettingsResponse settings = accountSettings.get();
    } catch(Exception e) {
      e.getStackTrace();
    }
  }

  private String stripDoubleQuotes(String s) {
    String result = s.replace("\"", "");
    return result;
  }

  @Override
  public String handleRequest(SQSEvent event, Context context)
  {
    String response = new String();

    // call Lambda API
    logger.info("Getting account settings");
    CompletableFuture<GetAccountSettingsResponse> accountSettings =
        lambdaClient.getAccountSettings(GetAccountSettingsRequest.builder().build());

    // log execution details
    logger.info("ENVIRONMENT VARIABLES: {}", gson.toJson(System.getenv()));
    logger.info("CONTEXT: {}", gson.toJson(context));
    logger.info("EVENT: {}", gson.toJson(event));

    // process event
    for(SQSMessage msg : event.getRecords()){
      logger.info("Process msg: " + msg.getBody());
      try {
        // get Task attributes
        ObjectMapper mapper = new ObjectMapper();

        JsonNode env = mapper.readTree(gson.toJson(System.getenv()));
        logger.info("env: " + env.toString());
        String bucket_name = env.get("RESULT_DATA_BUCKET").asText();
        logger.info("bucket_name: " + bucket_name);

        JsonNode body = mapper.readTree((String) msg.getBody());
        logger.info("body: " + body.toString());
        JsonNode task = body.get("task");
        logger.info("task: " + task.toString());
        String user_id = task.get("user_id").asText();
        String task_id = task.get("task_id").asText();
        String task_issues_csv = task.get("task_issues_csv").asText();
        logger.info("user_id: " + user_id);
        logger.info("task_id: " + task_id);
        logger.info("task_issues_csv: " + task_issues_csv);

        // get Task by user_id and task_id
        TaskTable taskTable = new TaskTable();
        /*
        logger.info("[DEBUG]List all Tasks:");
        List<Task> taskRecords = taskTable.list();
        for (Task t : taskRecords) {
          logger.info("task=" + t.toString());
        }
        logger.info("[DEBUG]:Done.");
        */
        logger.info("Get Task:");
        Task taskRecord = taskTable.get(user_id, task_id);
        if (taskRecord != null) {
          logger.info("Task: " + taskRecord.toString());
        } else {
          logger.info("Task not found for user_id=" + user_id + ", task_id=" + task_id + ".");
        }

        // get Issues by task_id
        //List<Issue> taskIssues = new IssueTable().getTaskIssues(task_id);
        //for (Issue issue : taskIssues) {
        //  logger.info("Issue: " + issue.toString());
        //}

        // download task_issues_csv file
        S3File s3File = new S3File();
        String bucketName = bucket_name;
        String objectKey = user_id + "/" + task_id + "/" + task_issues_csv;
        String filePath = "/tmp/" + task_issues_csv;
        boolean success = s3File.downloadObject(bucketName, objectKey, filePath);
        if (success) {
          logger.info("Successfully downloaded: " +
                        "bucketName=" + bucketName + ", " +
                        "objectKey=" + objectKey + ", " +
                        "filePath=" + filePath + ".");
        } else {
          logger.info("Failed to download: " +
                        "bucketName=" + bucketName + ", " +
                        "objectKey=" + objectKey + ", " +
                        "filePath=" + filePath + ".");
        }
      } catch (Exception ex) {
        logger.error("Error in retrieving task issues: " + ex);
      }
      logger.info("Done with msg: " + msg.getBody());
    }

    // process Lambda API response
    try {
      GetAccountSettingsResponse settings = accountSettings.get();
      response = gson.toJson(settings.accountUsage());
      logger.info("Account usage: {}", response);
    } catch(Exception e) {
      e.getStackTrace();
    }
    return response;
  }
}

