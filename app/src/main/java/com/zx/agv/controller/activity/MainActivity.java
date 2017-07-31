package com.zx.agv.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.zx.agv.controller.R;
import com.zx.agv.controller.databinding.ActivityMainBinding;
import com.zx.agv.controller.socket.Const;
import com.zx.agv.controller.socket.NetManager;
import com.zx.agv.controller.socket.SocketThreadManager;
import com.zx.agv.controller.viewModel.MainViewModel;


public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver bcReceiver;
    SharedPreferences preferences;

    public static Context s_context;
    MainViewModel mainViewModel;
    private AgvInfoLayout agvInfoPicker;
    private EditText etIp;
    private EditText etPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityMainBinding activityMainBinding
                = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainViewModel = new MainViewModel(this, activityMainBinding);

        agvInfoPicker = (AgvInfoLayout) findViewById(R.id.agvInfoPicker);

        agvInfoPicker.setWheelViewItemNumber(3);


        preferences = this.getSharedPreferences("network", MODE_APPEND);
        Const.SOCKET_SERVER = preferences.getString("ip", "192.168.1.112");
        Const.SOCKET_PORT = preferences.getInt("port", 8080);

        NetManager.instance().init(this);
        s_context = this;
        new Thread(networkTask).start();
        regBroadcast();

//        handler = new Handler( getMainLooper() )
//        {
//            @Override
//            public void handleMessage(Message msg)
//            {
//                switch(msg.what){
//
//                    case 0:
//                        showMsg("发送socket失败");
//                        break;
//
//                    case 1:
//
//                        showMsg("发送socket成功");
//                        break;
//                }
//            }
//        };

    }

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            SocketThreadManager.sharedInstance();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //设置IP信息
            showDialog();
            return true;
        }
        if (id == R.id.refresh) {
            //发送刷新请求
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void regBroadcast() {
        bcReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final byte[] value = intent.getByteArrayExtra("response");
                if (value.length >= 7) {
                    int firstIndex = 0;
                    for (int i = 0; i < value.length; i++) {
                        if (value[i] == 0x1f) {
                            firstIndex = i;
                            break;
                        }
                    }
                    byte byteLine = value[firstIndex + 4];
                    byte byteSpeed = value[firstIndex + 5];

//                    mainViewModel.setLineAndSpeed(byteLine, byteSpeed);

                }
            }
        };

        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(Const.BC);
        registerReceiver(bcReceiver, intentToReceiveFilter);
    }

    public void showMsg(String str) {

        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    private void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置网络参数");
        builder.setView(view);
        SharedPreferences preferences = getSharedPreferences("network", Context.MODE_APPEND);
        etIp = (EditText) view.findViewById(R.id.ip);
        etPort = (EditText) view.findViewById(R.id.port);
        etIp.setText(preferences.getString("ip", "192.168.1.222"));
        etPort.setText(String.valueOf(preferences.getInt("port", 8080)));
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "点击了确认按钮", Toast.LENGTH_SHORT).show();

                SharedPreferences preferences = getSharedPreferences("network", Context.MODE_APPEND);
                SharedPreferences.Editor editor = preferences.edit();
                String ip = etIp.getText().toString();
                int port = Integer.parseInt(etPort.getText().toString());
                editor.putString("ip", ip);
                editor.putInt("port", port);
                editor.commit();
                Const.SOCKET_SERVER = preferences.getString("ip", "192.168.1.222");
                Const.SOCKET_PORT = preferences.getInt("port", 8080);

                SocketThreadManager.releaseInstance();
                new Thread(networkTask).start();
                regBroadcast();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
