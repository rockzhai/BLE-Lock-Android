package com.rocky.btlock.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.rocky.btlock.R;
import com.rocky.btlock.adapter.RecordAdapter;
import com.rocky.btlock.data.Constant;
import com.rocky.btlock.data.Record;
import com.rocky.btlock.utils.Utils;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

public class RecordActivity extends AppCompatActivity {
    private static ListView mRecordList;
    private static ArrayList<Record> arrayList;
    private static ActionProcessButton getRecord;
    private static RecordAdapter mAdapter;
    private static Handler mHandler1;
    private static Handler mHandler;
    private static RecordActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getSupportActionBar().setTitle("开锁记录");
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String time = bundle.getString("time");
                int id = bundle.getInt("id");
                Record record = new Record();
                record.setTime(time);
                record.setId(id);
                arrayList.add(record);
                mAdapter.notifyDataSetChanged();
            }
        };

        instance = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        arrayList = new ArrayList<Record>();
        LoginActivity.writeChar(Utils.hexStringToBytes(Constant.RECORD_REQUEST));
        getRecord = (ActionProcessButton) findViewById(R.id.getRecord);
        mRecordList = (ListView) findViewById(R.id.recordList);
        mAdapter = new RecordAdapter(this, arrayList);
        mRecordList.setAdapter(mAdapter);
        getRecord.setProgress(50);
        getRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.writeChar(Utils.hexStringToBytes(Constant.RECORD_REQUEST));
                getRecord.setProgress(50);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void addData(int id, String time) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        //  int a, b, c, d;

        bundle.putInt("id", id);
        bundle.putString("time", Utils.hexToString(time.substring(8, 16)) + "\n" + Utils.getTime(time));
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

//    //添加纪录
//    public static void addRecord(final int id, final String time) {
//        mHandler1.post(new Runnable() {
//            @Override
//            public synchronized void run() {
//                int a ,b;
//                a= time.charAt(10)*10+time.charAt(11);
//                b=time.charAt(12)*10+time.charAt(12);
//                String string = (a*256+b)+"";
//                addData(id, string);
//                addData(id, time);
//
//            }
//        });
//
//
//    }

    //完成读取
    public static void addEnd() {
        mHandler.post(new Runnable() {
            @Override
            public synchronized void run() {
                final MaterialDialog materiaDialog = new MaterialDialog(RecordActivity.instance);
                materiaDialog.setTitle("提示");
                materiaDialog.setMessage("记录更新完毕！");
                materiaDialog.setNegativeButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materiaDialog.dismiss();

                    }
                });
                materiaDialog.show();
                getRecord.setProgress(100);
            }
        });


    }
}
