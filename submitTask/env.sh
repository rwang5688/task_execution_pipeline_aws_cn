#!/bin/bash
# AWS specific environment variables
export AWS_ACCOUNT_ID=200691530094
export TARGET_CLOUD=aws-cn
export TARGET_REGION=cn-northwest-1

# task-list workflow and database specific environment variables
export TASK_EXEC_PREPROCESS_DATA_BUCKET=task-exec-preprocess-data-bucket-xcalibyte-com-cn
export TASK_EXEC_LOG_DATA_BUCKET=task-exec-log-data-bucket-xcalibyte-com-cn
export TASK_EXEC_RESULT_DATA_BUCKET=task-exec-result-data-bucket-xcalibyte-com-cn
export TASK_EXEC_SUBMIT_TASK_QUEUE=task-exec-submit-task-queue-xcalibyte-com-cn
export TASK_EXEC_PROCESS_TASK_QUEUE=task-exec-process-task-queue-xcalibyte-com-cn
export TASK_EXEC_UPDATE_TASK_QUEUE=task-exec-update-task-queue-xcalibyte-com-cn
export TASK_EXEC_UPLOAD_TASK_ISSUES_QUEUE=task-exec-upload-task-issues-queue-xcalibyte-com-cn
export TASK_EXEC_TASK_TABLE=task-exec-task-table-xcalibyte-com-cn
export TASK_EXEC_ISSUE_TABLE=task-exec-issue-table-xcalibyte-com-cn

