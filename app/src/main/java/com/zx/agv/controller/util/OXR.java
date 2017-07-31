package com.zx.agv.controller.util;

/**
 * Created by zx on 2017/7/1.
 */

public class OXR {
    static public byte countOXR(byte[] bytes) {
        byte oxr = 0x00;
        for(int i=1; i<bytes.length-1; i++) {
            oxr ^= bytes[i];
        }
        return oxr;
    }

    static public boolean checkOXR(byte[] bytes) {
        int length = bytes.length;
        byte oxr = 0x00;
        for(int i=1; i<bytes.length-1; i++) {
            oxr ^= bytes[i];
        }

        return oxr==bytes[length-1] ? true : false;
    }
}
