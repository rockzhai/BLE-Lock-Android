package com.rocky.btlock.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rocky.btlock.R;
import com.rocky.btlock.data.Constant;
import com.rocky.btlock.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import me.drakeet.materialdialog.MaterialDialog;

public class SettingActivity extends AppCompatActivity {
    private static Handler mHandler;
    private static MaterialDialog materialDialog;
    private static MaterialDialog materiaDialog;
    public static SettingActivity instance;
    private static MaterialDialog maDialog;
    private boolean isAdmin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mHandler = new Handler();
        instance = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent in = getIntent();
        isAdmin = in.getExtras().getBoolean("isAdmin");

        Button logout = (Button) findViewById(R.id.logout);
        Button exit = (Button) findViewById(R.id.exit);
        final Button clear = (Button) findViewById(R.id.clear);
        Button changePW = (Button) findViewById(R.id.changePassword);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                MainActivity.instance.finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.finish();
                LoginActivity.instance.finish();
                System.exit(0);
            }
        });
        clear.setText("校验系统时间");
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
                        Locale.getDefault());
                String time = sdf.format(System.currentTimeMillis());
                //20160624232850
                int year, month, day, hour, min, second;
                //
                year = Integer.valueOf(time.substring(0, 4));
                month = Integer.valueOf(time.substring(4, 6));
                day = Integer.valueOf(time.substring(6, 8));
                hour = Integer.valueOf(time.substring(8, 10));
                min = Integer.valueOf(time.substring(10, 12));
                second = Integer.valueOf(time.substring(12, 14));
                String str_year = "0" + Integer.toHexString(year);
                String str_month = "0" + Integer.toHexString(month);
                String str_day = Integer.toHexString(day) + "";
                if (str_day.length() % 2 == 1) {
                    str_day = "0" + str_day;
                }


                String str_hour = Integer.toHexString(hour) + "";
                if (str_hour.length() % 2 == 1) {
                    str_hour = "0" + str_hour;
                }
                String str_min = Integer.toHexString(min) + "";
                if (str_min.length() % 2 == 1) {
                    str_min = "0" + str_min;
                }
                String str_sec = Integer.toHexString(second) + "";
                if (str_sec.length() % 2 == 1) {
                    str_sec = "0" + str_sec;
                }
                LoginActivity.writeChar(Utils.hexStringToBytes(Constant.HEAD_CHAR + "0c" + "22" + Constant.SEVER_BLUETOOTH + str_year + str_month + str_day + str_hour + str_min + str_sec + Constant.END_CHAR));
                Log.e("+++++++++++++++++++++++=",Constant.HEAD_CHAR + "0c" + "22" + Constant.SEVER_BLUETOOTH + str_year + str_month + str_day + str_hour + str_min + str_sec + Constant.END_CHAR);

            }
        });
        changePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.pw_change, null);
                final EditText editText = (EditText) view.findViewById(R.id.pw_change_edit);
                materialDialog = new MaterialDialog(SettingActivity.this).setView(view).setTitle("请输入密码：")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String str = editText.getText().toString();
                                if (!str.isEmpty() && str.length() == 6) {
                                    str = Utils.toHexString(str);
                                    LoginActivity.writeChar(Utils.hexStringToBytes(Constant.HEAD_CHAR + "0b" + Constant.CHANGE_PASSWORD_NUM + Constant.SEVER_BLUETOOTH + str + Constant.END_CHAR));
                                    materialDialog.dismiss();
                                } else {
                                    editText.setError("六位数字密码哦");
                                }

                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        });
                materialDialog.show();
            }
        });
        if (!isAdmin) {
            clear.setVisibility(View.GONE);
            changePW.setVisibility(View.GONE);
        }

    }

    //开锁成功弹窗提示
    public static void chagneSuccess() {
        mHandler.post(new Runnable() {
            @Override
            public synchronized void run() {
                materiaDialog = new MaterialDialog(SettingActivity.instance)
                        .setTitle("提示")
                        .setMessage("修改触屏密码成功！")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materiaDialog.dismiss();

                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materiaDialog.dismiss();

                            }
                        });

                materiaDialog.show();

            }
        });
    }


    //弹窗提示
    public static void sendTimeSuccess() {
        mHandler.post(new Runnable() {
            @Override
            public synchronized void run() {
                maDialog = new MaterialDialog(SettingActivity.instance)
                        .setTitle("提示")
                        .setMessage("时间同步成功！");
                maDialog.setCanceledOnTouchOutside(true);
                maDialog.show();
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
}
