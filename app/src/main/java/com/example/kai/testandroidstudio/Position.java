package com.example.kai.testandroidstudio;

/**
 * Created by himeri on 2017/07/11.
 */

public class Position{
    int x;
    int y;

    Position(int x,int y){
        this.x=x;
        this.y=y;

    }
    Position(){
        this(0,0);
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setX(int x1){
        this.x=x1;
    }
    public void setY(int y1){
        this.y=y1;
    }
}
