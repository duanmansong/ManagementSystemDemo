package com.zhiyuan3g.managementsystemdemo.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.zhiyuan3g.managementsystemdemo.R;
import com.zhiyuan3g.managementsystemdemo.db.MySqlHelper;
import com.zhiyuan3g.managementsystemdemo.utils.ActivityCollector;
import com.zhiyuan3g.managementsystemdemo.utils.Contents;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    //region 声明控件对象
    private AutoCompleteTextView userName;
    private EditText userPassword;
    private Button btn_login;
    private Button btn_register;
    private Button btn_exit;
    private CheckBox checkBox;
    //endregion
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private MySqlHelper mySqlHelper;
    private SQLiteDatabase db;
    private List<String> lists = new ArrayList<>();
    private boolean isExist=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(this);//将该窗体加到窗体集合中
        init();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemenber = pref.getBoolean("remember_password", false);
        if (isRemenber) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            userName.setText(account);
            userPassword.setText(password);
            checkBox.setChecked(true);
        }
        mySqlHelper = new MySqlHelper(LoginActivity.this, Contents.DBNAME, null, Contents.VERSION);
        db = mySqlHelper.getWritableDatabase();
        Cursor cursorstu = db.rawQuery("select * from loginHistory", null);//查询loginHistory表
        while (cursorstu.moveToNext()) {//遍历所有的邮标
            lists.add(cursorstu.getString(1));//将登陆过的用户名存入到lists集合中
        }
        userName.setThreshold(1);
        ArrayAdapter adapter = new ArrayAdapter(LoginActivity.this, android.R.layout.simple_list_item_1, lists);
        userName.setAdapter(adapter);//将集合中数据绑定到输入框中

        //region 为注册按钮添加监听
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog();
            }
        });
        //endregion

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //region 实例化控件
    private void init() {
        userName = (AutoCompleteTextView) findViewById(R.id.userName);
        userPassword = (EditText) findViewById(R.id.userPassword);
        btn_login = (Button) findViewById(R.id.login);
        btn_exit = (Button) findViewById(R.id.exit);
        btn_register = (Button) findViewById(R.id.register);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
    }

    //endregion
    //region 注册
    public void showMyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);//声明一个AlertDialog对话框
        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);//声明一个布局压缩器对象
        View view = inflater.inflate(R.layout.registerlayout, null);//加载一个注册布局
        //声明布局中的控件
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_back = (Button) view.findViewById(R.id.btn_back);
        final EditText editUserName = (EditText) view.findViewById(R.id.editText1);
        final EditText editPswd = (EditText) view.findViewById(R.id.editText2);
        final EditText editPswd_confirm = (EditText) view.findViewById(R.id.editText3);
        final AlertDialog dialog = builder.setTitle("注册信息").setView(view).create();//创建对话框
        //为注册确定按钮添加监听
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExist=false;
                if (!editUserName.getText().toString().equals("")&&!editPswd.getText().toString().equals("")&&!editPswd_confirm.getText().toString().equals("")){
                    for (int i=0;i<lists.size();i++){
                        if (editUserName.getText().toString().equals(lists.get(i))){
                            isExist=true;
                        }
                    }
                    if (!isExist){
                        if ((editPswd.getText().toString()).equals(editPswd_confirm.getText().toString())) {
                            Cursor cursor = db.rawQuery("select count(*) from user where username = '" + editUserName.getText().toString() + "'", null);
                            cursor.moveToNext();
                            int count = cursor.getInt(0);
                            if (count == 0) {
                                ContentValues values = new ContentValues();
                                values.put("username", editUserName.getText().toString());
                                values.put("password", MD5(editPswd.getText().toString()));
                                db.insert("user", null, values);//将注册成功的用户名和密码存入数据库中的用户表中
                                dialog.dismiss();//取消对话框
                                Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "您的用户名已存在", Toast.LENGTH_SHORT).show();
                            }
                            cursor.close();//关闭油标
                        } else {
                            Toast.makeText(LoginActivity.this, "您两次输入的密码不相同", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
        //为取消按钮添加监听
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//取消对话框
            }
        });
    }
    //endregion

    //登录
    public void login() {
        Cursor cursor1 = db.rawQuery("select count(*) from user where username = '" + userName.getText().toString() + "'", null);
        cursor1.moveToNext();
        int count = cursor1.getInt(0);
        if (count == 1) {
            Cursor cursor = db.rawQuery("select username,password from user where username = '" + userName.getText().toString() + "'", null);
            cursor.moveToNext();
            if ((userName.getText().toString()).equals(cursor.getString(0).toString()) && (MD5(userPassword.getText().toString())).equals(cursor.getString(1).toString())) {
                //将用户名和密码存入SharedPreferences中
                editor = pref.edit();
                if (checkBox.isChecked()) {
                    editor.putBoolean("remember_password", true);
                    editor.putString("account", userName.getText().toString());
                    editor.putString("password", userPassword.getText().toString());
                } else {
                    editor.clear();
                }
                editor.commit();
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userName", cursor.getString(0).toString());
                startActivity(intent);

                Cursor cursor2 = db.rawQuery("select count(*) from loginHistory where name = '"
                        + userName.getText().toString() + "'", null);
                cursor2.moveToNext();
                int count2 = cursor2.getInt(0);
                if (count2 == 0) {
                    ContentValues values = new ContentValues();
                    values.put("name", userName.getText().toString());
                    db.insert("loginHistory", null, values);
                }
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "您输入的用户名或密码错误！", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(LoginActivity.this, "您输入的用户名或密码错误！", Toast.LENGTH_SHORT).show();
        }
        cursor1.close();
    }

    //endregion
    //MD5加密
    public static String MD5(String string) {
        return encodeMD5String(string);
    }

    public static String encodeMD5String(String str) {
        return encode(str, "MD5");
    }

    private static String encode(String str, String method) {
        MessageDigest md = null;
        String dstr = null;
        try {
            md = MessageDigest.getInstance(method);
            md.update(str.getBytes());
            dstr = new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dstr;
    }
    //endregion
}
