package com.example.myapplication.polygonseldemo;

import org.junit.Test;

public class PolyGonSelectViewTest {

    @Test
    public void test() {

        float v1 = (float) calPointLineDis(100, 100, 500, 300, 300, 700);
        float v2 = pointToLine(100, 100, 500, 300, 300, 700);
        System.out.println("res=" + v1);
        System.out.println("res=" + v2);
    }

    private float calPointDis(float x1, float y1, float x2, float y2) {
        return (float) (Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2)));
    }

    private double calPointLineDis(float x0, float y0, float x1, float y1, float x2, float y2) {
        double space = 0;
        double a, b, c;
        a = calPointDis(x1, y1, x2, y2);// 线段的长度
        b = calPointDis(x1, y1, x0, y0);// (x1,y1)到点的距离
        c = calPointDis(x2, y2, x0, y0);// (x2,y2)到点的距离
        if (c <= 0.000001 || b <= 0.000001) {
            space = 0;
            return space;
        }
        if (a <= 0.000001) {
            space = b;
            return space;
        }
        if (c * c >= a * a + b * b) {
            space = b;
            return space;
        }
        if (b * b >= a * a + c * c) {
            space = c;
            return space;
        }
        double p = (a + b + c) / 2;// 半周长
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
        space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
        return space;
    }


    private float pointToLine(float x0, float y0, float x1, float y1, float x2, float y2) {
        float a = Math.abs(x1 - x2);
        float b = Math.abs(y1 - y2);
        float c = x2 * y1 - x1 * y2;

        return (float) (Math.abs(a * x0  + b * y0 + c) / Math.sqrt(a * a + b * b));
    }

}