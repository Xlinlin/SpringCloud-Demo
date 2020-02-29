#!/bin/bash

CMD=$1

showHelp(){
	echo "start  ---- Start k8s cluster node"
	echo "stop   ---- Stop k8s cluster node"
    echo "status ---- Show k8s cluster node status"
	exit 1
}
 
executeCommand(){
	echo "---------- $CMD: docker --------"
	systemctl $CMD docker
	echo "---------- $CMD: flanneld --------"
	systemctl $CMD flanneld
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
