#!/usr/bin/env sh

REPOSITORY=/home/ubuntu/action

chmod -R 777 $REPOSITORY


sh $REPOSITORY/gradlew clean build


echo "> 새 어플리케이션 배포"
nohup java -jar $REPOSITORY/build/libs/demo-0.0.1-SNAPSHOT.jar >> $REPOSITORY/deploy.log &