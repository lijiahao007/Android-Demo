package com.example.myapplication.polygonseldemo;

import android.content.Context;

import java.util.ArrayList;

public class VirtualFenceInfo {
    final static public int DETECT_SENSITIVITY_NOT = 0;
    final static public int DETECT_SENSITIVITY_LOW = 1;
    final static public int DETECT_SENSITIVITY_MIDDLE = 2;
    final static public int DETECT_SENSITIVITY_HIGH = 3;
    final static public int DETECT_SENSITIVITY_CLOSE = 10;

    final static public int AREA_DETECT_ENABLE_NONSUPPORT = 0;
    final static public int AREA_DETECT_ENABLE_OPEN = 1;
    final static public int AREA_DETECT_ENABLE_CLOSE = 10;

    final static public int AREA_LINE_ENABLE_NONSUPPORT = 0;
    final static public int AREA_LINE_ENABLE_OPEN = 1;
    final static public int AREA_LINE_ENABLE_CLOSE = 2;

    public static final int DETECT_MODE_NONSUPPORT = 0;
    public static final int DETECT_MODE_ALL_ALWAYS = 1;
    public static final int DETECT_MODE_TIME_TIMER = 2;

    public static final int AREA_ALARM_TYPE_IN = 1;
    public static final int AREA_ALARM_TYPE_OUT = 2;
    public static final int AREA_ALARM_TYPE_DUPLEX = 3;

    private int detectSensitivity;//检测灵敏度 0：无效/不支持 01：低，02：中， 03： 高，10：关闭
    private int lineDetectEnable;//越线检测使能： 0=不支持， 01=有 10=无
    private int lineCount;//线的数量
    private int areaDetectEnable;//区域检测使能： 0=不支持， 01=有 10=无
    private int areaPointCount;//区域检测顶点数量
    private int areaAlarmType;//区域检测报警类型：1=进入报警，2=离开报警， 3=全开（进入+离开）
    private int detectMode;//定时检测：0：无效/不支持 , 1：全天，2：自定义时间
    private int timerCount;//时间段的数量
    private ArrayList<TimerInfo> timerInfos = new ArrayList<>();//时间段
    private ArrayList<PointInfo> pointInfos = new ArrayList<>();
    private ArrayList<LineInfo> lineInfos = new ArrayList<>();

    public int getDetectSensitivity() {
        return detectSensitivity;
    }

    public void setDetectSensitivity(int detectSensitivity) {
        this.detectSensitivity = detectSensitivity;
    }

    public int getLineDetectEnable() {
        return lineDetectEnable;
    }

    public void setLineDetectEnable(int lineDetectEnable) {
        this.lineDetectEnable = lineDetectEnable;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public int getAreaDetectEnable() {
        return areaDetectEnable;
    }

    public void setAreaDetectEnable(int areaDetectEnable) {
        this.areaDetectEnable = areaDetectEnable;
    }

    public int getAreaPointCount() {
        return areaPointCount;
    }

    public void setAreaPointCount(int areaPointCount) {
        this.areaPointCount = areaPointCount;
    }

    public int getAreaAlarmType() {
        return areaAlarmType;
    }

    public void setAreaAlarmType(int areaAlarmType) {
        this.areaAlarmType = areaAlarmType;
    }

    public int getDetectMode() {
        return detectMode;
    }

    public void setDetectMode(int detectMode) {
        this.detectMode = detectMode;
    }

    public int getTimerCount() {
        return timerCount;
    }

    public void setTimerCount(int timerCount) {
        this.timerCount = timerCount;
    }

    public ArrayList<TimerInfo> getTimerInfos() {
        return timerInfos;
    }

    public void setTimerInfos(ArrayList<TimerInfo> timerInfos) {
        this.timerInfos = timerInfos;
    }

    public ArrayList<PointInfo> getPointInfos() {
        return pointInfos;
    }

    public void setPointInfos(ArrayList<PointInfo> pointInfos) {
        this.pointInfos = pointInfos;
    }

    public ArrayList<LineInfo> getLineInfos() {
        return lineInfos;
    }

    public void setLineInfos(ArrayList<LineInfo> lineInfos) {
        this.lineInfos = lineInfos;
    }

    @Override
    public String toString() {
        return "VirtualFenceInfo{" +
                "detectSensitivity=" + detectSensitivity +
                ", lineDetectEnable=" + lineDetectEnable +
                ", lineCount=" + lineCount +
                ", areaDetectEnable=" + areaDetectEnable +
                ", areaPointCount=" + areaPointCount +
                ", areaAlarmType=" + areaAlarmType +
                ", detectMode=" + detectMode +
                ", timerCount=" + timerCount +
                ", timerInfos=" + timerInfos +
                ", pointInfos=" + pointInfos +
                ", lineInfos=" + lineInfos +
                '}';
    }

    public static class LineInfo {
        private int alarmtype;//越线报警类型：1=正向压线，2=反向压线， 3=全开（正+反）
        private int startX;//线的起始点x坐标 （在图像宽上的比例，精度为0.00001）
        private int startY;//线的起始点y坐标 （在图像高上的比例，精度为0.00001）
        private int endX;//线的结束点x坐标（在图像宽上的比例，精度为0.00001）
        private int endY;//线的结束点y坐标（在图像高上的比例，精度为0.00001）

        public boolean isEquals(LineInfo lineInfo) {
            if (lineInfo == null) return false;
            if (alarmtype != lineInfo.getAlarmtype()) return false;
            if (startX != lineInfo.getStartX()) return false;
            if (startY != lineInfo.getStartY()) return false;
            if (endX != lineInfo.getEndX()) return false;
            if (endY != lineInfo.getEndY()) return false;
            return true;
        }

        public LineInfo() {
        }

        public LineInfo(int alarmtype, int startX, int startY, int endX, int endY) {
            this.alarmtype = alarmtype;
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getStartY() {
            return startY;
        }

        public void setStartY(int startY) {
            this.startY = startY;
        }

        public int getEndX() {
            return endX;
        }

        public void setEndX(int endX) {
            this.endX = endX;
        }

        public int getEndY() {
            return endY;
        }

        public void setEndY(int endY) {
            this.endY = endY;
        }

        public int getAlarmtype() {
            return alarmtype;
        }

        public void setAlarmtype(int alarmtype) {
            this.alarmtype = alarmtype;
        }

        @Override
        public String toString() {
            return "AlarmLineInfo{" +
                    "startX=" + startX +
                    ", startY=" + startY +
                    ", endX=" + endX +
                    ", endY=" + endY +
                    ", alarmtype=" + alarmtype +
                    '}';
        }
    }

    public static class PointInfo {
        private int pointId;//区域的顶点id，由顶点id升序依次连接相领两点成区域（最后一个点和第一个点连接）
        private int pointX;//区域顶点x坐标（在图像宽上的比例，进度为0.00001）
        private int pointY;//区域顶点y坐标（在图像宽上的比例，进度为0.00001）

        public int getPointId() {
            return pointId;
        }

        public void setPointId(int pointId) {
            this.pointId = pointId;
        }

        public int getPointX() {
            return pointX;
        }

        public void setPointX(int pointX) {
            this.pointX = pointX;
        }

        public int getPointY() {
            return pointY;
        }

        public void setPointY(int pointY) {
            this.pointY = pointY;
        }

        public boolean isEquals(PointInfo pointInfo) {
            if (pointInfo == null) return false;
            if (pointId != pointInfo.getPointId()) return false;
            if (pointX != pointInfo.getPointX()) return false;
            if (pointY != pointInfo.getPointY()) return false;
            return true;
        }

        @Override
        public String toString() {
            return "PointInfo{" +
                    "pointId=" + pointId +
                    ", pointX=" + pointX +
                    ", pointY=" + pointY +
                    '}';
        }
    }

    public static class TimerInfo {
        private int startH;     //哭声检测开始时间：时
        private int startM;     //哭声检测开始时间：分
        private int startS;     //哭声检测开始时间：秒
        private int endH;       //哭声检测结束时间：时
        private int endM;       //哭声检测结束时间：分
        private int endS;       //哭声检测结束时间：秒

        public TimerInfo() {
        }

        public TimerInfo(int startH, int startM, int startS, int endH, int endM, int endS) {
            this.startH = startH;
            this.startM = startM;
            this.startS = startS;
            this.endH = endH;
            this.endM = endM;
            this.endS = endS;
        }

        public int getStartH() {
            return startH;
        }

        public void setStartH(int startH) {
            this.startH = startH;
        }

        public int getStartM() {
            return startM;
        }

        public void setStartM(int startM) {
            this.startM = startM;
        }

        public int getStartS() {
            return startS;
        }

        public void setStartS(int startS) {
            this.startS = startS;
        }

        public int getEndH() {
            return endH;
        }

        public void setEndH(int endH) {
            this.endH = endH;
        }

        public int getEndM() {
            return endM;
        }

        public void setEndM(int endM) {
            this.endM = endM;
        }

        public int getEndS() {
            return endS;
        }

        public void setEndS(int endS) {
            this.endS = endS;
        }

        public boolean isEquals(TimerInfo timerInfo) {
            if (timerInfo == null) return false;
            if (startH != timerInfo.getStartH()) return false;
            if (startM != timerInfo.getStartM()) return false;
            if (startS != timerInfo.getStartS()) return false;
            if (endH != timerInfo.getEndH()) return false;
            if (endM != timerInfo.getEndM()) return false;
            if (endS != timerInfo.getEndS()) return false;
            return true;
        }

        public String getTimeText(Context context) {
            if (isTransgression()) {
                return getStartTimeText() + "-"
                        + getEndTimeText();
            } else {
                return getStartTimeText() + "-" + getEndTimeText();
            }
        }

        public boolean isTransgression() {
            if (getStartH() > getEndH()) {
                return true;
            } else if (getStartH() == getEndH() && getStartM() > getEndM()) {
                return true;
            }
            return false;
        }

        public String getStartTimeText() {
            return replenishZero(startH) + ":" + replenishZero(startM);
        }

        public String getEndTimeText() {
            return replenishZero(endH) + ":" + replenishZero(endM);
        }

        private String replenishZero(int temp) {
            return temp > 9 ? String.valueOf(temp) : "0" + temp;
        }

        @Override
        public String toString() {
            return "TimerInfo{" +
                    "startH=" + startH +
                    ", startM=" + startM +
                    ", startS=" + startS +
                    ", endH=" + endH +
                    ", endM=" + endM +
                    ", endS=" + endS +
                    '}';
        }
    }
}
