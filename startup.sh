#!/bin/sh

nohup java -jar manage.jar -Dspring.profiles.active=prod > /dev/null 2>&1 &