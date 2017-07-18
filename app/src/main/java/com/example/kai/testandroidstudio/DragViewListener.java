package com.example.kai.testandroidstudio;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Kai on 2017/07/17.
 * 以下のサイトのクラスを利用させて頂いてます。
 * http://kurukurupapa.hatenablog.com/entry/20120422/1335098811
 */

public class DragViewListener implements View.OnTouchListener {
    // ドラッグ対象のView
    private ImageView dragView;
    // ドラッグ中に移動量を取得するための変数
    private int oldX;
    private int oldY;

    public DragViewListener(ImageView dragView) {
        this.dragView = dragView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // タッチしている位置取得
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        if(oldX == 0 && oldY == 0) {

            oldX = x;
            oldY = y;

        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 今回イベントでのView移動先の位置
                int left = dragView.getLeft() + (x - oldX);
                int top = dragView.getTop() + (y - oldY);
                // Viewを移動する
                dragView.layout(left, top, left + dragView.getWidth(), top
                        + dragView.getHeight());
                break;
        }

        // 今回のタッチ位置を保持
        oldX = x;
        oldY = y;
        // イベント処理完了
        return true;
    }
}
