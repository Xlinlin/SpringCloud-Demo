#!/bin/bash

## app home path
SERVICE_HOME=/home/omni-services
## app name
SERVICE_NAME=content-management-service

cd $SERVICE_HOME/$SERVICE_NAME
PROG=$SERVICE_HOME/$SERVICE_NAME
PIDFILE=$SERVICE_HOME/$SERVICE_NAME/$SERVICE_NAME.pid
JARFILE=$SERVICE_HOME/$SERVICE_NAME/$SERVICE_NAME.jar


## get app status
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

## check and start,stop,status
case "$1" in
    start)
        status
        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
            echo "$PIDFILE exists, process is already running or crashed"
            exit 1
        fi

        echo "Starting $PROG ..."
        nohup  $JAVA_HOME/bin/java -server -Xms512m -Xmx512m -jar $JARFILE > content-management-service.log 2>&1 &

        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
            echo "$PROG is started"
            echo $! > $PIDFILE
            exit 0
        else
            echo "Stopping $PROG"
            rm -f $PIDFILE
            exit 1
        fi
        ;;
    stop)
        status
        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
            echo "Shutting down $PROG"
            kill `cat $PIDFILE`
            RETVAL=$?
            if [ $RETVAL -eq 0 ]; then
                rm -f $PIDFILE
            else
                echo "Failed to stopping $PROG"
            fi
        fi
        ;;
    status)
        status
        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
            PID=$(cat $PIDFILE)
            echo "$PROG is running ($PID)"
        else
            echo "$PROG is not running"
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
