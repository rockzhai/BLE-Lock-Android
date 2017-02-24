package com.rocky.btlock.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rocky.btlock.R;
import com.rocky.btlock.fragment.FragmentMain;
import com.rocky.btlock.fragment.FragmentUsers;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Rocky on 2016/5/28.
 */
public class MainActivity extends MaterialNavigationDrawer {
    private final static String TAG = "MainActivity";
    //点击两次返回键退出
    private long mExitTime = 0;
    public static FragmentMain mainFragment;
    public static MainActivity instance;
    private static Toolbar toolbar;
    private static MaterialDialog materiaDialog;
    private static Handler mHandler = new Handler();
    private boolean isAdmin = true;
    private String userName, userAttr = "普通用户";

    @Override
    public void init(Bundle savedInstanceState) {
        instance = this;
        Intent in = getIntent();
        //isAdmin = in.getExtras().getBoolean("isAdmin");
        //userName = in.getExtras().getString("UserName");
        if (isAdmin) {
            userAttr = "管理员账户";
        }
        Intent toSet = new Intent(MainActivity.this, SettingActivity.class);
        toSet.putExtra("isAdmin", isAdmin);
        toolbar = getToolbar();
        toolbar.setPopupTheme(R.style.ToolbarPopupTheme);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_about:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                }
                return false;
            }
        });
        final MaterialAccount account = new MaterialAccount(this.getResources(), userName, userAttr, R.drawable.photo, R.drawable.bamboo);
        this.addAccount(account);
        mainFragment = new FragmentMain();
        MaterialSection mSection = newSection("智能开锁", mainFragment);
        this.addSection(mSection);
        if (isAdmin) {
            this.addSection(newSection("开锁记录", new Intent(this, RecordActivity.class)));
            this.addSection(newSection("用户管理", new FragmentUsers()));
            userAttr = "管理员账户";
        }

        this.addSection(newSection("关于我们", new Intent(this, AboutActivity.class)));
        this.addBottomSection(newSection("设置", R.drawable.ic_settings_black_24dp, toSet));
        this.setAccountListener(new MaterialAccountListener() {
            @Override
            public void onAccountOpening(MaterialAccount account) {
                if (account.getAccountNumber() == 0) {
                    Toast.makeText(getApplication(), "当前用户：" + account.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChangeAccount(MaterialAccount newAccount) {
                if (account.getAccountNumber() == 0) {
                    // TODO
                }
            }
        });

    }

    //开锁成功弹窗提示
    public static void setOpen() {
        mHandler.post(new Runnable() {
            @Override
            public synchronized void run() {
                materiaDialog = new MaterialDialog(MainActivity.instance)
                        .setTitle("提示")
                        .setMessage("开锁成功！")
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

                mainFragment.setOpen();
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {//
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                // 否则退出程序
                LoginActivity.instance.finish();
                System.exit(0);

            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
