#!/bin/bash
## 加载配置，避免获取不到java_home 
source /etc/profile

SERVICE_NAME=message-producer
SERVICE_HOME=/home/omni-services/$SERVICE_NAME
PROG=$SERVICE_HOME/
PIDFILE=$SERVICE_HOME/$SERVICE_NAME.pid
JARFILE=$SERVICE_HOME/$SERVICE_NAME.jar
LOG_FILE=$SERVICE_NAME.log

#堆配置：服务模式(启动慢，一次编译，运行效率高)、最小内存512m、最大内存512m、年轻代大512m
HEAP_OPTIONS="-server -Xms512m -Xmx512m -Xmn256m"
#栈配置：设置每个线程的栈大小，DK5.0以后每个线程栈大小为1M，依据实际情况设定。较少的值内存能生成更多的线程（受系统限制），较大的值可（如2M）能会再一定程度上降低性能。
#STACKS_OPTIONS=-Xss128K
## GC配置：gc详情、gc时间、gc日志文件位置、gc堆栈信息
GC_OPTIONS="-XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:$SERVICE_HOME/$SERVICE_NAME_gc.log -XX:+PrintHeapAtGC"
## gc收集器：Java提供串行收集器、并行收集器、并发收集器三种，串行收集器只适用于小数据量的情况，并行收集器主要以到达一定的吞吐量为目标
# 并行垃圾收集器：配置仅对年轻代有效、并行线程数20(建议与处理器数目相等)
GC_COLLECTOR_PARAL="-XX:+UseParallelGC -XX:ParallelGCThreads=4 -XX:+UseParallelOldGC"
## 老年代垃圾收集器：年老代垃圾收集方式为并行收集(1.6以后支持)
# CMS垃圾收集器：初始标记->并发标记->并发预清理->重新标记->并发清理->并发重置(标记-清理 算法),CMS默认在老年代空间使用68%时候启动垃圾回收。可以通过-XX:CMSinitiatingOccupancyFraction=n来设置这个阀值。
#GC_COLLECTOR_CMS="-XX:+UseConcMarkSweepGC -XX:ParallelCMSThreads=4"
## oom配置：OOM时导出堆到文件、导出OOM的路径
#-XX:OnOutOfMemoryError=D:/tools/jdk1.7_40/bin/printstack.bat %p //p代表的是当前进程的pid ，即当程序OOM时，在D:/a.txt中将会生成线程的dump。
OOM_OPTIONS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$SERVICE_HOME/$SERVICE_NAME.dump"
## 类加载监控
#CLASS_LOAD_MONITOR="-XX:+TraceClassLoading -XX:+PrintClassHistogram"
## 其他配置：新生的转老年代GC次数、禁止程序GC、set/get使用本地方法、加快编译、锁机制的性能改善
#OTHER_OPTIONS="-XX:MaxTenuringThreshold=15 -XX:+DisableExplicitGC -XX:+UseFastAccessorMethods -XX:+AggressiveOpts -XX:+UseBiasedLocking"
## jvm 参数配置
JAVA_OPTS="$HEAP_OPTIONS $PERM_OPTIONS $GC_OPTIONS $GC_COLLECTOR_PARAL $OOM_OPTIONS"


cd $SERVICE_HOME

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
           echo "-----Exit"
           exit 1
        else
           echo "-----Jave home: $JAVA_HOME"
           echo "-----Starting $PROG ..."
           echo "-----Java options: $JAVA_OPTS"
           nohup java $JAVA_OPTS -jar $JARFILE  > $LOG_FILE 2>&1 &

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
            kill -9 `cat $PIDFILE`  
            RETVAL=$?  
            if [ $RETVAL -eq 0 ]; then
                LS_DATE=`date +%Y%m%d`
                BACK_LOG=$SERVICE_NAME-$LS_DATE.log
                BACK_LOG_FILE=$SERVICE_HOME/$BACK_LOG
                ## 备份日志文件
                if [  -f "$BACK_LOG_FILE" ]; then
                    echo "-----Back log file $BACK_LOG exist and copy log to back file!"
                    echo "--------------------------------------------------------------------------------------------------------------------" >> $BACK_LOG
                    echo `date "+%Y-%m-%d %H:%M:%S"` >> $BACK_LOG
                    echo "-----------------------------------------重新启动-------------------------------------------------------------------" >> $BACK_LOG
                    cat $LOG_FILE >> $BACK_LOG
                    ## 删除日志文件
                    rm -f $LOG_FILE
                else
                    echo "-----Back log file to $BACK_LOG...."
                    mv $LOG_FILE $BACK_LOG
                fi
                ## 删除Pid文件
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

