#!/bin/bash
. ./.env
. checkenv.sh


function remove () {
  for SERVICE in "${SERVICES[@]}"
  do
    echo ----------[ removing $SERVICE ]----------
    cd $SERVICE
    serverless remove
    cd ..
  done
}


# remove data
aws s3 rm s3://${TASK_EXEC_PREPROCESS_DATA_BUCKET} --recursive
aws s3 rm s3://${TASK_EXEC_CACHE_DATA_BUCKET} --recursive
aws s3 rm s3://${TASK_EXEC_LOG_DATA_BUCKET} --recursive
aws s3 rm s3://${TASK_EXEC_RESULT_DATA_BUCKET} --recursive

# remove functions
SERVICES=(generate-task-summary upload_task_issues update_task create_task)
remove

# for now, only aws has Lambda function with container image
if [ ${TARGET_CLOUD} == "aws" ] then
  SERVICES=(process_task_service)
  remove
fi

# remove resources
echo ----------[ removing resources ]----------
cd resources
. remove.sh
cd ..

