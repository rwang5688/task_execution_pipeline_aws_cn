#!/bin/bash
# AWS specific environment variables
export AWS_ACCOUNT_ID=700702834148
export TARGET_CLOUD=aws
export TARGET_REGION=us-west-2

# task-list workflow and database specific environment variables
export TASK_LIST_PREPROCESS_DATA_BUCKET=task-list-preprocess-data-bucket-xcalibyte-com-cn
export TASK_LIST_LOG_DATA_BUCKET=task-list-log-data-bucket-xcalibyte-com-cn
export TASK_LIST_RESULT_DATA_BUCKET=task-list-result-data-bucket-xcalibyte-com-cn
export TASK_LIST_SUBMIT_TASK_QUEUE=task-list-submit-task-queue-xcalibyte-com-cn
export TASK_LIST_PROCESS_TASK_QUEUE=task-list-process-task-queue-xcalibyte-com-cn
export TASK_LIST_UPDATE_TASK_QUEUE=task-list-update-task-queue-xcalibyte-com-cn
export TASK_LIST_UPDATE_TASK_LOG_STREAM_QUEUE=task-list-update-task-log-stream-queue-xcalibyte-com-cn
export TASK_LIST_TASK_TABLE=task-list-task-table-xcalibyte-com-cn

