package com.chao.domian;

public class SendMessage {
    private String send_user;//发信者
    private String receive_user;//接收者
    private String msg_text;//消息内容

    public String getSend_user() {
        return send_user;
    }

    public void setSend_user(String send_user) {
        this.send_user = send_user;
    }

    public String getReceive_user() {
        return receive_user;
    }

    public void setReceive_user(String receive_user) {
        this.receive_user = receive_user;
    }

    public String getMsg_text() {
        return msg_text;
    }

    public void setMsg_text(String msg_text) {
        this.msg_text = msg_text;
    }
}
