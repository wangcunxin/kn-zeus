package com.taobao.zeus.web.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by hc on 2017/6/9.
 */
public class JdbcUtils {
    private static final String url = "jdbc:mysql://10.154.219.176:3306/zeus";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "beehive";
    private static final String password = "123";

    public static Connection conn = null;

    private static JdbcUtils single = null;

    // 静态工厂方法
    public static JdbcUtils getInstance() {
        if (single == null) {
            single = new JdbcUtils();
        }
        return single;
    }

    public Connection getConn() {
        if (conn == null) {
            try {
                Class.forName(driver); //classLoader,加载对应驱动
                conn = (Connection) DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
