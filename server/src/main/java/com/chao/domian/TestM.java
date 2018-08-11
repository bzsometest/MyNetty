package com.chao.domian;

import com.google.gson.Gson;

public class TestM {
    public static void main(String[] args) {
        String jsStr="{send_user: \"system\", receive_user: \"admin\", msg_text: \"14231\"}";
        Gson gson = new Gson();
        SendMessage myMessage = gson.fromJson(jsStr, SendMessage.class);
        System.out.println(myMessage.getReceive_user());
    }
}
