
PROJECT_ROOT="/home/ubuntu/open_project_link_moa_server"
JAR_FILE="/build/libs/demo-0.0.1-SNAPSHOT.jar"

APP_LOG="$PROJECT_ROOT/logs/app.log"
ERROR_LOG="$PROJECT_ROOT/logs/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date + %c)

echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG