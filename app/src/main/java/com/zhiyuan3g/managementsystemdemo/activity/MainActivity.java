package com.zhiyuan3g.managementsystemdemo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhiyuan3g.managementsystemdemo.DBDao.EmployeeDao;
import com.zhiyuan3g.managementsystemdemo.R;
import com.zhiyuan3g.managementsystemdemo.adapter.EmployeeAdapter;
import com.zhiyuan3g.managementsystemdemo.db.MySqlHelper;
import com.zhiyuan3g.managementsystemdemo.entity.Employee;
import com.zhiyuan3g.managementsystemdemo.utils.ActivityCollector;
import com.zhiyuan3g.managementsystemdemo.utils.Contents;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int op_imageid[] = {R.drawable.add,R.drawable.delete,R.drawable.mituo_icon_fresh,R.drawable.user,R.drawable.comment};
    private String str[] = {"新增", "删除", "刷新", "管理用户", "短信群发"};
    private ListView listView;
    private View visView;
    private boolean flag = true;
    private MySqlHelper mySqlHelper;
    private SQLiteDatabase db;
    private List<Employee> employees ;
    private ListView emp_listView;
    private CheckBox checkboxSum;//全选checkbook按钮
    private boolean checkAll = false;//是否全选
    private ArrayList<CheckBox> cbs = new ArrayList<>();
    private String updateName;//存储信息修改后的员工名
    private AutoCompleteTextView search_edit;
    private List<String> lists = new ArrayList<>();
    private EmployeeDao employeeDao;
//    private ActionBar actionBar;//标题栏
    private JSONArray jsonArray;
    private ImageView headImage;//头像
    EmployeeAdapter adapter ;

    private final int CAMA = 1;
    private String[] departmentMassage;
    private String seadepartmenent;
    private HashMap<Integer,Boolean> isSelect;

    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        if (intent!=null&&!intent.equals("")){
            userName = intent.getStringExtra("userName");
        }

//        Toast.makeText(MainActivity.this,userName,Toast.LENGTH_SHORT).show();
        emp_listView = (ListView) findViewById(R.id.emp_listView);
        employeeDao = new EmployeeDao(MainActivity.this);

//        Cursor cursor = db.rawQuery("select * from employee",null);
//        if (cursor!= null){
//            while (cursor.moveToNext()){
//                employees.add(new Employee(cursor.getString(1),cursor.getString(2)
//                        ,cursor.getString(3),cursor.getString(4),cursor.getString(5)
//                        ,cursor.getString(6),cursor.getString(7),cursor.getString(8)
//                ));
//            }
//            adapter = new EmployeeAdapter(MainActivity.this,employees);
//        }
        reflashList();
        mySqlHelper = new MySqlHelper(MainActivity.this, Contents.DBNAME,null,Contents.VERSION);
        db = mySqlHelper.getWritableDatabase();

        checkboxSum = (CheckBox) findViewById(R.id.checkboxSum);
        checkboxSum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkAll = isChecked;
//                for (int i = 0;i<employees.size();i++){
//                    isSelect = new HashMap<Integer, Boolean>();
//                    isSelect.put(i, isChecked);
//                    EmployeeAdapter.setIsSelected(isSelect);
////                    EmployeeAdapter.getIsSelected().put(i,isChecked);
//                }

                adapter.notifyDataSetChanged();
                reflashList();
            }
        });

        emp_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showInfomation(position);
            }
        });

    }

    //刷新列表
    public void reflashList(){
        employees = new ArrayList<>();
        employees = employeeDao.getAllEmployeeByUserName(userName);
        if (employees.size()>=0){
            adapter = new EmployeeAdapter(MainActivity.this,employees,checkAll);
        }
        emp_listView.setAdapter(adapter);
    }
    public void showInfomation(final int index){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.employee_info_item, null);
        final AlertDialog dialog = builder.setTitle("员工详细信息").setView(view).create();
        //声明要加载到对话框里的布局里的控件对象
        Button notify = (Button) view.findViewById(R.id.notify);
        Button btnOk = (Button) view.findViewById(R.id.btnOk);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        final EditText infoName = (EditText) view.findViewById(R.id.info_name);
        final EditText infoSex = (EditText) view.findViewById(R.id.info_tex);
        final EditText infoMingZu = (EditText) view.findViewById(R.id.info_mingZu);
        final EditText infoDepartment = (EditText) view.findViewById(R.id.info_department);
        final EditText eId = (EditText) view.findViewById(R.id.eId);
        final EditText eBirthday = (EditText) view.findViewById(R.id.eBirthday);
        final EditText phone = (EditText) view.findViewById(R.id.ePhone);
        final EditText more = (EditText) view.findViewById(R.id.eMore);
        headImage = (ImageView) view.findViewById(R.id.emp_handImage);
        ImageView callEmp = (ImageView) view.findViewById(R.id.callEmp);
        ImageView smsEmp = (ImageView) view.findViewById(R.id.smsEmp);
        //实例化要加载到对话框里的布局里的控件
        infoName.setText(employees.get(index).getName().toString());
        infoSex.setText(employees.get(index).getSex().toString());
        infoMingZu.setText(employees.get(index).getMingZu().toString());
        infoDepartment.setText(employees.get(index).getDepartment().toString());
        eId.setText(employees.get(index).getId().toString());
        eBirthday.setText(employees.get(index).getBirthday().toString());
        phone.setText(employees.get(index).getPhone().toString());
        more.setText(employees.get(index).getMore().toString());

        Cursor cursor = db.rawQuery("select * from employee where id = '"+ employees.get(index).getId().toString() + "'",null);
        cursor.moveToNext();
        //获取图片
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
        //出来获取的图片
        final Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
        //显示图片
        headImage.setImageBitmap(bitmap);
        dialog.show();
        updateName = employees.get(index).getName().toString();//将当前的员工姓名存入
        //为头像添加监听事件
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        //修改按钮
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将所有的EditText设置成可输入的状态
                infoName.setEnabled(true);
                infoSex.setEnabled(true);
                infoMingZu.setEnabled(true);
                infoDepartment.setEnabled(true);
                eId.setEnabled(true);
                eBirthday.setEnabled(true);
                phone.setEnabled(true);
                more.setEnabled(true);
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap1 = ((BitmapDrawable)headImage.getDrawable()).getBitmap();
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                //压缩bitmap到ByteArrayOutputStream
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteOut);
                ContentValues values = new ContentValues();
                values.put("name",infoName.getText().toString());
                values.put("sex",infoSex.getText().toString());
                values.put("mingZu",infoMingZu.getText().toString());
                values.put("id", eId.getText().toString());
                values.put("department", infoDepartment.getText().toString());
                values.put("birthday", eBirthday.getText().toString());
                values.put("phone", phone.getText().toString());
                values.put("more", more.getText().toString());
                values.put("image", byteOut.toByteArray());
                db.update("employee", values, "name=?", new String[]{updateName});
                reflashList();
                dialog.dismiss();
            }
        });
        //为取消按钮添加监听
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //给电话图片添加监听按钮
        callEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone.getText().toString()));
                startActivity(intent);
            }
        });

        //为信息图片添加监听事件
        smsEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+phone.getText().toString()));
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
//        employees = new ArrayList<>();
//        Cursor cursor = db.rawQuery("select * from employee",null);
//        while (cursor.moveToNext()){
//            employees.add(new Employee(cursor.getString(1),cursor.getString(2)
//                    ,cursor.getString(3),cursor.getString(4),cursor.getString(5)
//                    ,cursor.getString(6),cursor.getString(7),cursor.getString(8)
//                    ));
//        }
//        employees = employeeDao.getAllEmployee();
//        adapter.notifyDataSetChanged();//刷新列表
//        adapter = new EmployeeAdapter(MainActivity.this,employees);
        reflashList();
        super.onResume();
    }


    private void chooseImage(){
        //选择相册
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CAMA);
    }
    //点击相册里的图片后回调的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMA){
            if (resultCode == Activity.RESULT_OK && data !=null){
                Uri selectedImagePath = data.getData();
                Cursor cursor = getContentResolver().query(selectedImagePath,null,null,null,null);
                if (cursor == null){
                    Toast.makeText(MainActivity.this,"cursor为空",Toast.LENGTH_SHORT).show();
                }else {
                    cursor.moveToFirst();
                    String img = cursor.getString(1);
                    cursor.close();
                    int width = headImage.getWidth();
                    int height = headImage.getHeight();
                    BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inJustDecodeBounds = true;
//                        BitmapFactory.decodeFile(img,options);

                    int imageWidth = options.outWidth;
                    int imageHeight = options.outHeight;
                    int scaleFactor = Math.min(imageWidth/width,imageHeight/height);
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = scaleFactor;
                    options.inPurgeable = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(img,options);
                    headImage.setImageBitmap(bitmap);
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 3;
//                    Bitmap bitmap = BitmapFactory.decodeFile(img,options);
//                    headImage.setImageBitmap(bitmap);
                }

            }
        }
    }

    //添加员工
    public void addEmployee(){
        Intent intent = new Intent(MainActivity.this,AddEmplooyeeActivity.class);
        intent.putExtra("user",userName);
        startActivity(intent);
        finish();
    }

    //删除员工信息
    public void deleteEmployee(){
        employees = employeeDao.getAllEmployeeByUserName(userName);
        HashMap<Integer,Boolean> choose = EmployeeAdapter.getIsSelected();
        if (employees.size()>0){
            for (int i=0;i<choose.size();i++){
                if (choose.get(i)){
                    boolean result = employeeDao.deleteEmployee(employees.get(i).getId());
                    if (result){
//
                        adapter.notifyDataSetChanged();
//                        reflashList();
                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }else {
            Toast.makeText(MainActivity.this,"没有数据可以删除了",Toast.LENGTH_LONG).show();
        }

//        for (Employee employee:employees){
//            if (employee.isChecked()){
//                Cursor cursor = db.rawQuery("delete from employee where id = '"+employee.getId() + "'",null);
//                cursor.moveToNext();
//            }
//        }
        reflashList();
    }
    //按部门和姓名查询员工
    public void searchEmployee(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.search_employee_item, null);
//        final AlertDialog dialog = builder.setTitle("搜索").setIcon(R.drawable.search_press).setView(view).create();
        Spinner searchSpinner = (Spinner) view.findViewById(R.id.searchSpinner);
        final EditText searchName = (EditText) view.findViewById(R.id.searchName);
        departmentMassage =  getResources().getStringArray(R.array.listDep);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,departmentMassage);
        searchSpinner.setAdapter(arrayAdapter);


//        dialog.show();
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seadepartmenent = departmentMassage[position];

//                dialog.dismiss();
//                Toast.makeText(MainActivity.this, "选择的是" + departmentMassage[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new AlertDialog.Builder(this)
                .setTitle("按部门或人名搜索")
                .setIcon(R.drawable.search_press)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = searchName.getText().toString();
                        if (name != null&&!name.equals("")) {
                            employees = employeeDao.search(name,userName);
                        } else {
                            employees = employeeDao.getAllEmployee(seadepartmenent,userName);
                        }
                        adapter.notifyDataSetChanged();
                        if (employees.size() >= 0) {
                            adapter = new EmployeeAdapter(MainActivity.this, employees,checkAll);
                        }
                        emp_listView.setAdapter(adapter);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "选择的是取消", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //群发短信
    private void sma_all(){
        Cursor cursor = db.rawQuery("select phone from employee",null);
        String emp_sms = "";
        while (cursor.moveToNext()){
            emp_sms = emp_sms + cursor.getString(0)+",";
        }
        Intent intentsms = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+emp_sms));
        cursor.close();
        startActivity(intentsms);
    }

    //用户管理
    public void userManager(){
        Intent userIntent = new Intent(MainActivity.this,UserManagerActivity.class);
        userIntent.putExtra("user",userName);
        startActivity(userIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        SubMenu subMenu = menu.addSubMenu("");

        subMenu.add("添加").setIcon(R.drawable.addstudent_image).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                addEmployee();
                return false;
            }
        });
        subMenu.add("删除").setIcon(R.drawable.deletestudent_image).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                deleteEmployee();
//                onResume();
//                Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        subMenu.add("刷新").setIcon(R.drawable.refreshstudent_image).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                reflashList();
                Toast.makeText(MainActivity.this,"刷新成功",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        subMenu.add("搜索").setIcon(R.drawable.search_press).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                searchEmployee();
//                Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        subMenu.add("群发短信").setIcon(R.drawable.sms_press).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sma_all();
//                Toast.makeText(MainActivity.this,"",Toast.LENGTH_LONG).show();
                return false;
            }
        });
//        subMenu.add("用户管理").setIcon(R.drawable.user_image).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                userManager();
////                Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });
        MenuItem item = subMenu.getItem();
        item.setIcon(R.drawable.menu);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * 菜单、返回键响应
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==event.KEYCODE_BACK){
            exitBy2Click();//调用双击退出函数
        }
        return false;
    }

    //双击退出函数
    private static Boolean isExit = false;
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false){
            isExit=true;//准备退出
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;//取消退出
                }
            },2000);//如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }else {

            finish();
            System.exit(0);
        }
    }
}
