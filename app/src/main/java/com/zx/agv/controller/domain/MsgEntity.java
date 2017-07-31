package com.zx.agv.controller.domain;

import android.os.Handler;

/**
 * 存储发送socket的类，包含要发送的BufTest，以及对应的返回结果的Handler
 * Created by zx on 2017/6/18.
 */

public class MsgEntity {
    //要发送的消息
    private byte [] bytes;
    //错误处理的handler
    private Handler mHandler;

    public MsgEntity( byte [] bytes, Handler handler)
    {
        this.bytes = bytes;
        mHandler = handler;
    }

    public byte []  getBytes()
    {
        return this.bytes;
    }

    public Handler getHandler()
    {
        return mHandler;
    }
}
