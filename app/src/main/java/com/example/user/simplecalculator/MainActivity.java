package com.example.user.simplecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etNum1;
    EditText etNum2;

    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;
    Button btnHistory;

    TextView tvResult;
    DBHelper dbHelper;
    String oper = "";
    private static final String KEY_RESULT = "RESULT";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNum1 = (EditText) findViewById(R.id.etNum1);
        etNum2 = (EditText) findViewById(R.id.etNum2);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMult = (Button) findViewById(R.id.btnMult);
        btnDiv = (Button) findViewById(R.id.btnDiv);
        btnHistory = (Button) findViewById(R.id.btnHistory);

        tvResult = (TextView) findViewById(R.id.tvResult);

        if (savedInstanceState != null) {
            oper = savedInstanceState.getString(KEY_RESULT, "");
            tvResult.setText(oper);
        }

        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMult.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
        btnHistory.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              SQLiteDatabase db = dbHelper.getWritableDatabase();
                                              ArrayList<String> tableData = new ArrayList<>();
                                              Cursor c = db.query("mytable", null, null, null, null, null, null);
                                              if (c.moveToFirst()) {
                                                  do {
                                                      float num1= c.getFloat(c.getColumnIndex("num1"));
                                                      float num2 = c.getFloat(c.getColumnIndex("num2"));
                                                      String oper = c.getString(c.getColumnIndex("oper"));
                                                      float result = c.getFloat(c.getColumnIndex("result"));
                                                      tableData.add(num1+oper+num2 + " = "+result);
                                                  } while (c.moveToNext());
                                              }
                                              c.close();
                                              Intent intent = new Intent(MainActivity.this, History.class);
                                              intent.putExtra("array",tableData);
                                              startActivity(intent);
                                          }
                                      });

        dbHelper = new DBHelper(this);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_RESULT, tvResult.getText().toString());
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        float num1 = 0;
        float num2 = 0;
        float result = 0;
        ContentValues cv = new ContentValues();
        if (TextUtils.isEmpty(etNum1.getText().toString())
                || TextUtils.isEmpty(etNum2.getText().toString())) {
            return;
        }
        num1 = Float.parseFloat(etNum1.getText().toString());
        num2 = Float.parseFloat(etNum2.getText().toString());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2;
                cv.put("num1",num1);
                cv.put("num2",num2);
                cv.put("oper",oper);
                cv.put("result",result);
                db.insert("mytable", null, cv);
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2;
                cv.put("num1",num1);
                cv.put("num2",num2);
                cv.put("oper",oper);
                cv.put("result",result);
                db.insert("mytable", null, cv);
                break;
            case R.id.btnMult:
                oper = "*";
                result = num1 * num2;
                cv.put("num1",num1);
                cv.put("num2",num2);
                cv.put("oper",oper);
                cv.put("result",result);
                db.insert("mytable", null, cv);
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2;
                cv.put("num1",num1);
                cv.put("num2",num2);
                cv.put("oper",oper);
                cv.put("result",result);
                db.insert("mytable", null, cv);
                break;
            default:
                break;
        }
        tvResult.setText(num1 + " " + oper + " " + num2 + " = " + result);
    }
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "num1 real,"
                    + "num2 real,"
                    +"oper text,"
                    +"result real"+ ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
