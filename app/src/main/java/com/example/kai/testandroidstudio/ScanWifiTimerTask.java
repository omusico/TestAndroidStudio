package com.example.kai.testandroidstudio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import 	android.os.Handler;
import android.util.Log;

import static android.os.Looper.getMainLooper;

/**
 * Created by Kai on 2017/07/18.
 */

public class ScanWifiTimerTask extends TimerTask {
    Context context;
    Handler handler;

    public ScanWifiTimerTask(Context context){
        this.context=context;
        this.handler= new Handler(getMainLooper());
    }
    @Override
    public void run() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ((TensorFlowPositionEstimate) context).ScanWifi();
                //取得したMACからエリアの有無を推定
                //if(エリアが変わった)
                    //エリア推定
                //位置推定
                //描画
            }
        });
    }
}
