package com.rocky.btlock.adapter;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.rocky.btlock.R;
import com.rocky.btlock.activity.LoginActivity;
import com.rocky.btlock.activity.MainActivity;
import com.rocky.btlock.data.Constant;
import com.rocky.btlock.data.User;
import com.rocky.btlock.utils.Utils;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by rocky.
 */
public class ListAdapter extends BaseAdapter {
    private ArrayList<User> goodses;
    private LayoutInflater inflater;
    Context context;

    public ListAdapter(Context context, ArrayList<User> arrayList) {
        this.goodses = arrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return goodses.size();
    }

    @Override
    public Object getItem(int position) {
        return goodses.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder = null;
        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.list_item, null);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.addFinger = (ActionProcessButton) view.findViewById(R.id.btn_finger_add);
            holder.list = (RelativeLayout) view.findViewById(R.id.mlist_item);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.name.setText(goodses.get(position).getName());

        if (goodses.get(position).isHaveFiger()) {
            holder.addFinger.setProgress(100);
        } else {
            holder.addFinger.setProgress(0);
        }
        final Holder finalHolder = holder;
        holder.addFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.addFinger.getProgress() == 0) {
                    Log.e("+++++++++++++++++++++++", Constant.HEAD_CHAR + "09" + Constant.ADD_NEW_FINGER_NUM + Constant.SEVER_BLUETOOTH + Utils.toHexString(goodses.get(position).getName()) + Constant.END_CHAR);
                    LoginActivity.writeChar(Utils.hexStringToBytes(Constant.HEAD_CHAR + "09" + Constant.ADD_NEW_FINGER_NUM + Constant.SEVER_BLUETOOTH + Utils.toHexString(goodses.get(position).getName()) + Constant.END_CHAR));
                    Log.e("+++++++++++++++++++++++", Constant.HEAD_CHAR + "09" + Constant.ADD_NEW_FINGER_NUM + Constant.SEVER_BLUETOOTH + Utils.toHexString(goodses.get(position).getName()) + Constant.END_CHAR);
                    Toast.makeText(context, "请根据语音提示进行指纹添加操作", Toast.LENGTH_LONG).show();
                    finalHolder.addFinger.setProgress(50);
                }
            }
        });
        finalHolder.list.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final MaterialDialog materialDialog = new MaterialDialog(MainActivity.instance);
                materialDialog.setTitle("提示");
                materialDialog.setMessage("确定删除" + goodses.get(position).getName() + "用户吗？");
                materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginActivity.writeChar(Utils.hexStringToBytes("aa091301" + Utils.toHexString(goodses.get(position).getName()) + Constant.END_CHAR));
                        //   materialDialog.setMessage("aao91301" + Utils.toHexString(goodses.get(position).getName()) + "od"+"/n");
                        materialDialog.dismiss();


                        goodses.remove(position);
                        notifyDataSetChanged();
                    }
                });
                materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                    }
                });
                materialDialog.setCanceledOnTouchOutside(true);
                materialDialog.show();

                return true;
            }
        });
        return view;
    }

    class Holder {
        TextView name;
        ActionProcessButton addFinger;
        RelativeLayout list;

    }
}
