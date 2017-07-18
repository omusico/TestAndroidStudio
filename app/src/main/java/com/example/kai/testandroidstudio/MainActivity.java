package com.example.kai.testandroidstudio;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.os.Looper.getMainLooper;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @ViewById(R.id.container)
    ViewPager mViewPager;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.fab)
    FloatingActionButton fab;
    TensorFlowPositionEstimate test;
    private final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 0;

    //static { System.loadLibrary("tensorflow_inference"); }

    @AfterViews
    void initMainActivity() {
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        test = new TensorFlowPositionEstimate( getAssets(), this,(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE) );
    }

    public static class WifiReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Permissonの確認 Android6以上は必要
        if( ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //許可された時の処理を記述
            //現段階では不要
        } else {
            //許可されなかった時の処理
            //エラーダイアログを表示して強制終了
            AlertDialog.Builder errDlg = new AlertDialog.Builder(this);
            errDlg.setTitle("Please,Enable Wifi");
            errDlg.setMessage("Sorry, don't enable wifi_permisson");
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
                            finishAndRemoveTask();
                        }
                    });
            // 表示
            errDlg.create().show();
        }
    }



/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        test = new TensorFlowPositionEstimate( getAssets(), (ImageView) findViewById(R.id.imageView), this );

        /*
        TensorFlowInferenceInterface inferenceInterface;
        inferenceInterface = new TensorFlowInferenceInterface(getAssets(),"test_model.pb");    //assetフォルダとassetフォルダ内のGraphNameが引数
        float array[] = new float[205];
        float pos[] = new float[68];
        Arrays.fill(array,0);
        Arrays.fill(pos,0);

        org.tensorflow.Operation operation = inferenceInterface.graphOperation(output_node[0]);
        int num = (int) operation.output(0).shape().size(0);
        Log.d("outputSize","Read output size:"+num);
        // input rssi value by hand
        Log.d("input array","handring");

        //pos()
        array[0]=0.39f;
        array[1]=0.39f;
        array[2]=0.39f;
        array[3]=0.38f;
        array[4]=0.33f;
        array[5]=0.33f;
        array[6]=0.22f;
        array[7]=0.22f;
        array[8]=0.33f;
        array[9]=0.33f;
        array[10]=0.24f;
        array[11]=0.22f;
        array[12]=0.22f;
        array[13]=0.24f;
        array[14]=0.30f;
        array[15]=0.23f;
        array[19]=0.38f;
        array[20]=0.37f;
        array[32]=0.16f;
        array[33]=0.35f;
        array[34]=0.36f;
        array[35]=0.28f;
        array[41]=0.33f;
        array[42]=0.31f;
        array[43]=0.31f;
        array[44]=0.30f;
        array[76]=0.36f;
        array[77]=0.35f;
        array[78]=0.75f;
        array[80]=0.24f;
        array[81]=0.25f;
        array[82]=0.26f;
        array[100]=0.32f;
        array[101]=0.32f;
        array[102]=0.32f;
        array[103]=0.32f;
        array[104]=0.27f;
        array[105]=0.16f;
        array[106]=0.21f;
        array[107]=0.21f;
        array[108]=0.19f;
        array[109]=0.18f;
        array[111]=0.14f;
        array[113]=0.20f;
        array[119]=0.22f;
        array[120]=0.21f;
        array[121]=0.16f;
        array[122]=0.17f;


        for(int i=0;i<array.length;i++) {
            array[i] = array[i] * 100 - 100;
        }
        for(int i=0;i<array.length;i++) {
            Log.d(String.valueOf(i),String.valueOf(array[i]));
        }


        long start = System.currentTimeMillis();
        inferenceInterface.feed("dense1_input_1",array, 1,205 );
        inferenceInterface.run(new String[]{"output_node0"},false);
        inferenceInterface.fetch("output_node0",pos);

        double sum=0;
        for(int i=0;i<pos.length;i++){
            sum += pos[i];
            Log.d(String.valueOf(i),String.valueOf(pos[i]));
        }
        Log.d("maxIndex",String.valueOf(utility.arrayMaxIndex(pos)));
        Log.d("sum",String.valueOf(sum));

        long end = System.currentTimeMillis();
        Log.d(getClass().getName(), "measure: " + (end - start));
        */

    //}

    @Click(R.id.button)
    void loadImage_HimejiGF(){
        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_content);
        layout.removeAllViews();
        getLayoutInflater().inflate(R.layout.map_view2,layout);
        test.searchArea("001",(ImageView) findViewById(R.id.imageView2));
    }

    @Click(R.id.button2)
    void loadImage_UnivHyogo(){
        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_content);
        layout.removeAllViews();
        getLayoutInflater().inflate(R.layout.map_view2,layout);
        test.searchArea("002",(ImageView) findViewById(R.id.imageView2));

    }

    @Click(R.id.button3)
    void loadImage_Plaza_ifo_No6(){
        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_content);
        layout.removeAllViews();
        getLayoutInflater().inflate(R.layout.map_view2,layout);
        test.searchArea("003",(ImageView) findViewById(R.id.imageView2));
    }

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
