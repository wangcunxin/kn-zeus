package com.taobao.zeus.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by hc on 2017/7/31.
 */
public class MyAuthenricator extends Authenticator {
    String username = null;
    String password = null;

    public MyAuthenricator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
