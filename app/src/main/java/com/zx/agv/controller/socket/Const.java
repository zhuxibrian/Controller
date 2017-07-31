package com.zx.agv.controller.socket;

/**
 * Created by zx on 2017/6/18.
 */

public class Const {
    public static String SOCKET_SERVER = "192.168.199.157";

    public static int SOCKET_PORT = 8080;

    // 默认timeout 时间 60s
    public final static int SOCKET_TIMOUT = 60 * 1000;

    public final static int SOCKET_READ_TIMOUT = 15 * 1000;

    //如果没有连接无服务器。读线程的sleep时间
    public final static int SOCKET_SLEEP_SECOND = 3 ;

    //心跳包发送间隔时间
    public final static int SOCKET_HEART_SECOND = 10 ;

    public final static String BC = "BC";

    public final static byte TRUE = 0x01;
    public final static byte FALSE = 0x00;

    public final static byte SDQY = 0x01;
    public final static byte QDTS = 0x02;
    public final static byte FXQH = 0x03;

    public final static byte START = 0x04;
    public final static byte STOP = 0x05;
    public final static byte RESET = 0x06;
}
