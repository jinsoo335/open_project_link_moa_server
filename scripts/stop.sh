#!/usr/bin/env sh

PROJECT_ROOT="/home/ubuntu/action"


BUILD_JAR=$(ls /home/ubuntu/action/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)

CURRENT_PID=$(pgrep -f $JAR_FILE)

if [ -z $CURRENT_PID ]
then
  echo "현재 실행중인 애플리케이션이 없습니다"
else
  echo "실행중인 $CURRENT_PID 애플리케이션이 종료 "
  kill -9 $CURRENT_PID
fi