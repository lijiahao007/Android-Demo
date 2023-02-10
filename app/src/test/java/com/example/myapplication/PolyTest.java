package com.example.myapplication;

import org.junit.Test;

public class PolyTest {
    @Test
    public void test1() {
        long begin = System.currentTimeMillis();
        double res = 0;
        for (int i = 0; i < 100000; i++) {
//            res = calPointLineDis(100, 100, 500, 300, 300, 700);
            res = calPointLineDis(100, 100, 100, 500, 300, 400);
        }
        long end = System.currentTimeMillis();
        System.out.println("test1 -- " + (end - begin) + "ms   " + res );

    }

    @Test
    public void test2() {
        long begin = System.currentTimeMillis();
        double res = 0;
        for (int i = 0; i < 100000; i++) {
//            res = pointToLine(100, 100, 500, 300, 300, 700);
            res = pointToLine(100, 100, 100, 500, 300, 400);

        }
        long end = System.currentTimeMillis();
        System.out.println("test2 -- " + (end - begin) + "ms   " + res  );
    }

    @Test
    public void test3() {
        long begin = System.currentTimeMillis();
        double res = 0;
        for (int i = 0; i < 100000; i++) {
//            res = pointToLine1(100, 100, 500, 300, 300, 700);
            res = pointToLine1(100, 100, 100, 500, 300, 400);
        }
        long end = System.currentTimeMillis();
        System.out.println("test2 -- " + (end - begin) + "ms  " + res);
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

        return (float) (Math.abs(a * x0 + b * y0 + c) / Math.sqrt(a * a + b * b));
    }


    private double pointToLine1(float x, float y, float x1, float y1, float x2, float y2) {
        double cross = (x2 - x1) * (x - x1) + (y2 - y1) * (y - y1);
        if (cross <= 0)
            return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));

        double d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        if (cross >= d2) return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));

        double r = cross / d2;
        double px = x1 + (x2 - x1) * r;
        double py = y1 + (y2 - y1) * r;
        return Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
    }

}
