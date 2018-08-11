package com.chao.domian;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static Map<String, String> users = new HashMap();

    static {
        users.put("chao", "123456");
        users.put("admin", "admin123");
    }

    public static void add(String username, String token) {
        users.put(username, token);
    }

    public static String getToken(String username) {
        return users.get(username);
    }

    public static String getUser(String token) {
        for (String key : users.keySet()) {
            if (users.get(key).equals(token)) {
                return key;
            }
        }
        return null;
    }


}

class User {
    String username;
    String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
