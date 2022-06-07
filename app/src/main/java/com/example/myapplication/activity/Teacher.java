package com.example.myapplication.activity;

import android.os.Parcel;
import android.os.Parcelable;

public class Teacher implements Parcelable {

    String name;
    int age;
    double height;

    //注意： createFromParcel中读取数据的顺序，需要和writeToParcel写入数据的顺序一致，否则会数据出粗


    public Teacher(String name, int age, double height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }

    protected Teacher(Parcel in) {
        name = in.readString();
        age = in.readInt();
        height = in.readDouble();
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
//            从Parcel容器中读取数据，封装到Parcelable中返回到逻辑层
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
//            方法是供外部类反序列化本类数组使用。
            return new Teacher[size];
        }
    };

    @Override
    public int describeContents() {// 内容接口描述
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        将数据写入外部提供的Parcel中，即打包需要传递的数据到Parcel中，其中打包的顺序需要按照数据的声明顺序，
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeDouble(height);
    }
}
