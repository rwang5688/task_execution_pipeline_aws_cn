#!/bin/bash
. ./.env
. checkenv.sh

cluster_name=$ECS_CLUSTER_NAME
task_definition=$ECS_TASK_DEFINITION
subnets=$ECS_TASK_NETWORK_AWS_VPC_SUBNET
security_groups=$ECS_TASK_NETWORK_AWS_VPC_SECURITY_GROUP

aws ecs run-task --cluster $cluster_name \
        --launch-type "FARGATE" \
        --task-definition $task_definition \
        --count 1 \
        --platform-version "LATEST" \
        --network-configuration "awsvpcConfiguration={subnets=[$subnets],securityGroups=[$security_groups], assignPublicIp=ENABLED}" \
        --overrides "containerOverrides=[]"
