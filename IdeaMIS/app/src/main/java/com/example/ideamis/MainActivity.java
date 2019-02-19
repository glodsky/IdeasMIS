package com.example.ideamis;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    FileUtls myFileUtils = new FileUtls() ;
    Button btnSave ;
    RadioButton radiobtnChoose;
    TextView txtDate;
    TextView txtTime;
    EditText editContent;

    Timer timer = new Timer();
    public void begin(View view) {
        timer.schedule(task, 1000, 1000);
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtTime.setText(getCurrentDateTime("HH:mm:ss"));
                }
            });
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", sysTime);
                    txtTime.setText(sysTimeStr);
                    break;
            }
        }
    };

   public  String getCurrentDateTime(String format  ){
       SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(format);
       Date date = new Date(System.currentTimeMillis());
       return simpleDateFormat1.format(date);
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSave =  (Button) findViewById(R.id.btnSave);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        editContent = (EditText) findViewById(R.id.editContent);
        editContent.setText("Idea灵感");
        editContent.setSelection(editContent.getText().length());
        txtDate.setText(getCurrentDateTime("yyyyMMdd"));
        txtTime.setText(getCurrentDateTime("HH:mm:ss"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        new Thread() {
            public void run() {
                while(true) { //保证线程一直执行
                    try {
                        //休眠1秒
                        Thread.sleep(1000);
                        //给Handler发送信息
                        mHandler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();

    }
    public void doBtnOnclick(View view){
        switch (view.getId()) {
            // 创建文件夹或者文件
            case R.id.btnSave:
                String fileName = editContent.getText().toString() +  getCurrentDateTime("yyyyMMddHHmm") ;
                myFileUtils.makeRootDirectory(fileName);
                myToast("创建文件夹"+fileName+"成功", 5000);
                break;
        }
    }

    public void doRadioOnclick(View view) {
        String parts,parts2,typeClass = "";
        parts = editContent.getText().toString();
        int end_i = 0 ;
        if ( parts.length()>= "Idea灵感".length())
            end_i = "Idea灵感".length() ;
        else
            end_i = parts.length();
        parts2 = parts.substring(0, end_i);  // 多余扔
        radiobtnChoose = (RadioButton) findViewById(view.getId());
        typeClass = radiobtnChoose.getText().toString();
        switch(view.getId()){
            case R.id.radioButton36: // 其它
                editContent.setText(parts2);
                break;
            default:
                editContent.setText(parts2+typeClass);
                break;

        }
        //editContent.setFocusable(true);
        //editContent.setFocusableInTouchMode(true);
        //editContent.requestFocus();
        editContent.setSelection(editContent.getText().length());
        //myToast(editContent.getText().toString(),2000);
    }

    /**
     * 封装系统提供的toast
     * @param msg   提示的内容
     * @param time  点击后提示弹出来的间隔时间，单位为毫秒
     */
    public void myToast(String msg,int time){
        Toast.makeText(this, msg, time).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
