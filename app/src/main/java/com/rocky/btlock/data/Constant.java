package com.rocky.btlock.data;

/**
 * Created by Rocky on 2016/5/30.
 */
public class Constant {

    //全局常量
    //结束字符
    public static final String END_CHAR = "0d";
    //字头
    public static final String HEAD_CHAR = "aa";
    //蓝牙芯片终端（以下两个字段用于识别发送和识别信息对象）
    public static final String SEVER_BLUETOOTH = "01";
    //客户端
    public static final String APP = "02";
    //功能
    //发送本机蓝牙地址
    public static final String SEND_ADDRESS = "01";
    //创建管理员
    public static final String CREAT_ADMIN = "02";
    public static final String CREAT_ADMIN_SUCCESS = "03";
    public static final String CREAT_ADMIN_FAIL = "04";
    //校验用户登陆信息
    public static final String LOGIN = "05";
    //管理员登陆
    public static final String LOGIN_ADMIN = "07";
    //普通用户登陆
    public static final String LOGIN_USERS = "06";
    //用户登录失败
    public static final String LOGIN_FAIL_NUM = "08";
    //开锁
    public static final String LOCK_OPEN_NUM = "09";
    //开锁成功
    public static final String LOCK_OPEN_SUCCESS_NUM = "0a";
    //查看记录
    public static final String RECORD_REQUEST_NUM = "0b";
    //获取存储记录
    public static final String GET_RECORD_NUM = "0c";
    //记录发送完毕
    public static final String RECORD_END_NUM = "0d";
    //修改触摸屏密码
    public static final String CHANGE_PASSWORD_NUM = "0e";
    //修改触摸屏密码成功
    public static final String CHANGE_PASSWORD_SUCCESS_NUM = "0f";
    //新增用户
    public static final String NEW_USER_NUM = "10";
    //新增用户成功
    public static final String NEW_USER_SUCCESS_NUM = "11";
    //删除用户
    public static final String DEL_USER_NUM = "12";
    //删除用户成功
    public static final String DEL_SUCCESS_NUM = "14";
    //增加指纹
    public static final String ADD_NEW_FINGER_NUM = "15";
    //增加指纹成功
    public static final String ADD_FINGER_SUCCESS_NUM = "16";
    //增加指纹失败
    public static final String ADD_FINGER_FAIL_NUM = "17";
    //没有发送地址
    public static final String NO_BLUE_ADDRESS = "aa0518020d";

    //固定返回指令
    //创建管理员成功和失败
    public static final String CREAT_ADMIN_CALLBACK_SUCCESS = HEAD_CHAR + "05" + CREAT_ADMIN_SUCCESS + APP + END_CHAR;
    public static final String CREAT_ADMIN_CALLBACK_FAIL = HEAD_CHAR + "05" + CREAT_ADMIN_FAIL + APP + END_CHAR;
    //管理员登陆成功
    public static final String LOGIN_ADMIN_SUCCESS = HEAD_CHAR + "05" + LOGIN_ADMIN + APP + END_CHAR;
    //普通用户登录成功
    public static final String LOGIN_USER_SUCCESS = HEAD_CHAR + "05" + LOGIN_USERS + APP + END_CHAR;
    //用户登录失败
    public static final String LOGIN_FAIL = HEAD_CHAR + "05" + LOGIN_FAIL_NUM + APP + END_CHAR;
    //开锁指令
    public static final String OPEN_LOCK = HEAD_CHAR + "05" + LOCK_OPEN_NUM + SEVER_BLUETOOTH + END_CHAR;
    //开锁成功
    public static final String OPEN_LOCK_SUCCESS = HEAD_CHAR + "05" + LOCK_OPEN_SUCCESS_NUM + APP + END_CHAR;
    //查看记录
    public static final String RECORD_REQUEST = HEAD_CHAR + "05" + RECORD_REQUEST_NUM + SEVER_BLUETOOTH + END_CHAR;
    //记录发送完毕
    public static final String RECORE_END = HEAD_CHAR + "05" + RECORD_END_NUM + APP + END_CHAR;
    //修改触摸屏密码成功
    public static final String CHANGE_PASSWORD_SUCCESS = HEAD_CHAR + "05" + CHANGE_PASSWORD_SUCCESS_NUM + APP + END_CHAR;
    //新增用户成功
    public static final String ADD_USERS_SUCCESS = HEAD_CHAR + "05" + NEW_USER_SUCCESS_NUM + APP + END_CHAR;
    //删除用户成功
    public static final String DEL_USER_SUCCESS = HEAD_CHAR + "05" + DEL_SUCCESS_NUM + APP + END_CHAR;
    //添加指纹成功
    public static final String ADD_FINGER_SUCCESS = HEAD_CHAR + "05" + ADD_FINGER_SUCCESS_NUM + APP + END_CHAR;
    //添加指纹失败
    public static final String ADD_FINGER_FAIL = HEAD_CHAR + "05" + ADD_FINGER_FAIL_NUM + APP + END_CHAR;
    //查看用户
    public static final String GET_USER_REQUEST = "aa0519010d";
    //查看用户
    public static final String USER_END = "aa0521020d";
    //增加用户失败
    public static final String ADD_USE_FAIL = "aa0512020d";
    //时间校验成功
    public static final String TIME_SUCESS = "aa0523020d";

    public static final String ADMIN_DEL_FAIL = "aa0524020d";
}
