package com.example.task;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    Button buttonLogin, buttonRegister, buttonCancel, buttonUpdate;
    EditText editLogin, editPw;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("关于本软件")
                        .setMessage("本软件仅实现Android连接MySQL数据库，只具有基本的增删改查功能！")
                        .show();
                break;
            case R.id.back:
                finish();
                break;
        }
        return true;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = findViewById(R.id.btn_login);
        buttonRegister = findViewById(R.id.btn_register);
        buttonCancel = findViewById(R.id.bt_cancel);
        buttonUpdate = findViewById(R.id.btn_update);
        editLogin = findViewById(R.id.et_username);
        editPw = findViewById(R.id.et_password);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        // 查询
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String useLogin = editLogin.getText().toString().trim();
                final String usePw = editPw.getText().toString().trim();

                final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
                loadingDialog.setLoadingText("登录中...").show();
                new Thread() {
                    @Override
                    public void run() {
//                       super.run();
                        Connection conn = null;
                        Statement stmt = null;
                        ResultSet rs = null;
                        try {
                            Looper.prepare();
                            conn = JDBC.JDBC_connection();
                            stmt = conn.createStatement();
                            String sql_select = "select login,pas from info where login='" + useLogin + "'and pas='" + usePw + "'LIMIT 1";
                            rs = stmt.executeQuery(sql_select);

                            if (rs.next()) {

//                                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, WeiBo.class);
                                startActivity(intent);
                                loadingDialog.close();
                            } else {
                                Toast.makeText(MainActivity.this, "账号不存在或密码错误", Toast.LENGTH_SHORT).show();
                                loadingDialog.close();
                            }
                            Looper.loop();

                        } catch (SQLException e) {
                            e.getMessage();
                        } catch (ClassNotFoundException e) {
                            e.getMessage();
                        }
                    }
                }.start();

            }
        });

        // 删除
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final EditText editText = new EditText(MainActivity.this);
                editText.setHint("请输入要注销的账户");
                editText.setSingleLine();
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Spanned msg = Html.fromHtml("<font color = red>内容不能为空</font>");
                    editText.setError(msg);
                }

                builder.setTitle("注销账户")
                        .setView(editText)
                        .setCancelable(false) //设置点击弹出框外不会消失
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String deleLogin = editText.getText().toString().trim();

                                new Thread() {
                                    @Override
                                    public void run() {
//                                        super.run();
                                        Connection conn = null;
                                        int u = 0;
                                        PreparedStatement pst = null;
                                        String sql_delete = "delete from info where login='" + deleLogin + "'";

                                        try {
                                            Looper.prepare();
                                            conn = JDBC.JDBC_connection();
                                            pst = conn.prepareStatement(sql_delete);
                                            u = pst.executeUpdate();
                                            Toast.makeText(MainActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                                            pst.close();
                                            conn.close();
                                            Looper.loop();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        //修改
        buttonUpdate.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View DialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.update, null, false);
                final  EditText Login = DialogView.findViewById(R.id.editTextName);
                final  EditText Pas = DialogView.findViewById(R.id.editTextNum);
                builder.setTitle("修改密码")
                        .setView(DialogView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        Connection conn = null;
                                        PreparedStatement pstm = null;
//                                        super.run();
                                        try {
                                            conn = JDBC.JDBC_connection();
                                            Log.d("更新", "驱动数据库加载成功");
//                                            String sql_update = "update info set pas='" + "457" + "' where login='" + "123" + "'";
                                            String sql_update = "update info set pas='" + Pas.getText().toString() + "' where login='" + Login.getText().toString() + "'";
                                            Log.d("更新", sql_update);
                                            pstm = conn.prepareStatement(sql_update);

                                            pstm.executeUpdate();
                                            conn.close();
                                            pstm.close();
                                            Looper.loop();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

}