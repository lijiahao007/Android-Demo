package com.example.myapplication.polygonseldemo;

public class AlarmLineInfo {
    //起点在上终点在下
    static public final int ORIENTATION_BOTHWAY = 3;
    static public final int ORIENTATION_RIGHT = 1;
    static public final int ORIENTATION_LEFT = 2;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private int orientation = ORIENTATION_BOTHWAY;
    private boolean isEdit = false;

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    @Override
    public String toString() {
        return "AlarmLineInfo{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                ", orientation=" + orientation +
                ", isEdit=" + isEdit +
                '}';
    }
}
