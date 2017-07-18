package com.example.kai.testandroidstudio;

/**
 * Created by himeri on 2017/07/10.
 */

import java.io.IOException;
import java.util.*;
import java.io.InputStream;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import org.androidannotations.annotations.EBean;
import android.support.annotation.NonNull;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class TensorFlowPositionEstimate extends Activity {
    private AssetManager assetMgr;
    private Context context;
    private Loading loading;
    private ImageView mapImageView;
    private DragViewListener dragMapViewLisener;
    private WifiManager wifiManager;
    private List<ScanResult> results;
    private BroadcastReceiver receiverWifi;
    private Timer mainTimer;
    private ScanWifiTimerTask ScanTask;
    private final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 0;


    /*--- calc object for position estimate ---*/
    private List<String> modelPath = new ArrayList<>();
    private List<String> imagePath = new ArrayList<>();
    private TensorFlowInferenceInterface areaModel;
    private TensorFlowInferenceInterface estimateModel;
    private float input[];  //入力データ
    private float output[]; //出力データ
    //wi-fi manager
    //mac rssi hash
    //modelの添え字とラベルの対応表のハッシュ
    private Position pos = new Position();
    private MapSize mSize = new MapSize();

    public static class WifiReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

        }
    }

    public TensorFlowPositionEstimate(AssetManager mgr, Context context,WifiManager wifiManager){
        this.assetMgr=mgr;
        this.context=context;
        this.loading = new Loading(context);
        this.mainTimer=new Timer();
        this.ScanTask = new ScanWifiTimerTask(this);

        /*--- これ以下はWifiの処理。TimerTaskを継承して作ったクラスと差し替える ---*/
        this.wifiManager = wifiManager;
        this.results=new ArrayList<ScanResult>();
        this.receiverWifi=new WifiReceiver();
        context.registerReceiver(receiverWifi,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        new Thread(new Runnable() {
            @Override
            public void run() {
                mainTimer.schedule(ScanTask,0,1000);
            }
        }).start();
    }

    private void initWifiModule(){
        if (wifiManager.isWifiEnabled() == false) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public void ScanWifi(){
        initWifiModule();
        wifiManager.startScan();
        results.clear();
        results=wifiManager.getScanResults();
        int size = results.size()-1;
        Log.d("wifi num",String.valueOf(size));
        while (size >= 0)
        {
            String ssid=results.get(size).SSID;
            String mac=results.get(size).BSSID;
            int rssi=results.get(size).level;
            int band=results.get(size).frequency;
            size--;
        }

    }

    public void searchArea(final String areaName,ImageView mapImageView){
        loading.show();
        registerDragListener(mapImageView);
        loadMapImage(areaName+".jpg");
        loading.close();

    }

    private void loadTensorFlowModel(final String modelName){
        estimateModel = new TensorFlowInferenceInterface(assetMgr,modelName+".pb");
    }

    private void registerDragListener(ImageView mapImageView){
        this.mapImageView=mapImageView;
        this.dragMapViewLisener = new DragViewListener(this.mapImageView);
        mapImageView.setOnTouchListener(this.dragMapViewLisener);
    }

    private void loadMapImage(final String ImageName) {
        try {
            InputStream istream = assetMgr.open(ImageName);
            Bitmap bitmap = BitmapFactory.decodeStream(istream);
            mapImageView.setImageBitmap(bitmap);
        }
        catch (IOException e){
            AlertDialog.Builder errDlg = new AlertDialog.Builder(context);
            errDlg.setTitle("No Image File");
            errDlg.setMessage("Sorry, can't find this Image file");
            errDlg.setPositiveButton(
                    "Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           finishAndRemoveTask();
                        }
                    });
            errDlg.setNegativeButton(
                    "Reload",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            loadMapImage(ImageName);
                        }
                    });
            // 表示
            errDlg.create().show();
        }
    }



}
