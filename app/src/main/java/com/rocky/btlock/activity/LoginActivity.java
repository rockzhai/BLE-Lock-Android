package com.rocky.btlock.activity;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rocky.btlock.bluetooth.BluetoothLeClass;
import com.rocky.btlock.R;
import com.rocky.btlock.data.Constant;
import com.rocky.btlock.fragment.FragmentUsers;
import com.rocky.btlock.utils.Utils;

import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * 登陆
 */
public class LoginActivity extends AppCompatActivity {
    private TextView testText;
    private Button checkAddress;
    private MaterialDialog materialDialog;
    private static Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance = this;
        //初始化登陆界面
        testText = (TextView) findViewById(R.id.show);
        nameView = (AutoCompleteTextView) findViewById(R.id.user_name);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mLogInButton = (Button) findViewById(R.id.login_button);
        mReg = (Button) findViewById(R.id.login_register);
        checkAddress = (Button) findViewById(R.id.checkBlueadress);
        mActionbar = getSupportActionBar();
        mActionbar.setSubtitle("尚未连接设备");
        //蓝牙相关
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        Log.i(TAG, "mBluetoothAdapter = " + mBluetoothAdapter);
        //打开蓝牙
        mBluetoothAdapter.enable();
        mActionbar.setSubtitle("蓝牙已打开");
        myBleAddress = mBluetoothAdapter.getAddress();
        //底层说并没有给冒号安排存储空间，把冒号去掉=-=
        myBleAddress = myBleAddress.replaceAll(":", "");
        mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }
        Log.i(TAG, "mBLE = e" + mBLE);
        mActionbar.setSubtitle("正在寻找智能锁设备...");
        // 发现BLE终端的Service时回调事件
        mBLE.setOnServiceDiscoverListener(mServiceDiscover);

        // 收到BLE终端数据交互的事件
        mBLE.setOnDataAvailableListener(mOnDataAvailable);
        final String str = Utils.toHexString("root");

        mReg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
                SharedPreferences sp = instance.getSharedPreferences("SP", MODE_PRIVATE);
                //存入数据
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString(1 + "", nameView.getText().toString() + 0);
//                editor.putInt("id", 1);
//                editor.commit();
                String reg = Utils.toHexString(nameView.getText().toString() + mPasswordView.getText().toString());
                writeChar(Utils.hexStringToBytes(Constant.HEAD_CHAR + "0f" + Constant.CREAT_ADMIN + Constant.SEVER_BLUETOOTH + reg + Constant.END_CHAR));

            }
        });

        mLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
                login();
            }
        });

        checkAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // checkAddress.setText("AA0b");
                writeChar(Utils.hexStringToBytes(Constant.HEAD_CHAR + "0b" + Constant.SEND_ADDRESS + Constant.SEVER_BLUETOOTH + myBleAddress + Constant.END_CHAR));
                Toast.makeText(getApplicationContext(), "蓝牙地址发送成功", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //登录验证
    private void login() {
        if (checkFormat) {
            String str = Utils.toHexString(nameView.getText().toString() + mPasswordView.getText().toString());

            writeChar(Utils.hexStringToBytes(Constant.HEAD_CHAR + "0f" + Constant.LOGIN + Constant.SEVER_BLUETOOTH + str + Constant.END_CHAR));
        }
    }

    /**
     * 登录验证
     */
    private void attemptLogin() {
        nameView.setError(null);
        mPasswordView.setError(null);
        //获取用户输入值
        String name = nameView.getText().toString();
        String password = mPasswordView.getText().toString();
        //检查密码有效性
        if (name.length() != 4) {
            if (name.length() == 0) {
                nameView.setError("请输入用户名");
            } else {
                nameView.setError("用户名为四位哦~");
            }
        } else if (password.length() != 6 || password.isEmpty()) {
            mPasswordView.setError("Tips：六位密码");
        } else {
            checkFormat = true;
        }

    }

    //弹窗提示
    public void makeDialog(final String str) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                materialDialog = new MaterialDialog(LoginActivity.instance)
                        .setTitle("提示")
                        .setMessage(str)
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();

                            }
                        });
                materialDialog.show();
            }
        });

    }


    private BluetoothLeClass.OnServiceDiscoverListener mServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {
        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLE.getSupportedGattServices());
        }
    };

    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {
        /**
         * BLE终端数据被读的事件
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG,
                    "onCharRead " + gatt.getDevice().getName() + " read "
                            + characteristic.getUuid().toString() + " -> "
                            + Utils.bytesToHexString(characteristic.getValue()));
        }

        /**
         * 收到BLE终端写入数据回调
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            Log.e(TAG, "onCharWrite " + gatt.getDevice().getName() + " write "
//                    + characteristic.getUuid().toString() + " -> "
//                    + new String(characteristic.getValue()));
            String str = Utils.bytesToHexString(characteristic.getValue());
            Log.e(TAG, "+++++++++++++++++++++" + str);
            //管理员登录成功指令
//            String str2 = "aa0406020d";
//            Log.e(TAG, "+++++++++++++++++++++" + str2);
            Intent in = new Intent(LoginActivity.this, MainActivity.class);
            switch (str) {
                case Constant.NO_BLUE_ADDRESS:
                    makeDialog("请先验证蓝牙地址");
                    break;
                //创建管理员账号成功
                case Constant.CREAT_ADMIN_CALLBACK_SUCCESS:
                    makeDialog("管理员账号创建成功");
                    break;
                //创建管理员账号失败
                case Constant.CREAT_ADMIN_CALLBACK_FAIL:
                    makeDialog("管理员账号创建失败");
                    break;
                //管理员登陆成功
                case Constant.LOGIN_ADMIN_SUCCESS:
                    Log.e(TAG, "++++++++++++++++++++admin login");
                    in.putExtra("isAdmin", true);
                    in.putExtra("UserName", nameView.getText().toString());
                    startActivity(in);
                    break;
                //普通用户登录成功
                case Constant.LOGIN_USER_SUCCESS:
                    Log.e(TAG, "++++++++++++++++++++admin login");
                    in.putExtra("isAdmin", false);
                    in.putExtra("UserName", nameView.getText().toString());
                    startActivity(in);
                    break;
                //用户登录失败
                case Constant.LOGIN_FAIL:
                    makeDialog("登录失败，请重试");
                    break;
                //开锁成功
                case Constant.OPEN_LOCK_SUCCESS:
                    MainActivity.setOpen();
                    break;
                //记录发送完毕
                case Constant.RECORE_END:
                    RecordActivity.addEnd();
                    break;
                //修改触摸屏密码成功
                case Constant.CHANGE_PASSWORD_SUCCESS:
                    SettingActivity.chagneSuccess();
                    break;
                //新增用户成功
                case Constant.ADD_USERS_SUCCESS:
                    FragmentUsers.setDialog("新增用户成功");
                    break;
                //删除用户成功
                case Constant.DEL_USER_SUCCESS:
                    FragmentUsers.setDialog("删除用户成功");
                    break;
                //新增用户失败
                case Constant.ADD_USE_FAIL:
                    FragmentUsers.setDialog("该用户名已被占用");
                    break;
                //添加指纹失败
                case Constant.ADD_FINGER_FAIL:
                    FragmentUsers.addFingerFail();
                    break;
                //时间
                case Constant.TIME_SUCESS:
                    SettingActivity.sendTimeSuccess();
                    break;
                //管理员
                case Constant.ADMIN_DEL_FAIL:
                    FragmentUsers.setDialog("管理员用户不可删");
                    break;

            }
            //添加存储记录
            if (str.charAt(4) == '0' && str.charAt(5) == 'c') {
                RecordActivity.addData(1, str);
                Log.e(TAG, "+++++++++++++++++++" + str);
            }
            //添加用户信息
            if (str.charAt(4) == '2' && str.charAt(5) == '0') {
                //  int id = Utils.charToInt(str.charAt(8)) * 16 + Utils.charToInt(str.charAt(9));
                String str_name = Utils.hexToString(str.substring(8, 16));
                boolean b = false;
                if (str.charAt(17) == '1') {
                    b = true;
                }
                FragmentUsers.addData(1, str_name, b);
                Log.e(TAG, "+++++++++++++++++++" + str);
            }
            //添加指纹成功
            if (str.charAt(4) == '1' && str.charAt(5) == '6') {
                FragmentUsers.addFingerSuccess(Utils.hexToString(str.substring(8, 16)));
                Log.e(TAG, "+++++++++++++++++++" + str);
            }

        }
    };

    public static void writeChar(byte[] bytes) {
        Log.i(TAG, "Message = " + bytes);
        if (gattCharacteristic_char != null) {
            gattCharacteristic_char.setValue(bytes);
            mBLE.writeCharacteristic(gattCharacteristic_char);
        }
    }

    //搜索设备
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // 设备搜索结果
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //5F:52:CC:7F:86:55
                    //20:91:48:50:5C:8E
                    //D4:F5:13:78:E9:63
                    //20:91:48:50:58:DE
                    if (device.getAddress().equals("20:91:48:50:58:DE")) {
                        scanLeDevice(false);
                        Log.e(TAG, device.getAddress());
                        Log.e(TAG, device.getName());
                        boolean bRet = mBLE.connect(device.getAddress());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (bRet) {
                            mActionbar.setSubtitle("已连接，请登录");
                            Log.e(TAG, "connect+++++success");
                            Log.e(TAG, Constant.HEAD_CHAR + "0B" + Constant.SEND_ADDRESS + Constant.SEVER_BLUETOOTH + myBleAddress + Constant.END_CHAR);
                        }

                    }

                }
            });
            // rssi
            Log.i(TAG, "rssi = " + rssi);
            Log.i(TAG, "mac = " + device.getAddress());
            Log.i(TAG, "scanRecord.length = " + scanRecord.length);
        }

    };

    @Override
    protected void onResume() {
        Log.i(TAG, "---> onResume");
        super.onResume();
        mBLE.close();
        scanLeDevice(true);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "---> onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "---> onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "---> onDestroy");
        super.onDestroy();
        Log.e(TAG, "start onDestroy~~~");
        scanLeDevice(false);
        mBLE.disconnect();
        mBLE.close();
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        BluetoothGattCharacteristic Characteristic_cur = null;

        for (BluetoothGattService gattService : gattServices) {
            // -----Service的字段信息----//
            int type = gattService.getType();
            Log.e(TAG, "-->service type:" + Utils.getServiceType(type));
            Log.e(TAG, "-->includedServices size:"
                    + gattService.getIncludedServices().size());
            Log.e(TAG, "-->service uuid:" + gattService.getUuid());

            // -----Characteristics的字段信息----//
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                Log.e(TAG, "---->char uuid:" + gattCharacteristic.getUuid());

                int permission = gattCharacteristic.getPermissions();
                Log.e(TAG,
                        "---->char permission:"
                                + Utils.getCharPermission(permission));

                int property = gattCharacteristic.getProperties();
                Log.e(TAG,
                        "---->char property:"
                                + Utils.getCharPropertie(property));

                byte[] data = gattCharacteristic.getValue();
                if (data != null && data.length > 0) {
                    Log.e(TAG, "---->char value:" + new String(data));
                }

                if (gattCharacteristic.getUuid().toString().equals(UUID_CHAR6)) {
                    // 把char1 保存起来以方便后面读写数据时使用
                    gattCharacteristic_char = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    Log.i(TAG, "+++++++++UUID_CHAR");
                }

                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_HERATRATE)) {
                    // 把heartrate 保存起来以方便后面读写数据时使用
                    gattCharacteristic_heartrate = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    Log.i(TAG, "+++++++++UUID_HERATRATE");
                }

                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_KEY_DATA)) {
                    // 把heartrate 保存起来以方便后面读写数据时使用
                    gattCharacteristic_keydata = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    Log.i(TAG, "+++++++++UUID_KEY_DATA");
                }

                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_TEMPERATURE)) {
                    // 把heartrate 保存起来以方便后面读写数据时使用
                    gattCharacteristic_temperature = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    Log.i(TAG, "+++++++++UUID_TEMPERATURE");
                }

                // -----Descriptors的字段信息----//
                List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic
                        .getDescriptors();
                for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
                    Log.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
                    int descPermission = gattDescriptor.getPermissions();
                    Log.e(TAG,
                            "-------->desc permission:"
                                    + Utils.getDescPermission(descPermission));

                    byte[] desData = gattDescriptor.getValue();
                    if (desData != null && desData.length > 0) {
                        Log.e(TAG, "-------->desc value:" + new String(desData));
                    }
                }
            }
        }//

    }


    public static LoginActivity instance = null;
    private static final String TAG = "LoginAvtivity";
    private static String myBleAddress;
    // UI
    private AutoCompleteTextView nameView;
    private EditText mPasswordView;
    private boolean checkFormat = false;
    private boolean checkResult = false;
    private ActionBar mActionbar;
    private Button mReg;

    //初始化变量
    public static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR6 = "0000fff6-0000-1000-8000-00805f9b34fb";
    public static String UUID_HERATRATE = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String UUID_TEMPERATURE = "00002a1c-0000-1000-8000-00805f9b34fb";

    static BluetoothGattCharacteristic gattCharacteristic_char1 = null;
    static BluetoothGattCharacteristic gattCharacteristic_char = null;
    static BluetoothGattCharacteristic gattCharacteristic_heartrate = null;
    static BluetoothGattCharacteristic gattCharacteristic_keydata = null;
    static BluetoothGattCharacteristic gattCharacteristic_temperature = null;
    ;
    // 搜索BLE终端
    private BluetoothAdapter mBluetoothAdapter;
    // 读写BLE终端
    static private BluetoothLeClass mBLE;
    public String bluetoothAddress;
    static private byte writeValue_char1 = 0;
    private boolean mScanning;
    private int mRssi;

}

