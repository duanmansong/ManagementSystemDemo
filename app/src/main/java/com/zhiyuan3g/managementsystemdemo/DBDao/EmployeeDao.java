package com.zhiyuan3g.managementsystemdemo.DBDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhiyuan3g.managementsystemdemo.db.MySqlHelper;
import com.zhiyuan3g.managementsystemdemo.entity.Employee;
import com.zhiyuan3g.managementsystemdemo.utils.Contents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/5.
 */
public class EmployeeDao {
    private Context context;
    MySqlHelper helper;
    SQLiteDatabase db;

    public EmployeeDao(Context context) {
        this.context = context;
        helper = new MySqlHelper(context, Contents.DBNAME,null,Contents.VERSION);
        db = helper.getReadableDatabase();
    }

    private boolean addEmployee(Employee employee){
        boolean result = true;
        ContentValues values = new ContentValues();
        values.put("name",employee.getName());
        values.put("sex",employee.getSex());
        values.put("mingZu",employee.getMingZu());
        values.put("id",employee.getId());
        values.put("department",employee.getDepartment());
        values.put("birthday",employee.getBirthday());
        values.put("phone",employee.getPhone());
        values.put("user",employee.getUser());
        values.put("more",employee.getMore());
//        values.put("image", String.valueOf(employee.getImage()));
        long rows = db.insert("employee",null,values);
        if (rows>0){
            result = true;
        }
        return result;
    }
    public boolean deleteEmployee(String id){
        boolean result = false;
        long rows = db.delete("employee", "id=?", new String[]{id});
        if (rows>0){
            result = true;
        }
        return result;
    }

    public boolean modifyEmployeeById(Employee employee){
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put("name",employee.getName());
        values.put("sex",employee.getSex());
        values.put("mingZu",employee.getMingZu());
        values.put("id",employee.getId());
        values.put("department",employee.getDepartment());
        values.put("birthday",employee.getBirthday());
        values.put("phone",employee.getPhone());
        values.put("user",employee.getUser());
        values.put("more",employee.getMore());
//        values.put("image", String.valueOf(employee.getImage()));
        long rows = db.update("employee", values, "id=?", new String[]{String.valueOf(employee.getId())});
        if (rows>0){
            result = true;
        }
        return result;
    }
    public List<Employee> getAllEmployee(){
        List<Employee> employees = new ArrayList<>();
        String[] columns = {"name","sex","mingZu","id","department","birthday","phone","more","image"};
        Cursor cursor = db.query("employee", columns, null, null, null, null, null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String mingZu = cursor.getString(cursor.getColumnIndex("mingZu"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String department = cursor.getString(cursor.getColumnIndex("department"));
                String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String user = cursor.getString(cursor.getColumnIndex("user"));
                String more = cursor.getString(cursor.getColumnIndex("more"));
//            byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
                Employee employee = new Employee(name,sex,mingZu,id,department,birthday,phone,user,more);
                employees.add(employee);
            }
        }
        return employees;
    }

    public List<Employee> getAllEmployee(String empDepartment,String userName){
        List<Employee> employees = new ArrayList<>();
//        String[] columns = {"name","sex","mingZu","id","department","birthday","phone","user","more","image"};
//        Cursor cursor = db.query("employee", columns, "department=?", new String[]{empDepartment}, null, null, null);
        String sql = "select * from employee where department like '"+empDepartment+"' and user like '"+userName+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String mingZu = cursor.getString(cursor.getColumnIndex("mingZu"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String department = cursor.getString(cursor.getColumnIndex("department"));
                String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String user = cursor.getString(cursor.getColumnIndex("user"));
                String more = cursor.getString(cursor.getColumnIndex("more"));
//            byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                Employee employee = new Employee(name,sex,mingZu,id,department,birthday,phone,user,more);
                employees.add(employee);
            }
        }
        return employees;
    }

    //模糊查询
    public List<Employee> search(String eName,String userName){
        List<Employee> employees = new ArrayList<>();
        String sql = "select * from employee where name like '%"+eName+"%' and user like '"+userName+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String mingZu = cursor.getString(cursor.getColumnIndex("mingZu"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String department = cursor.getString(cursor.getColumnIndex("department"));
                String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String user = cursor.getString(cursor.getColumnIndex("user"));
                String more = cursor.getString(cursor.getColumnIndex("more"));
//            byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                Employee employee = new Employee(name,sex,mingZu,id,department,birthday,phone,user,more);
                employees.add(employee);
            }
        }
        return employees;
    }
    public List<Employee> getAllEmployeeByName(String empName){
        List<Employee> employees = new ArrayList<>();
        String[] columns = {"name","sex","mingZu","id","department","birthday","phone","user","more","image"};
        Cursor cursor = db.query("employee", columns,"name=?" , new String[]{empName}, null, null, null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String mingZu = cursor.getString(cursor.getColumnIndex("mingZu"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String department = cursor.getString(cursor.getColumnIndex("department"));
                String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String user = cursor.getString(cursor.getColumnIndex("user"));
                String more = cursor.getString(cursor.getColumnIndex("more"));
//            byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
                Employee employee = new Employee(name,sex,mingZu,id,department,birthday,phone,user,more);
                employees.add(employee);
            }
        }
        return employees;
    }

    public List<Employee> getAllEmployeeByUserName(String userName){
        List<Employee> employees = new ArrayList<>();
        String[] columns = {"name","sex","mingZu","id","department","birthday","phone","user","more","image"};
        Cursor cursor = db.query("employee", columns, "user=?", new String[]{userName}, null, null, null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String mingZu = cursor.getString(cursor.getColumnIndex("mingZu"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String department = cursor.getString(cursor.getColumnIndex("department"));
                String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String user = cursor.getString(cursor.getColumnIndex("user"));
                String more = cursor.getString(cursor.getColumnIndex("more"));
//            byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
                Employee employee = new Employee(name,sex,mingZu,id,department,birthday,phone,user,more);
                employees.add(employee);
            }
        }
        return employees;
    }
    public Employee getEmployeeById(String eid) {
        Employee employee = null;
        String[] columns = {"name","sex","mingZu","id","department","birthday","phone","user","more","image"};
        Cursor cursor = db.query("employee", columns, "id=?", new String[]{eid}, null, null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            String mingZu = cursor.getString(cursor.getColumnIndex("mingZu"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String department = cursor.getString(cursor.getColumnIndex("department"));
            String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String user = cursor.getString(cursor.getColumnIndex("user"));
            String more = cursor.getString(cursor.getColumnIndex("more"));
//            byte[] image = cursor.getBlob(9);
            employee = new Employee(name,sex,mingZu,id,department,birthday,user,phone,more);
        }
        return employee;
    }
    public Employee getEmployeeByDepartment(String mdepartment){
        Employee employee = null;
        String[] columns = {"name","sex","mingZu","id","department","birthday","phone","user","more","image"};
        Cursor cursor = db.query("employee", columns, "id=?", new String[]{mdepartment}, null, null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            String mingZu = cursor.getString(cursor.getColumnIndex("mingZu"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String department = cursor.getString(cursor.getColumnIndex("department"));
            String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String user = cursor.getString(cursor.getColumnIndex("user"));
            String more = cursor.getString(cursor.getColumnIndex("more"));
//            byte[] image = cursor.getBlob(9);
            employee = new Employee(name,sex,mingZu,id,department,birthday,phone,user,more);
        }
        return employee;
    }
}
