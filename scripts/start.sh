#!/bin/bash

REPOSITORY=/home/ubuntu/action

echo "> 새 어플리케이션 배포"
nohup java -jar build/libs/demo-0.0.1-SNAPSHOT.jar &