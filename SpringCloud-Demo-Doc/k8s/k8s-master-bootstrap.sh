#!/bin/bash

CMD=$1

showHelp(){
	echo "start  ---- Start k8s cluster"
	echo "stop   ---- Stop k8s cluster"
    echo "status ---- Show k8s cluster status"
	exit 1
}
 
executeCommand(){
	echo "---------- $CMD etcd --------"
	systemctl $CMD etcd
	echo "---------- $CMD: docker --------"
	systemctl $CMD docker
	echo "---------- $CMD: kube-apiserver --------"
	systemctl $CMD kube-apiserver
	echo "---------- $CMD: kube-controller-manager --------"
	systemctl $CMD kube-controller-manager
	echo "---------- $CMD: kube-scheduler --------"
	systemctl $CMD kube-scheduler
	echo "---------- $CMD: kubelet--------"
	systemctl $CMD kubelet
	echo "---------- $CMD: kube-proxy--------"
	systemctl $CMD kube-proxy
	exit 1
}
 
showGift(){
	echo " ___                     ___              
     /  /\      ___          /  /\             
    /  /:/_    /  /\        /  /:/_            
   /  /:/ /\  /  /:/       /  /:/ /\           
  /  /:/ /:/ /__/::\      /  /:/ /::\          
 /__/:/ /:/  \__\/\:\__  /__/:/ /:/\:\         
 \  \:\/:/      \  \:\/\ \  \:\/:/~/:/         
  \  \::/        \__\::/  \  \::/ /:/          
   \  \:\        /__/:/    \__\/ /:/           
    \  \:\       \__\/       /__/:/            
     \__\/                   \__\/  "
}

checkParams(){
	if [ -z $CMD ]
	then
		echo "Exe command not null!"
		showHelp
		exit 1
	fi
	if [ "$CMD" != "start" ] && [ "$CMD" != "stop" ] && [ "$CMD" != "status" ]
	then
		echo "Not support command!"
		showHelp
		exit 1
	fi
}

checkParams
showGift
executeCommand
