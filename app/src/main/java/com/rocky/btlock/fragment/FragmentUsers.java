package com.rocky.btlock.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.rocky.btlock.R;
import com.rocky.btlock.activity.LoginActivity;
import com.rocky.btlock.activity.MainActivity;
import com.rocky.btlock.adapter.ListAdapter;
import com.rocky.btlock.data.Constant;
import com.rocky.btlock.data.Record;
import com.rocky.btlock.data.User;
import com.rocky.btlock.utils.Utils;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Rocky on 2016/5/28.
 */
public class FragmentUsers extends Fragment {

    private static Handler mHandler, mHandler_Update;
    private View view;
    private static View viewDialog;
    private static ListAdapter mAdapter;
    private static ListView listView;
    private static ArrayList<User> arrayList;
    private Button addUser, addNew;
    private static MaterialDialog mDialog, materialDialog, md_del;
    private EditText name, password;
    private int id = 1;
    private static final String TAG = "Fragment_USER";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_users, container, false);
        viewDialog = inflater.inflate(R.layout.add_user, container, false);
        listView = (ListView) view.findViewById(R.id.user_listView);
        addUser = (Button) view.findViewById(R.id.btn_add_user);
        addNew = (Button) viewDialog.findViewById(R.id.btn_add_new_user);
        name = (EditText) viewDialog.findViewById(R.id.name);
        password = (EditText) viewDialog.findViewById(R.id.password);
        arrayList = new ArrayList<User>();
        mAdapter = new ListAdapter(getActivity(), arrayList);
        listView.setAdapter(mAdapter);
        mDialog = new MaterialDialog(MainActivity.instance).setTitle("添加用户").setView(viewDialog);
        mDialog.setCanceledOnTouchOutside(true);
        LoginActivity.writeChar(Utils.hexStringToBytes("aa0519010d"));

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String name = bundle.getString("name");
                int id = bundle.getInt("id");
                boolean isHaveFinger = bundle.getBoolean("isHaveFinger");
                User user = new User();
                user.setId(id);
                user.setHaveFiger(isHaveFinger);
                user.setName(name);
                arrayList.add(user);
                mAdapter.notifyDataSetChanged();
            }
        };
        mHandler_Update = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mAdapter.notifyDataSetChanged();
            }
        };


        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() != 4) {
                    name.setError("请输入四位用户名");
                } else if (password.getText().length() != 6) {
                    password.setError("请输入六位密码");
                } else {
                    String str = Utils.toHexString(name.getText().toString() + password.getText().toString());
                    LoginActivity.writeChar(Utils.hexStringToBytes(Constant.HEAD_CHAR + "0f" + Constant.NEW_USER_NUM + Constant.SEVER_BLUETOOTH + str + Constant.END_CHAR));
                    Log.e(TAG, Constant.HEAD_CHAR + "0f" + Constant.NEW_USER_NUM + Constant.SEVER_BLUETOOTH + str + Constant.END_CHAR);
                    mDialog.dismiss();
                }
            }
        });


//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                Log.e("TAG","++++++++++++++++++++++++++++++idiiididididididi");
//                Toast.makeText(MainActivity.instance, "删除操作", Toast.LENGTH_LONG).show();
////                md_del = new MaterialDialog(MainActivity.instance)
////                        .setTitle("删除提示")
////                        .setPositiveButton("确定", new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                String del_name = Utils.toHexString(arrayList.get(position).getName());
////                                LoginActivity.writeChar(Utils.hexStringToBytes(Constant.HEAD_CHAR + "09" + Constant.DEL_USER_NUM + Constant.SEVER_BLUETOOTH + del_name + Constant.END_CHAR));
////                                arrayList.remove(position);
////                                mAdapter.notifyDataSetChanged();
////                            }
////                        })
////                        .setNegativeButton("取消", new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                md_del.dismiss();
////                            }
////                        });
////                md_del.show();
//                return true;
//            }
//        });
        return view;
    }


//    //读取本地数据
//    private void loadData() {
//        // for (id = 1; sp.getString(id + "", "end").equals("end"); id++) {
//        //读取数据
//        sp = getActivity().getSharedPreferences("SP", Context.MODE_PRIVATE);
//        boolean isFinger = false;
//        String str = sp.getString(id + "", "none0");
//        if (str.charAt(4) != '0') {
//            isFinger = true;
//        }
//        str = str.substring(0, str.length() - 1);
//        addData(id, str + "(管理员)", isFinger);
//        // }
//    }

    public static void addData(int id, String name, boolean isHaveFinger) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("name", name);
        bundle.putBoolean("isHaveFinger", isHaveFinger);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    //添加指纹成功，反馈刷新
    public static void addFingerSuccess(final String name) {
        mHandler.post(new Runnable() {
            @Override
            public synchronized void run() {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getName().equals(name)) {
                        arrayList.get(i).setHaveFiger(true);
                        mAdapter.notifyDataSetChanged();
                    }
                }


            }
        });
    }

    //弹窗提示
    public static void setDialog(final String str) {
        mHandler.post(new Runnable() {
            @Override
            public synchronized void run() {
                materialDialog = new MaterialDialog(MainActivity.instance)
                        .setTitle("提示")
                        .setMessage(str)
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                                mHandler_Update.sendMessage(new Message());
                            }
                        });
                materialDialog.show();
                materialDialog.setCanceledOnTouchOutside(true);
            }
        });
    }

    //添加指纹失败
    public static void addFingerFail() {
        mHandler_Update.post(new Runnable() {
            @Override
            public synchronized void run() {
                materialDialog = new MaterialDialog(MainActivity.instance)
                        .setTitle("提示")
                        .setMessage("增加指纹失败")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                                mHandler_Update.sendMessage(new Message());
                            }
                        });
                materialDialog.show();
            }
        });
    }


}
