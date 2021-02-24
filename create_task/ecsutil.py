import os
import boto3


def get_ecs_client():
    region_name = ''
    if 'TARGET_REGION' in os.environ:
        region_name = os.environ['TARGET_REGION']
    print('get_ecs_client: region_name=%s' % region_name)

    session = boto3.Session(profile_name=None)
    ecs = session.client('ecs',
        region_name=region_name)
    return ecs


# run_fargate_task
def run_fargate_task(cluster_name, task_definition, aws_vpc_subnet, aws_vpc_security_group):
    client = get_ecs_client()
    response = client.run_task(
        cluster=cluster_name,
        launchType = 'FARGATE',
        taskDefinition=task_definition,
        count = 1,
        platformVersion='LATEST',
        networkConfiguration={
            'awsvpcConfiguration': {
                'subnets': [
                    aws_vpc_subnet,
                ],
                'securityGroups': [aws_vpc_security_group],
                'assignPublicIp': 'ENABLED'
            }
        },
        overrides={
            'containerOverrides': [],
        },
    )
    return str(response)
