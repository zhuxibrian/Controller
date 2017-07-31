package com.zx.agv.controller.viewModel;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zx.agv.controller.databinding.ActivityMainBinding;
import com.zx.agv.controller.databinding.ContentMainBinding;
import com.zx.agv.controller.domain.ControllerInfo;
import com.zx.agv.controller.domain.RequestEntity;
import com.zx.agv.controller.socket.Const;
import com.zx.agv.controller.socket.SocketThreadManager;

import static com.zx.agv.controller.util.ByteConvert.IntConvertByte;
import static com.zx.agv.controller.util.OXR.countOXR;

/**
 * Created by zx on 2017/7/2.
 */

public class MainViewModel {
    private ActivityMainBinding activityMainBinding;
    private ContentMainBinding contentMainBinding;
    private AppCompatActivity activity;

    private ControllerInfo controllerInfo;
    Handler handler = null;

    public MainViewModel(AppCompatActivity activity, ActivityMainBinding activityMainBinding) {
        this.activity = activity;
        this.activityMainBinding = activityMainBinding;
        this.contentMainBinding = activityMainBinding.contentMain;
        init();
    }

    public void init() {
        activity.setSupportActionBar(activityMainBinding.toolbar);
//        activityMainBinding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        controllerInfo = new ControllerInfo();
        contentMainBinding.setViewModel(this);
//        contentMainBinding.lineId.setText("1");
    }


    public void getControlInfo() {
        controllerInfo.setAgvId(contentMainBinding.agvInfoPicker.getAGVIndex());
        controllerInfo.setLineId(contentMainBinding.agvInfoPicker.getLineNoIndex());
        controllerInfo.setSpeed(contentMainBinding.agvInfoPicker.getSpeedIndex());
    }


    public void setLineAndSpeed(byte byteLine, byte byteSpeed) {//字节是什么意思
        int line = byteLine;
        int speed = byteSpeed;
        contentMainBinding.agvInfoPicker.setLineNoIndex(line);
        contentMainBinding.agvInfoPicker.setSpeedIndex(speed);
    }
    public void onChangeInfoClicked(View v) {
        getControlInfo();
        Toast.makeText(activity, "更改状态", Toast.LENGTH_SHORT).show();
        RequestEntity request = new RequestEntity();
        request.setByte(4, IntConvertByte(controllerInfo.getAgvId()));
        request.setByte(5, Const.FALSE);
        request.setByte(6,Const.FALSE);
        request.setByte(7,IntConvertByte(controllerInfo.getSpeed()));
        request.setByte(8,IntConvertByte(controllerInfo.getLineId()));
        request.setByte(10, countOXR(request.getBytes()));
        SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);

    }
    public void onShouDongQianYinClick(View v) {
        getControlInfo();
        if (contentMainBinding.shouDongQianYin.isChecked()) {
            RequestEntity request = new RequestEntity();
            request.setByte(4, IntConvertByte(controllerInfo.getAgvId()));
            request.setByte(5, Const.SDQY);
            request.setByte(6,Const.TRUE);
            request.setByte(10, countOXR(request.getBytes()));
            SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);
            Toast.makeText(activity, "向上 按键按下", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "向下 按键按下", Toast.LENGTH_SHORT).show();
            RequestEntity request = new RequestEntity();
            request.setByte(4, IntConvertByte(controllerInfo.getAgvId()));
            request.setByte(5, Const.SDQY);
            request.setByte(6,Const.FALSE);
            request.setByte(10, countOXR(request.getBytes()));
            SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);
        }
    }

    public void onQuDongTiShengClick(View v) {
        getControlInfo();
        if (contentMainBinding.quDongTiSheng.isChecked()) {
            Toast.makeText(activity, "驱动提升 按键按下", Toast.LENGTH_SHORT).show();
            RequestEntity request = new RequestEntity();
            request.setByte(4, IntConvertByte(controllerInfo.getAgvId()));
            request.setByte(5, Const.QDTS);
            request.setByte(6,Const.TRUE);
            request.setByte(10, countOXR(request.getBytes()));
            SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);
        } else {
            Toast.makeText(activity, "驱动下降 按键按下", Toast.LENGTH_SHORT).show();
            RequestEntity request = new RequestEntity();
            request.setByte(4, IntConvertByte(controllerInfo.getAgvId()));
            request.setByte(5, Const.QDTS);
            request.setByte(6,Const.FALSE);
            request.setByte(10, countOXR(request.getBytes()));
            SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);
        }

    }

    public void onFangXiangQieHuanClick(View v) {
        getControlInfo();
        if (contentMainBinding.fangXiangQieHuan.isChecked()) {
            Toast.makeText(activity, "向前 按键按下", Toast.LENGTH_SHORT).show();
            RequestEntity request = new RequestEntity();
            request.setByte(4, IntConvertByte(controllerInfo.getAgvId()));
            request.setByte(5, Const.FXQH);
            request.setByte(6,Const.FALSE);
            request.setByte(10, countOXR(request.getBytes()));
            SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);
//            int curSpeed = controllerInfo.getSpeed();
//            int curLine = controllerInfo.getLineId();
//            if (curSpeed != 0) request.setByte(7, IntConvertByte(curSpeed));
//            if (curLine != 0) request.setByte(8, IntConvertByte(curLine));
//            request.setByte(10, countOXR(request.getBytes()));
//            SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);

        } else {
            Toast.makeText(activity, "向后 按键按下", Toast.LENGTH_SHORT).show();
            RequestEntity request = new RequestEntity();
            request.setByte(4, IntConvertByte(controllerInfo.getAgvId()));
            request.setByte(5, Const.FXQH);
            request.setByte(6,Const.TRUE);
            request.setByte(10, countOXR(request.getBytes()));
            SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);
        }
    }

    public void onStartClick(View v) {
        getControlInfo();
        Toast.makeText(activity, "开始 按键按下", Toast.LENGTH_SHORT).show();
        RequestEntity request = new RequestEntity();
        request.setByte(4, IntConvertByte(controllerInfo.getAgvId()));
        request.setByte(5, Const.START);
        request.setByte(6,Const.FALSE);
        request.setByte(10, countOXR(request.getBytes()));
        SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);
    }

    public void onStopClick(View v) {
        getControlInfo();
        Toast.makeText(activity, "停止 按键按下", Toast.LENGTH_SHORT).show();
        RequestEntity request = new RequestEntity();
        request.setByte(4, IntConvertByte(controllerInfo.getAgvId()));
        request.setByte(5, Const.STOP);
        request.setByte(6,Const.FALSE);
        request.setByte(10, countOXR(request.getBytes()));
        SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);
    }

    public void onResetClick(View v) {
        getControlInfo();
        Toast.makeText(activity, "复位 按键按下", Toast.LENGTH_SHORT).show();
        RequestEntity request = new RequestEntity();
        request.setByte(4, IntConvertByte(controllerInfo.getAgvId()));
        request.setByte(5, Const.RESET);
        request.setByte(6,Const.FALSE);
        request.setByte(10, countOXR(request.getBytes()));
        SocketThreadManager.sharedInstance().sendMsg(request.getBytes(), handler);
    }

}