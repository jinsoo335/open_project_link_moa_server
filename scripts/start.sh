#!/bin/bash

PROJECT_ROOT=/home/ubuntu/action
JAR_FILE=/home/ubuntu/action/build/libs/demo-0.0.1-SNAPSHOT.jar

nohup java -jar $JAR_FILE &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "실행된 프로세스 아이디 $CURRENT_PID 입니다."