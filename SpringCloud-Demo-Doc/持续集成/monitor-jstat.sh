#!/bin/bash

## 定时监控本机器下所有java应用的 JVM信息
# 定时任务配置
# crontab -e
# 写入如下，5分钟执行一次检测
# */5 * * * * /home/admin/monitor-jstat.sh >> /data/logs/monitor/monitor.log 2>&1

#导入环境变量，需要查看当前的用户的环境变量，否则会出现jps ifconfig环境变量不可用错误提示
export PATH=$PATH:/usr/local/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/data/jdk1.8.0_181/bin:/home/admin/.local/bin:/home/admin/bin

# 老年代告警阈值
oldwarn="99000"
# 元空间(永久代)告警阈值
metawarn="99900"

# 手机号码，多个用逗号隔开
mobile="17727821863"
# 短信发送url地址
sendsms="http://api.pureh2b.com/dreamWebSms/batchSend"
jsonhead="Content-type:application/json"

## 当前日期
sysdate=`date`
## 当前服务器IP地址
addr=`ifconfig -a|grep -w inet|grep -v 127.0.0.1|grep -v 172.16.1.1|awk '{print $2}'|cut -d : -f 2`

for pid in `jps -v |grep -v Jps |grep -v logstash-watch-collection |awk '{print $1}'`

do
 ## 应用名称
 name=`jps -v |grep -v Jps |grep -v logstash-watch-collection |grep $pid |awk '{print $2}'`

 #jvm内存监控
 #echo "##################开始Java应用:${name}内存监控############################"
 #echo "监控日期：     $sysdate"
 #echo "进程PID：      $pid"
 #echo "进程名称：     $name"
 #echo "当前服务器IP： $addr"

 # 堆栈信息
 jstat=$(jstat -gcutil ${pid})
 #echo -e "jstat: \n $jstat"
 #S0=$(echo ${jstat}|sed -n '2p' |awk '{print $1}')
 #S0=$(echo ${jstat}|awk '{print $12}')
 #echo $S0

 # 新生代
 eden=$(echo ${jstat}|awk '{print $14}')
 from=$(echo ${jstat}|awk '{print $12}')
 tospc=$(echo ${jstat}|awk '{print $13}')
 # 老年代
 oldge=$(echo ${jstat}|awk '{print $15}')
 # 永久代 JDK8已经废弃改用 元空间代替
 meta=$(echo ${jstat}|awk '{print $16}')

 #echo "Eden Space:           ${eden}%"
 #echo "From Space:           ${from}%"
 #echo "To Space:             ${tospc}%"
 #echo "Old Generation:       ${oldge}%"
 #echo "Meta Space:           ${meta}%"

 ## 将读取到的各区的值*1000，以便做下一次的比较操作
 #eden1=`gawk -v x=$eden -v y=1000 'BEGIN{printf "%.0f\n",x*y}'`
 #from1=`gawk -v x=$from -v y=1000 'BEGIN{printf "%.0f\n",x*y}'`
 #tospc1=`gawk -v x=$tospc -v y=1000 'BEGIN{printf "%.0f\n",x*y}'`
 oldge1=`gawk -v x=$oldge -v y=1000 'BEGIN{printf "%.0f\n",x*y}'`
 meta1=`gawk -v x=$meta -v y=1000 'BEGIN{printf "%.0f\n",x*y}'`

 #echo "Eden*100 Space:           ${eden1}"
 #echo "From*100 Space:           ${from1}"
 #echo "To*100 Space:             ${tospc1}"
 #echo "Old*100 Generation:       ${oldge1}"
 #echo "Meta*1000 Space:          ${meta1}"


 #if [ ${eden1} -ge "99999" ]
 #then
   #jvm=$jvm+${name}+":Eden Space:"+${eden}+"%超阀值 "
 #fi

 #if [ ${from1} -ge "99999" ]
 #then
   #jvm=$jvm+${name}+":From Space:"+${from}+"%超阀值 "
 #fi

 #if [ ${tospc1} -ge "99999" ]
 #then
   #jvm=$jvm+${name}+":To Space:"+${topspc}+"%超阀值 "
 #fi

 # 仅监控 老年代和元空间，超过90以上报警
 jvm=
 if [ ${oldge1} -ge ${oldwarn} ]
 then
   jvm="${jvm}[老年代 ${oldge}%超阀值]"
 fi
 if [ ${meta1} -ge ${metawarn} ]
 then
   jvm="${jvm}[元空间(永久代) ${meta}%超阈值]"
 fi

 if [ ! -n "$jvm" ]
 then
   echo "应用:${name}-JVM信息正常"
 else
   jvm="应用${name}的JVM告警信息：${jvm}"

   echo "监控日期：     $sysdate"
   echo "进程PID：      $pid"
   echo "进程名称：     $name"
   echo "当前服务器IP： $addr"
   echo $jvm

   #echo "JVM告警信息：${jvm}"
   smsContent='{"mobile":"'$mobile'","content":"'$jvm'","userid":"WL0001","pwd":"123456"}'
   #echo "JVM告警发送短信内容: ${smsContent}"
   # -s 不输出请求信息
   smsResult=$(curl -H ${jsonhead} -X POST -s -d "${smsContent}" ${sendsms})
   #echo "短信返回结果：${smsResult}"
 fi
 #echo "##################结束Java应用:${name}内存监控############################"
done