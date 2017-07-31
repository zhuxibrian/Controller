package com.zx.agv.controller.util;

/**
 * Created by zx on 2017/7/1.
 */

public class ByteConvert {
    static public byte IntConvertByte(int num) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }

        byte res = b[3];
        return res;
    }
}
