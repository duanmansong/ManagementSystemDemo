package com.zhiyuan3g.managementsystemdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhiyuan3g.managementsystemdemo.R;
import com.zhiyuan3g.managementsystemdemo.entity.Employee;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/1/5.
 */
public class EmployeeAdapter extends BaseAdapter{
    private Context context;
    private List<Employee> employees;
    private boolean check;
    private static HashMap<Integer,Boolean> isSelected;

    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        EmployeeAdapter.isSelected = isSelected;
    }

    public EmployeeAdapter(){

    }
    public EmployeeAdapter(Context context,List<Employee> employees,boolean check){
        this.context = context;
        this.employees = employees;
        this.check = check;
        isSelected = new HashMap<>();
        initDate();
    }
    private void initDate(){
        for (int i = 0;i<employees.size();i++){
            getIsSelected().put(i,check);
        }
    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Employee employee;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.employee_item,null);
            viewHolder = new ViewHolder();
            viewHolder.emp_list_name = (TextView) convertView.findViewById(R.id.emp_list_name);
            viewHolder.emp_list_id = (TextView) convertView.findViewById(R.id.emp_list_id);
            viewHolder.emp_list_sex = (TextView) convertView.findViewById(R.id.emp_list_sex);
            viewHolder.emp_list_department = (TextView) convertView.findViewById(R.id.emp_list_department);
            viewHolder.emp_checkBox = (CheckBox) convertView.findViewById(R.id.emp_checkBox);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        employee = employees.get(position);
        viewHolder.emp_list_name.setText(employee.getName());
        viewHolder.emp_list_id.setText(employee.getId());
        viewHolder.emp_list_sex.setText(employee.getSex());
        viewHolder.emp_list_department.setText(employee.getDepartment());
        viewHolder.emp_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
//                    employees.get(position).setChecked(false);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
//                    employees.get(position).setChecked(true);
                }
            }
        });
//        viewHolder.emp_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                employees.get(position).setChecked(isChecked);
//            }
//        });
        viewHolder.emp_checkBox.setChecked(getIsSelected().get(position));

        return convertView;
    }

    class ViewHolder{
        private CheckBox emp_checkBox;
        private TextView emp_list_name;
        private TextView emp_list_id;
        private TextView emp_list_sex;
        private TextView emp_list_department;
    }
}
