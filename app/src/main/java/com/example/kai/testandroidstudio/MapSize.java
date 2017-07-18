package com.example.kai.testandroidstudio;

/**
 * Created by himeri on 2017/07/11.
 */

public class MapSize {
    int mapWidth;
    int mapHeight;
    int mapMidWitdh;
    int mapMidHeight;

    double scale;

    MapSize(int mapHeight,int mapWidth,double scale) {
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.scale = scale;
        this.mapMidHeight=mapHeight/2;
        this.mapMidWitdh=mapWidth/2;
    }
    MapSize(int mapHeight,int mapWidth){
        this(mapHeight,mapWidth,0);
    }
    MapSize(){
        this(0,0,0);
    }
    public int getMapWidth(){
        return this.mapWidth;
    }
    public int getMapHeight(){
        return this.mapHeight;
    }
    public double getScale(){
        return this.scale;
    }
    public int getMapMidHeight() {
        return mapMidHeight;
    }
    public int getMapMidWitdh() {
        return mapMidWitdh;
    }
    public void setMapWidth(int mapWidth){
        this.mapWidth=mapWidth;
    }
    public void setMapHeight(int mapHeight){
        this.mapHeight=mapHeight;
    }
    public void setScale(double scale){
        this.scale=scale;
    }
}
