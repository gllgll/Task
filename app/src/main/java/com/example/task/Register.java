package com.example.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


// 增
public class Register extends AppCompatActivity {
    EditText editRe, editPw, editPw1; //注册界面输入框
    Button btRe; //注册界面登录
    private String useName, usePassword, usePassword1; //向数据库写入的账号和密码
    String TAG = "GLL_GLL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        editRe = findViewById(R.id.editTextTextPersonName);
        editPw = findViewById(R.id.editTextTextPassword);
        editPw1 = findViewById(R.id.editTextTextPersonName2);
        btRe = findViewById(R.id.button);

        btRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入框的注册信息
                useName = editRe.getText().toString().trim();
                usePassword = editPw.getText().toString().trim();
                usePassword1 = editPw1.getText().toString().trim();

                //过滤判断
                if (useName.equals("")) {
                    Toast.makeText(Register.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (usePassword.equals("")) {
                    Toast.makeText(Register.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!usePassword.equals(usePassword1)) {
                    Toast.makeText(Register.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                } else {
                    //添加依赖，网址  https://github.com/ForgetAll/LoadingDialog
                    final LoadingDialog loadingDialog = new LoadingDialog(Register.this);
                    loadingDialog.setLoadingText("登录中...")
                            .show();
                    new Thread() {
                        @Override
                        public void run() {
//                            super.run();
                            Connection conn = null;
                            Statement stmt = null;
                            ResultSet rs = null;
                            try {
                                Looper.prepare();//使子进程可以显示toast
                                conn = JDBC.JDBC_connection(); // 调用JDBC连接数据库

                                stmt = conn.createStatement();
                                String sql_select = "select * from info where login='" + useName + "'";
//                                Log.d(TAG,"测试通过没1");

                                rs = stmt.executeQuery(sql_select);
                                if (rs.next()) {
                                    Toast.makeText(Register.this, "用户已存在！", Toast.LENGTH_SHORT).show();
                                    loadingDialog.close();
                                } else {
                                    String sql = "INSERT INTO info VALUES('" + useName + "','" + usePassword + "')";
                                    stmt.executeUpdate(sql);
                                    Toast.makeText(Register.this, "新建用户成功！", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                Looper.loop();
                            } catch (SQLException e) {
                                e.getMessage();
                            } catch (ClassNotFoundException e) {
                                e.getMessage();
                            }
                            //关闭连接
                            finally {
                                if (rs != null) {
                                    try {
                                        rs.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (stmt != null) {
                                    try {
                                        stmt.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (conn != null) {
                                    try {
                                        conn.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }.start();
                }
            }
        });
    }
}