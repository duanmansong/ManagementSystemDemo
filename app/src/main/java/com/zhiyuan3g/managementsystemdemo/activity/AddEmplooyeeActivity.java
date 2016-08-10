package com.zhiyuan3g.managementsystemdemo.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhiyuan3g.managementsystemdemo.R;
import com.zhiyuan3g.managementsystemdemo.db.MySqlHelper;
import com.zhiyuan3g.managementsystemdemo.utils.ActivityCollector;
import com.zhiyuan3g.managementsystemdemo.utils.Contents;
import com.zhiyuan3g.managementsystemdemo.view.CircleImg;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class AddEmplooyeeActivity extends AppCompatActivity {
    //声明控件对象
    private Spinner mzSpinner;//民族下拉列表
    private Spinner departmentSp;//部门下拉列表
    private EditText addName, workId, addBirthday, telephone, addMore;
    private RadioGroup emp_rg;
    private RadioButton emp_rb1, emp_rb2;
    private Button emp_bir_choose, add_submit, add_cancel;
//    private com.zhiyuan3g.managementsystemdemo.view.CircleImg headImage;
    private ImageView headImage;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;//日历控件

    private String arrs[];
    private String departmentList[];
    private String sex = "男";
    private String addMingZu;//民族名称
    private String departmentName;//部门名称
    private MySqlHelper mySqlHelper;
    private SQLiteDatabase db;
    private int headImageId = R.drawable.image;
    private final int CODE = 1;
    private String user;

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            addBirthday.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emplooyee);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        mySqlHelper = new MySqlHelper(AddEmplooyeeActivity.this, Contents.DBNAME, null, Contents.VERSION);
        db = mySqlHelper.getWritableDatabase();
        headImage = (ImageView) findViewById(R.id.headImage);
//        headImage = (CircleImg) findViewById(R.id.headImage);
        //为图片添加监听事件
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        //实例化控件对象
        addName = (EditText) findViewById(R.id.add_name);
        workId = (EditText) findViewById(R.id.workId);
        telephone = (EditText) findViewById(R.id.telephone);
        addMore = (EditText) findViewById(R.id.add_more);

        mzSpinner = (Spinner) findViewById(R.id.spinner_mz);
        departmentSp = (Spinner) findViewById(R.id.spinner_department);
        arrs = getResources().getStringArray(R.array.listArr);
        departmentList = getResources().getStringArray(R.array.listDep);
        ArrayAdapter adapter = new ArrayAdapter(AddEmplooyeeActivity.this, android.R.layout.simple_list_item_1, arrs);
        ArrayAdapter depAdapter = new ArrayAdapter(AddEmplooyeeActivity.this, android.R.layout.simple_list_item_1, departmentList);
        mzSpinner.setAdapter(adapter);
        departmentSp.setAdapter(depAdapter);

        mzSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addMingZu = arrs[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        departmentSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentName = departmentList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //实例化单选按钮
        emp_rg = (RadioGroup) findViewById(R.id.emp_rg);
        emp_rb1 = (RadioButton) findViewById(R.id.emp_rb1);
        emp_rb2 = (RadioButton) findViewById(R.id.emp_rb2);
        emp_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == emp_rb1.getId()) {//如果id是emp_rb1的id sex的值设为男，否则为女
                    sex = "男";
                } else {
                    sex = "女";
                }
            }
        });

        addBirthday = (EditText) findViewById(R.id.add_birthday);
        calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(AddEmplooyeeActivity.this, listener, year, month, day);
        emp_bir_choose = (Button) findViewById(R.id.emp_bir_choose);

        add_submit = (Button) findViewById(R.id.add_submit);
        add_cancel = (Button) findViewById(R.id.add_cancel);
        emp_bir_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        add_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将员工信息添加到数据库
                addEmployyeInfo();
            }
        });
        add_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEmplooyeeActivity.this, MainActivity.class);
                intent.putExtra("userName",user);
                startActivity(intent);
                finish();
            }
        });
    }

    //将填写好的信息存储
    public void addEmployyeInfo() {
        if (addName.getText().toString().equals("") || workId.getText().toString().equals("") || addBirthday.getText().toString().equals("") || telephone.getText().toString().equals("")) {
            Toast.makeText(AddEmplooyeeActivity.this, "您输入的信息不完整！", Toast.LENGTH_SHORT).show();
        } else {
            Bitmap bitmap = ((BitmapDrawable) headImage.getDrawable()).getBitmap();
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOut);

            ContentValues values = new ContentValues();
            values.put("name", addName.getText().toString());
            values.put("sex", sex);
            values.put("mingZu", addMingZu);
            values.put("id", workId.getText().toString());
            values.put("department", departmentName);
            values.put("birthday", addBirthday.getText().toString());
            values.put("phone", telephone.getText().toString());
            values.put("user",user);
            values.put("more", addMore.getText().toString());
            values.put("image", byteOut.toByteArray());
            db.insert("employee", null, values);
            Toast.makeText(AddEmplooyeeActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddEmplooyeeActivity.this, MainActivity.class);
            intent.putExtra("userName",user);
            startActivity(intent);
            finish();
        }
    }

    //从相册里选择图像
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CODE){
//            if (resultCode == Activity.RESULT_OK && data !=null){
//                Uri selectedImagePath = data.getData();
//                Cursor cursor = getContentResolver().query(selectedImagePath,null,null,null,null);
//                if (cursor == null){
//                    Toast.makeText(AddEmplooyeeActivity.this,"cursor为空",Toast.LENGTH_SHORT).show();
//                }else {
//                    cursor.moveToFirst();
//                    String img = cursor.getString(1);
//                    cursor.close();
////                    int width = headImage.getWidth();
////                    int height = headImage.getHeight();
////                    BitmapFactory.Options options = new BitmapFactory.Options();
//////                        options.inJustDecodeBounds = true;
//////                        BitmapFactory.decodeFile(img,options);
////
////                    int imageWidth = options.outWidth;
////                    int imageHeight = options.outHeight;
////                    int scaleFactor = Math.min(imageWidth/width,imageHeight/height);
////                    options.inJustDecodeBounds = false;
////                    options.inSampleSize = scaleFactor;
////                    options.inPurgeable = true;
////                    Bitmap bitmap = BitmapFactory.decodeFile(img,options);
////                    headImage.setImageBitmap(bitmap);
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 3;
//                    Bitmap bitmap = BitmapFactory.decodeFile(img,options);
//                    headImage.setImageBitmap(bitmap);
//                }
//
//            }
//        }
        switch (requestCode) {
            //从图库里得到图片
            case CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri selectedImagePath = data.getData();
//                    path = getPath(context,selectedImagePath);
                    Cursor cursor = getContentResolver().query(selectedImagePath, null, null, null, null);
                    if (cursor == null) {
                        Toast.makeText(AddEmplooyeeActivity.this, "cursor为空", Toast.LENGTH_SHORT).show();
                    } else {
                        cursor.moveToFirst();
                        String img = cursor.getString(1);
                        cursor.close();

//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inSampleSize = 3;
//                        Bitmap bitmap = BitmapFactory.decodeFile(img, options);
//                        imageShow.setImageBitmap(bitmap);
                        int width = headImage.getWidth();
                        int height = headImage.getHeight();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(img,options);

                        int imageWidth = options.outWidth;
                        int imageHeight = options.outHeight;
                        int scaleFactor = Math.min(imageWidth / width, imageHeight / height);
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = scaleFactor;
                        options.inPurgeable = true;
                        Bitmap bitmap = BitmapFactory.decodeFile(img, options);
                        headImage.setImageBitmap(bitmap);
                    }

                }

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==event.KEYCODE_BACK){
            Intent intent = new Intent(AddEmplooyeeActivity.this,MainActivity.class);
            intent.putExtra("userName",user);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
