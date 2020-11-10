package com.example.task;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class JDBC {

    public static Connection JDBC_connection() throws ClassNotFoundException, SQLException {
        String TAG = "gll";
        Class.forName("com.mysql.jdbc.Driver"); //加载驱动
        Log.d(TAG, "加载成功！");
//        String url = "jdbc:mysql://10.0.2.2:3306/数据库名字";
//        String user = "root";
//        String passwd = "123456";
        //定义一些启动JDBC需要的参数，分别是ip地址，端口号，用户名和密码，以及创建与数据库连接时要用到的url

        String ip = "10.0.2.2";
        int port = 3306;
        String user = "root";
        String passwd = "123456";
        String dbname = "task";
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbname + "?useUnicode=true&characterEncoding=UTF-8";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, passwd);
            Log.d(TAG, "数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
