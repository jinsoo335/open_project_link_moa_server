#!/bin/bash

PROJECT_ROOT="/home/ubuntu/action"
JAR_FILE="/build/libs/demo-0.0.1-SNAPSHOT.jar"

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

CURRENT_PID=$(pgrep -f $JAR_FILE)

if [ -z $CURRENT_PID ]; then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션이 없습니다"
else
  echo "$TIME_NOW > 실행중인 $CURRENT_PID 애플리케이션이 종료 "
  kill -15 $CURRENT_PID
fi