package com.rocky.btlock.adapter;

import android.content.Context;
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
import com.rocky.btlock.data.Constant;
import com.rocky.btlock.data.Record;
import com.rocky.btlock.data.User;
import com.rocky.btlock.utils.Utils;

import java.util.ArrayList;

/**
 * Created by rocky.
 */
public class RecordAdapter extends BaseAdapter {
    private ArrayList<Record> goodses;
    private LayoutInflater inflater;
    Context context;

    public RecordAdapter(Context context, ArrayList<Record> arrayList) {
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
            view = inflater.inflate(R.layout.record_item, null);
            holder.time = (TextView) view.findViewById(R.id.time);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.time.setText(goodses.get(position).getTime());
        return view;
    }

    class Holder {
        TextView time;
    }
}
