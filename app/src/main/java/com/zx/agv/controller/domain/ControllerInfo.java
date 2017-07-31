package com.zx.agv.controller.domain;

import android.databinding.ObservableInt;

/**
 * Created by zx on 2017/6/24.
 */

public class ControllerInfo {

    public int agvId;

    public int speed;

    public int lineId;

    public int getAgvId() {
        return agvId;
    }

    public void setAgvId(int agvId) {
        this.agvId = agvId + 1;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }
}
