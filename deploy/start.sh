#!/bin/bash
BUILD_JAR=$(ls /home/ubuntu/app/deploy/*.jar)
JAR_NAME=$(basename "$BUILD_JAR")

# shellcheck disable=SC2129
echo "> data now : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>/home/ubuntu/deploy.log
echo "> build 파일명: $JAR_NAME" >>/home/ubuntu/deploy.log

echo "> build 파일 복사" >>/home/ubuntu/deploy.log
DEPLOY_PATH=/home/ubuntu/
cp "$BUILD_JAR" $DEPLOY_PATH
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포" >>/home/ubuntu/deploy.log

COUNT=0

while :
do
  CURRENT_PID=$(pgrep -f "$JAR_NAME")
  echo "> 현재 실행중인 애플리케이션 pid 확인 : $CURRENT_PID" >>/home/ubuntu/deploy.log
  if [ -z "$CURRENT_PID" ]; then
    break
  fi

  if [ $COUNT -eq 0 ]; then
    COUNT=10
    echo "> sudo kill -9 $CURRENT_PID" >>/home/ubuntu/deploy.log
  fi

  if [ $COUNT -eq 0 ]; then
    echo "> 현재 구동중인 애플리케이션을 종료합니다." >>/home/ubuntu/deploy.log
  fi
  sleep 5

done

echo "> 현재 구동중인 애플리케이션이 없으므로 새로운 애플리케이션을 시작합니다." >>/home/ubuntu/deploy.log
nohup java -jar "$DEPLOY_JAR" >>/home/ubuntu/deploy.log 2>/home/ubuntu/deploy_err.log &
