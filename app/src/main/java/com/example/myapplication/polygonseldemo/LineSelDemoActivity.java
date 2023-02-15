package com.example.myapplication.polygonseldemo;

import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityLineSelDemoBinding;

import java.util.ArrayList;

public class LineSelDemoActivity extends BaseActivity<ActivityLineSelDemoBinding> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding.alarmLine.post(() -> {
            ArrayList<VirtualFenceInfo.LineInfo> lineInfos = new ArrayList<>();


            /**
             * 0~10 * 100000 （x、y的范围都在这里里，具体对应View的 0 ~ 1 * height  or 0~1 * width）
             */
            VirtualFenceInfo.LineInfo lineInfo1 = new VirtualFenceInfo.LineInfo(2,
                    (int) (0 * 100000), (int) (1 * 100000),
                    (int) (2 * 100000), (int) (3 * 100000));

            VirtualFenceInfo.LineInfo lineInfo2 = new VirtualFenceInfo.LineInfo(3,
                    (int) (4 * 100000), (int) (5 * 100000),
                    (int) (6 * 100000), (int) (7 * 100000));

            VirtualFenceInfo.LineInfo lineInfo = new VirtualFenceInfo.LineInfo(1,
                    (int) (8 * 100000), (int) (9 * 100000),
                    (int) (10 * 100000), (int) (11 * 100000));


            lineInfos.add(lineInfo1);
            lineInfos.add(lineInfo2);
            lineInfos.add(lineInfo);


            binding.alarmLine.initData(lineInfos, true);

        });


    }

}