#!/bin/bash
## 加载配置，避免获取不到java_home 
source /etc/profile

SERVICE_HOME=/home/admin/services
SERVICE_NAME=omni-basis-service

cd $SERVICE_HOME/$SERVICE_NAME
PROG=$SERVICE_HOME/$SERVICE_NAME
PIDFILE=$SERVICE_HOME/$SERVICE_NAME/$SERVICE_NAME.pid  
JARFILE=$SERVICE_HOME/$SERVICE_NAME/$SERVICE_NAME.jar

SKYWALKING_AGENT=-javaagent:/home/admin/agent/skywalking-agent.jar
SKYWALKING_SERVCIE_NAME=OMNI-BASIS-SERVICE
  
status() {  
    if [ -f $PIDFILE ]; then  
        PID=$(cat $PIDFILE)  
        if [ ! -x /proc/${PID} ]; then  
            return 1  
        else  
            return 0  
        fi  
    else  
        return 1  
    fi  
}  
  
case "$1" in  
    start)  
        status  
        RETVAL=$?  
        if [ $RETVAL -eq 0 ]; then  
            echo "-----$PIDFILE exists, process is already running or crashed"  
            exit 1  
        fi  
        
        ##检测 java环境
        if [ ! -n $JAVA_HOME ]; then
           echo "-----Please check JAVA_HOME!"
           echo "-----Exist"
           exist 1
        else
           echo "-----Jave home: $JAVA_HOME"
           echo "-----Starting $PROG ..." 
           #nohup java -server -Xms512m -Xmx512m -jar $JARFILE  > $SERVICE_NAME.log 2>&1 &
           nohup java -server -Xms512m -Xmx512m $SKYWALKING_AGENT -Dskywalking.agent.service_name=$SKYWALKING_SERVCIE_NAME -jar $JARFILE  > $SERVICE_NAME.log 2>&1 &

           RETVAL=$?
           if [ $RETVAL -eq 0 ]; then
               echo "-----$PROG is started"
               echo $! > $PIDFILE
               exit 0
           else
               echo "-----Stopping $PROG"
               rm -f $PIDFILE
               exit 1
           fi        
        fi
        ;;  
    stop)  
        status  
        RETVAL=$?  
        if [ $RETVAL -eq 0 ]; then  
            echo "-----Shutting down $PROG"  
            kill -15 `cat $PIDFILE`  
            RETVAL=$?  
            if [ $RETVAL -eq 0 ]; then  
                rm -f $PIDFILE  
            else  
                echo "-----Failed to stopping $PROG"  
            fi  
        fi  
        ;;  
    status)  
        status  
        RETVAL=$?  
        if [ $RETVAL -eq 0 ]; then  
            PID=$(cat $PIDFILE)  
            echo "-----$PROG is running ($PID)"  
        else  
            echo "-----$PROG is not running"  
        fi  
        ;;  
    restart)  
        $0 stop  
        $0 start  
        ;;  
    *)  
        echo "Usage: $0 {start|stop|restart|status}"  
        ;;  
esac  
