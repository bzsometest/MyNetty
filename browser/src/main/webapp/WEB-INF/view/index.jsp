<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>首页[spring-websocket demo]</title>
    <style type="text/css">
        body {
            background: #E2E2E2;
            font-size: 13.5px;
        }

        #message_div {
            background: #5170ad;
            width: 400px;
            min-height: 500px;
            border: 1px solid #ccc;
            color: #fff;
            overflow-y: auto;
            float: left;
        }

        ul, li {
            list-style-type: square;
        }

        #send_div {
            float: left;
        }

        table tr td {
            font-size: 12px;
        }

        input, textarea {
            font-size: 12px;
        }
    </style>
    <script type="text/javascript" src="${pageContext.request.contextPath }/static/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/sockjs.min.js"></script>
    <script type="text/javascript">
        var PATH = "localhost:8080/server";
        var TOKEN = "123456";
        var message = {send_user: "system", receive_user: "", msg_text: ""};

        var websocket = null;

        function openSocket() {
            if (window['WebSocket']) {
                // ws://host:port/project/websocketpath
                websocket = new WebSocket("ws://" + PATH + '/websocket?token=' + TOKEN);
            } else {
                console.log("new SockJS");
                websocket = new new SockJS(PATH + '/websocket/socketjs');
            }
            websocket.onopen = function (event) {
                console.log('open', event);
            };
            websocket.onmessage = function (event) {
                console.log("onmessage");
                console.log(event.data);
                try {
                    var jsObj = JSON.parse(event.data);
                    var msg2 = jsObj.send_user + ":" + jsObj.msg_text;
                    $(' #message_div > ul').append('<li>' + msg2 + '</li>');
                } catch (e) {
                    $(' #message_div > ul').append('<li>' + event.data + '</li>');
                }
            };


        }

        //发送消息
        function send() {
            message.receive_user = $("#username").val();
            message.msg_text = $("#msg_txt").val();
            console.log("send");
            console.log(message);
            websocket.send(JSON.stringify(message));
        }

        function set_token() {
            TOKEN = $("#token_text").val();
            openSocket();
        }

        $(function () {
            $("#token_text").val(TOKEN);
        })
    </script>

</head>
<body>
<div id="message_div">
    <ul></ul>
</div>
<div id="send_div">
    <table>
        <tr>
            <td align="right">用户:</td>
            <td><input id="username" type="text" style="width: 300px;"/></td>
        </tr>
        <tr>
            <td align="right">内容:</td>
            <td><textarea id="msg_txt" style="width: 300px;"></textarea></td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <button class="btn_send" onclick="send()">发送消息</button>
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td align="right">token</td>
            <td><input id="token_text" type="text" style="width: 300px;"/></td>
            <td colspan="2" align="center">
                <button class="btn_token" onclick="set_token()">设置</button>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
