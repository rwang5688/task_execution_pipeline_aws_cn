# task_execution_pipeline_aws_cn
Task Execution Pipeline for processing various static analysis tasks running on AWS-CN.

# prerequisites
You need to handle the AWS account and permissions.
You need to deploy a ECS fargate cluster with 0 desired number of tasks on the ECS console.

# deploy
First replace all 'fill-this-value' with correct value in .env file.
Then run deploy.sh to deploy task execution pipeline.

# remove
You need to delete the ECS fargate cluster on the ECS console.
Run remove.sh to remove the task execution pipeline.

# problem and solution
1. If you provide the incorrect value to AWS account id in .env file, all the AWS lambda function will fail to deploy.
Provide the correct value and run deploy.sh again.
2. If you provide the incorrect value to ECS_XXX related variables in .env file, run ECS task will fail. 
And messages will be blocked in process task queue. Provide the correct value and redeploy create task lambda function to make it works in the future.
For messages that blocked in process task queue, run run_ecs_task.sh manually can consume the messages in process task queue and all later task will continue.

# enable xray trace
If you want to trace more information, then you need to instrument more code into the task execution pipeline.
## enable/disable xray trace for lambda function
1.To enable: add "tracing: true" statement to the lambda function's definition in its serverless.yml file.
 Currently, create task and update task lambda functions' tracing functionality is enabled by default.
2.To disable xray trace for lambda function, just remove "tracing: true' statement in the lambda function's definition in its serverless.yml file.
## enable/disable xray trace for ECS fargate
1.To enable: when create task definition, need to config xray-daemon container with our self-defined container.
 And then in the IAM console, add AWSXrayDaemonWriteAccess policy to the ecsTaskExecutionRole.
 For unknown reason, config xray's context_missing to 'LOG_ERROR' to make process task runs ok.
2.To disable: no need xray-daemon container and no need add AWSXrayDaemonWriteAccess policy. Leave the instrumented code to enable xray trace in process task is ok.
 
