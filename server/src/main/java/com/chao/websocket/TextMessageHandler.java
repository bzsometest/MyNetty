package com.chao.websocket;

import com.chao.domian.SendMessage;
import com.chao.domian.UserManager;
import com.google.gson.Gson;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TextMessageHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> users;

    static {
        users = new HashMap<String, WebSocketSession>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        /*
         * 链接成功后会触发此方法，可在此处对离线消息什么的进行处理
         */
        String username = getUsernameBySession(session);
        String token = getTokenBySession(session);
        users.put(username, session);

        System.out.println(username + ":" + token + " connect success ...");
        session.sendMessage(new TextMessage(username + " 链接成功!!"));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        /*
         * 前端 com.chao.websocket.send() 会触发此方法
         */
        System.out.println("handleTextMessage");
        message = getNewMessage(session, message);
        sendMessageToUser(getUsernameBySession(session), message);
    }

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        System.err.println(exception.getMessage());
        System.out.println("websocket connection closed......");
        String username = getUsernameBySession(session);
        users.remove(username);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("websocket connection closed......");
        String username = getUsernameBySession(session);
        users.remove(username);
    }

    public void sendMessageToUser(String username, TextMessage message) {
        WebSocketSession session = users.get(username);
        if (session != null) {
            try {
                if (session.isOpen())
                    session.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToUsers(TextMessage message) {
        Iterator<Map.Entry<String, WebSocketSession>> it = users.entrySet().iterator();
        while (it.hasNext()) {
            WebSocketSession session = it.next().getValue();
            try {
                if (session.isOpen())
                    session.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //通过uri获得token口令
    private static String getTokenForUri(String uri) {
        String tSting = "token=";
        int index = uri.indexOf(tSting) + tSting.length();
        int end = uri.indexOf("&", index);
        if (end > 0) {
            return uri.substring(index, end);
        } else {
            return uri.substring(index);
        }
    }

    private static String getUsernameBySession(WebSocketSession session) {
        String token = getTokenForUri(session.getUri().getQuery());
        String username = UserManager.getUser(token);
        return (username == null) ? "system" : username;
    }


    private static String getTokenBySession(WebSocketSession session) {
        return getTokenForUri(session.getUri().getQuery());
    }

    /**
     * 获得新的TextMessage，增加发送者信息
     *
     * @param session
     * @param textMessage
     * @return
     */
    private static TextMessage getNewMessage(WebSocketSession session, TextMessage textMessage) {
        String jsonString = textMessage.getPayload();
        SendMessage myMessage;
        try {
            myMessage = new Gson().fromJson(jsonString, SendMessage.class);
            String username = getUsernameBySession(session);
            myMessage.setSend_user(username);
            return new TextMessage(new Gson().toJson(myMessage));
        } catch (Exception e) {
            System.out.println(jsonString);
            e.printStackTrace();
        }
        return textMessage;
    }
}
