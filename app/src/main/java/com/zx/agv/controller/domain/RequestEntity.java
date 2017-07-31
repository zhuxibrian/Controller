package com.zx.agv.controller.domain;

/**
 * Created by zx on 2017/7/1.
 */

public class RequestEntity {
    private byte [] bytes = {0x0C, 0x09, 0x01, (byte) 0xF5, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public void setByte(int index, byte value) {
        bytes[index] = value;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
