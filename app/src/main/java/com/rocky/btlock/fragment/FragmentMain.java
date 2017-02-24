package com.rocky.btlock.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.rocky.btlock.activity.LoginActivity;
import com.rocky.btlock.R;
import com.rocky.btlock.utils.Utils;
import com.rocky.btlock.data.Constant;

/**
 * Created by Rocky on 2016/5/28.
 */
public class FragmentMain extends Fragment {
    private static ActionProcessButton openLock;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        openLock = (ActionProcessButton) view.findViewById(R.id.btnOpenLock);
        openLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.writeChar(Utils.hexStringToBytes(Constant.OPEN_LOCK));
                openLock.setProgress(50);
            }
        });
        return view;
    }

    public static void setOpen() {
        openLock.setProgress(100);
    }

}
