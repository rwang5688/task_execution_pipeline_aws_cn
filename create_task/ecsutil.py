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
def run_fargate_task():
    client = get_ecs_client()
    response = client.run_task(
        cluster='scan-default',
        launchType = 'FARGATE',
        taskDefinition='scan-task:17',
        count = 1,
        platformVersion='LATEST',
        networkConfiguration={
            'awsvpcConfiguration': {
                'subnets': [
                    'subnet-0db2bd72f1721a5c4',
                ],
                'securityGroups': ['sg-07a8f6d3484ddb72d'],
                'assignPublicIp': 'ENABLED'
            }
        },
        overrides={
            'containerOverrides': [],
        },
    )
    return str(response)
