package com.example.kai.testandroidstudio;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Kai on 2017/07/13.
 */

public class Loading {
    Context mContext;
    ProgressDialog mProgressDialog;

    public Loading(Context context){
        mContext = context;
        mProgressDialog = new ProgressDialog(context);
    }
    public void show(){
        mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.map_search_view);
        mProgressDialog.setCancelable(false);
    }
    public void close(){
        mProgressDialog.dismiss();
    }
}
