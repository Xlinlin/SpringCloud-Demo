<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="/js/jquery-1.11.0.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/paho-mqtt/1.0.1/mqttws31.min.js"
	type="text/javascript"></script>
<script type="text/javascript">
	var imclient;
	var topic = 'PTP/test';
	
	//  连接的是ws 协议的 端口。非mqtt端口，默认端口使用61614
	// var hostname = '192.168.206.210';
	var port = 6164;

	var conTimes = 0;

	//失去连接后 2S 再次连接
	var reconnectTimeout = 2000;

	//mqtt ------ start ------
	//mqtt
	function initMQTT() {
		var clientId = "gourpId@@@ClientID_SYSTEM" + Date.parse(new Date());
		var username = 'admin';
		var password = 'admin123';

		imclient = new Paho.MQTT.Client(hostname, port, clientId);
		options = {
			timeout : 3,
			// useSSL: useTLS,  
			cleanSession : true,
			onSuccess : onConnect,
			onFailure : function(message) {
				setTimeout(initMQTT, reconnectTimeout);
			}
		};

		imclient.onConnectionLost = onConnectionLost;
		imclient.onMessageArrived = onMessageArrived;

		if (username != null) {
			options.userName = username;
			options.password = password;
		}
		imclient.connect(options);
	}

	//连接监听
	function onConnect() {
		// Connection succeeded; subscribe to our topic  
		imclient.subscribe(topic, {
			qos : 0
		});
		//$('#topic').val(topic);  
		console.log('Connection success!');
	}

	// 失去连接
	function onConnectionLost(response) {
		//$('#status').val("connection lost: " + response.errorMessage + ". Reconnecting");  

		if (response.errorCode !== 0) {
			console.log("onConnectionLost:" + response.errorMessage);
		}

		//setTimeout(initMQTT, reconnectTimeout);
	};

	//消息到达
	function onMessageArrived(message) {
		//var topic = message.destinationName;  
		var payload = message.payloadString;
		console.log(payload);
		var srcMsg = $('#resiveMsg').html();
		if(srcMsg){
			srcMsg = srcMsg + ';<br>' + payload
		}else{
			srcMsg = payload;
		}
		$('#resiveMsg').html(srcMsg);
	};

	function sendMessage() {
		var message = $('#message').val();
		var msg = new Paho.MQTT.Message(message);
		msg.destinationName = topic;
		imclient.send(msg);
	}

	$(document).ready(function() {
		initMQTT();
	});
	// mqtt ----- end -------
</script>
</head>
<body>
	<div>
		<div>
			输入发送消息：<input id="message">
		</div>
		<div>
			<input type="button" value="发送" onclick="sendMessage()">
		</div>
	</div>
	<div>
		<div id="resiveMsg"></div>
	</div>
</body>
</html>