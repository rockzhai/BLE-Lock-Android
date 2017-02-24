
package com.rocky.btlock.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class Utils {

    private static HashMap<Integer, String> serviceTypes = new HashMap();

    static {
        // Sample Services.
        serviceTypes.put(BluetoothGattService.SERVICE_TYPE_PRIMARY, "PRIMARY");
        serviceTypes.put(BluetoothGattService.SERVICE_TYPE_SECONDARY, "SECONDARY");
    }

    public static String getServiceType(int type) {
        return serviceTypes.get(type);
    }


    //-------------------------------------------    
    private static HashMap<Integer, String> charPermissions = new HashMap();

    static {
        charPermissions.put(0, "UNKNOW");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ, "READ");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED, "READ_ENCRYPTED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM, "READ_ENCRYPTED_MITM");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE, "WRITE");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED, "WRITE_ENCRYPTED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM, "WRITE_ENCRYPTED_MITM");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED, "WRITE_SIGNED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED_MITM, "WRITE_SIGNED_MITM");
    }

    public static String getCharPermission(int permission) {
        return getHashMapValue(charPermissions, permission);
    }

    //-------------------------------------------
    private static HashMap<Integer, String> charProperties = new HashMap();

    static {

        charProperties.put(BluetoothGattCharacteristic.PROPERTY_BROADCAST, "BROADCAST");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS, "EXTENDED_PROPS");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_INDICATE, "INDICATE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_NOTIFY, "NOTIFY");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_READ, "READ");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE, "SIGNED_WRITE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_WRITE, "WRITE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE, "WRITE_NO_RESPONSE");
    }

    public static String getCharPropertie(int property) {
        return getHashMapValue(charProperties, property);
    }

    //--------------------------------------------------------------------------
    private static HashMap<Integer, String> descPermissions = new HashMap();

    static {
        descPermissions.put(0, "UNKNOW");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ, "READ");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED, "READ_ENCRYPTED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM, "READ_ENCRYPTED_MITM");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE, "WRITE");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED, "WRITE_ENCRYPTED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM, "WRITE_ENCRYPTED_MITM");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED, "WRITE_SIGNED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED_MITM, "WRITE_SIGNED_MITM");
    }

    public static String getDescPermission(int property) {
        return getHashMapValue(descPermissions, property);
    }


    private static String getHashMapValue(HashMap<Integer, String> hashMap, int number) {
        String result = hashMap.get(number);
        if (TextUtils.isEmpty(result)) {
            List<Integer> numbers = getElement(number);
            result = "";
            for (int i = 0; i < numbers.size(); i++) {
                result += hashMap.get(numbers.get(i)) + "|";
            }
        }
        return result;
    }

    /**
     * 位运算结果的反推函数10 -> 2 | 8;
     */
    private static List<Integer> getElement(int number) {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++) {
            int b = 1 << i;
            if ((number & b) > 0)
                result.add(b);
        }

        return result;
    }

    // 转化十六进制编码为字符串
    public static String hexToString(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    //byte转化成string
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    //字符串转byte
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    // 转化字符串为十六进制编码
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    //int类型转byte
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    //字符转int
    public static int charToInt(char c) {
        int i;
        if (c >= '0' && c <= '9') {
            i = c - 48;
        } else {
            i = c - 87;
        }
        return i;
    }

    //处理开锁时间
    public static String getTime(String time) {
        int a, b, c, d, month_1, month_2, day_1, day_2, hour_1, hour_2, min_1, min_2;
        //年份
        a = Utils.charToInt(time.charAt(16));
        b = Utils.charToInt(time.charAt(17));
        c = Utils.charToInt(time.charAt(18));
        d = Utils.charToInt(time.charAt(19));
        a = a * 16 + b;
        c = c * 16 + d;

        //月份
        month_1 = Utils.charToInt(time.charAt(20));
        month_2 = Utils.charToInt(time.charAt(21));
        month_1 = month_1 * 16 + month_2;
        //天
        day_1 = Utils.charToInt(time.charAt(22));
        day_2 = Utils.charToInt(time.charAt(23));
        day_1 = day_1 * 16 + day_2;
        //时
        hour_1 = Utils.charToInt(time.charAt(24));
        hour_2 = Utils.charToInt(time.charAt(25));
        hour_1 = hour_1 * 16 + hour_2;
        //分
        min_1 = Utils.charToInt(time.charAt(26));
        min_2 = Utils.charToInt(time.charAt(27));
        min_1 = min_1 * 16 + min_2;

        String string = (a * 256 + c) + "年" + month_1 + "月" + day_1 + "日" + hour_1 + "时" + min_1 + "分";
        return string;
    }

}
