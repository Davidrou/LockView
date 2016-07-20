package com.example.kaiyicky.myapplication;

public class Point {
    private int ox;
    private int oy;
    private float r;
    private boolean isSelected;
    public int getOx() {
        return ox;
    }
    public void setOx(int ox) {
        this.ox = ox;
    }
    public int getOy() {
        return oy;
    }
    public void setOy(int oy) {
        this.oy = oy;
    }
    public float getR() {
        return r;
    }
    public void setR(float r) {
        this.r = r;
    }
    public boolean getSelectedStatus(){
        return isSelected;
    }

    public void setSelectedStatus(boolean isSeleted){
        this.isSelected =isSeleted;
    }
}