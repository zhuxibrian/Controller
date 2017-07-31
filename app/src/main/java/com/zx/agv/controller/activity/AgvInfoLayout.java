package com.zx.agv.controller.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.pl.wheelview.WheelView;
import com.zx.agv.controller.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/8.
 */

public class AgvInfoLayout extends LinearLayout {

    private WheelView mAGVPicker;
    private WheelView mLineNoPicker;
    private WheelView mSpeedPicker;
    private int mCurrAGVIndex = -1;
    private int mCurrLineIndex = -1;
    private int mCurrSpeedIndex = -1;
    private AreaDataUtil mAreaDataUtil;
    private ArrayList<String> mProvinceList = new ArrayList<>();

    public AgvInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAreaInfo();
    }

    public AgvInfoLayout(Context context) {
        this(context, null);
    }

    private void getAreaInfo() {
        mAreaDataUtil = new AreaDataUtil(getContext());
        mProvinceList = mAreaDataUtil.getProvinces();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_city_picker, this);

        mAGVPicker = (WheelView) findViewById(R.id.province_wv);
        mLineNoPicker = (WheelView) findViewById(R.id.city_wv);
        mSpeedPicker = (WheelView) findViewById(R.id.speed_wv);
        mAGVPicker.setData(mProvinceList);
        mAGVPicker.setDefault(0);
        ArrayList<String> list = new ArrayList<String>();
        list.add("不变");
        list.add("低速");
        list.add("中速");
        list.add("高速");
        list.add("高高速");

        mSpeedPicker.setData(list);
        mSpeedPicker.setDefault(2);

        String defaultProvince = mProvinceList.get(0);
        mLineNoPicker.setData(mAreaDataUtil.getCityByProvince(defaultProvince));
        mLineNoPicker.setDefault(0);

        mAGVPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (mCurrAGVIndex != id) {
                    mCurrAGVIndex = id;
                    String selectProvince = mAGVPicker.getSelectedText();
                    if (selectProvince == null || selectProvince.equals(""))
                        return;

                    // get city names by province
                    ArrayList<String> city = mAreaDataUtil.getCityByProvince(mProvinceList.get(id));
                    if (city.size() == 0) {
                        return;
                    }

                    mLineNoPicker.setData(city);

                    if (city.size() > 1) {
                        //if city is more than one,show start index == 1
                        mLineNoPicker.setDefault(1);
                    } else {
                        mLineNoPicker.setDefault(0);
                    }
                }

            }

            @Override
            public void selecting(int id, String text) {
                Log.e("mAGVPicker selecting", "id=" + id + ", text=" + text);
            }
        });

        mLineNoPicker.setOnSelectListener(new WheelView.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (mCurrLineIndex != id) {
                    mCurrLineIndex = id;
                    String selectCity = mLineNoPicker.getSelectedText();
                    if (selectCity == null || selectCity.equals(""))
                        return;
                    int lastIndex = Integer.valueOf(mLineNoPicker.getListSize());
                    if (id > lastIndex) {
                        mLineNoPicker.setDefault(lastIndex - 1);
                    }
                }
            }

            @Override
            public void selecting(int id, String text) {
                Log.e("mLineNoPicker selecting", "id=" + id + ", text=" + text);
            }
        });
        mSpeedPicker.setOnSelectListener(new WheelView.OnSelectListener()

        {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (mCurrSpeedIndex != id) {
                    mCurrSpeedIndex = id;
                    String selectSpeed = mSpeedPicker.getSelectedText();
                    if (selectSpeed == null || selectSpeed.equals(""))
                        return;
                    int lastIndex = Integer.valueOf(mSpeedPicker.getListSize());
                    if (id > lastIndex) {
                        mSpeedPicker.setDefault(lastIndex - 1);
                    }
                }
            }

            @Override
            public void selecting(int id, String text) {
                Log.e("mSpeedPicker selecting", "id=" + id + ", text=" + text);
            }
        });
    }
    public String getAGVStr() {
        if (mAGVPicker == null) {
            return null;
        }
        return mAGVPicker.getSelectedText();
    }

    public String getLineNoStr() {
        if (mLineNoPicker == null) {
            return null;
        }
        return mLineNoPicker.getSelectedText();
    }

    public String getSpeedStr() {
        if (mSpeedPicker == null) {
            return null;
        }
        return mSpeedPicker.getSelectedText();
    }
    public int getAGVIndex() {
        return mAGVPicker.getSelected();
    }

    public int getLineNoIndex() {

        return mLineNoPicker.getSelected();
    }

    public int getSpeedIndex() {
        return mSpeedPicker.getSelected();
    }
    public void setLineNoIndex(int num) {
        mLineNoPicker.setDefault(num);
    }
    public void setSpeedIndex(int num) {
        mSpeedPicker.setDefault(num);
    }
    public void setWheelViewItemNumber(int number) {
        mAGVPicker.setItemNumber(number);
        mLineNoPicker.setItemNumber(number);
        mSpeedPicker.setItemNumber(number);
    }
}
