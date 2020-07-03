#!/bin/bash
#author wxd
## 删除15天以前的数据
## 索引名称： app-log-2020.06.27
## Linux定时任务执行:
##最后linux crontab 设置如下：
##00 00 * * * /bin/bash ${WORK_PATH}/batch_del.sh del >> ${WORK_PATH}/del-elkOverDateIndex-running.log
## link: https://blog.csdn.net/weixin_38920212/article/details/98184115

PARAMETER=$1
ES_URL="your-es-url" #es对外http连接地址
ES_USER="your-name"  #es用户名
ES_PASSWORD="your-password" #es用户密码
APP_INDEX_START=app-log-

before7day=$(date -d '-7days' +'%Y%m%d')
before15day=$(date -d '-15days' +'%Y%m%d')
before30day=$(date -d '-30days' +'%Y%m%d')

usage() {
    echo -e "\033[46;31mUsage\033[0m: Please use \e[0;35m$0 del \e[0m"
    exit 1;
}

#1 app-log日志  获取app-log-索引开头的 日志后缀 如：2020.06.07
app_log_datetime=$(curl -s -u $ES_USER:$ES_PASSWORD $ES_URL/_cat/indices | awk '{print $3}' | grep $APP_INDEX_START | sort -n | sed -r 's/app-log-(.*)/\1/')
close_delete_app_log_index(){
    echo "start match app_log_datetime"
    for i in `echo $app_log_datetime`;do
      # 转日期 2020.06.07
      indexOriDate=$(echo $i | head -n 1)
      # 转成 20200607
      indexFormatDate=$(echo $i | head -n 1 | sed 's/\.//g')
      # 组成索引名称 app-log-2020.06.07
      apm_overDateIndex="$APP_INDEX_START$indexOriDate"
      ## 15以前的数据
      if [[ $before15day -ge $indexFormatDate ]]; then
        echo -e "\n$(date '+%Y-%m-%d %H:%M:%S')   es_index match successful,当前索引：\e[0;35m$apm_overDateIndex\e[0m"
        curl -XPOST -u $ES_USER:$ES_PASSWORD "$ES_URL/$apm_overDateIndex/_close"
        echo -e "$(date '+%Y-%m-%d %H:%M:%S')   索引\e[0;35m$apm_overDateIndex\e[0m, 关闭完成"
        if [[ ` echo $? ` == 0 ]];then
          curl -XDELETE -u $ES_USER:$ES_PASSWORD "$ES_URL/$apm_overDateIndex"
          echo -e "$(date '+%Y-%m-%d %H:%M:%S')   索引\e[0;35m$apm_overDateIndex\e[0m, 删除完成"
        else
          echo -e "$(date '+%Y-%m-%d %H:%M:%S')   索引\e[0;35m$apm_overDateIndex\e[0m, 关闭失败，无法进行删除"
        fi
      else
        echo -e "\n$(date '+%Y-%m-%d %H:%M:%S')   es_index match fail,当前索引：\e[0;35m$apm_overDateIndex\e[0m"
        echo -e "$(date '+%Y-%m-%d %H:%M:%S')   索引\e[0;35m$apm_overDateIndex\e[0m, 没有过期,不需要关闭，over.."
      fi
    done
}

if [ -n "$PARAMETER" ]; then
   case "$PARAMETER" in
    del)
		echo -e "开始删除 APP LOG 历史索引"
		close_delete_app_log_index

		echo -e "-----------------------------------------------删除完毕!-----------------------------------------------"
        ;;
    *)
        usage
        exit
        ;;
    esac
else
    echo -e "\033[46;31merror\033[0m: please input parameter"
fi
