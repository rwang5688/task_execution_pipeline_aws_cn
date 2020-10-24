#!/bin/bash
# set env vars
. ./env.sh

# submit task based on task_id1_context_user_id.json
# read ./xcalagent contents: fileinfor.json, preprocess.tar.gz, source_code.zip
echo "[CMD] python3 submit_task.py $1"
python3 ./submit_task.py $1

