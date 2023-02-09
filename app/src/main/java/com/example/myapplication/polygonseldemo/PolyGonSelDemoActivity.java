package com.example.myapplication.polygonseldemo;

import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityPolyGonSelDemoBinding;

import java.util.ArrayList;

public class PolyGonSelDemoActivity extends BaseActivity<ActivityPolyGonSelDemoBinding> {

    boolean isDeleteMode = false;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding.btnChanMode.setOnClickListener(view -> {
//            nextState();
//        });

//        binding.virtualFenceView.initData(new ArrayList<VirtualFenceInfo.PointInfo>() {{
////            VirtualFenceInfo.PointInfo pointInfo = new VirtualFenceInfo.PointInfo();
////            VirtualFenceInfo.PointInfo pointInfo1 = new VirtualFenceInfo.PointInfo();
////            VirtualFenceInfo.PointInfo pointInfo2 = new VirtualFenceInfo.PointInfo();
////            VirtualFenceInfo.PointInfo pointInfo3 = new VirtualFenceInfo.PointInfo();
////            VirtualFenceInfo.PointInfo pointInfo4 = new VirtualFenceInfo.PointInfo();
////            VirtualFenceInfo.PointInfo pointInfo5 = new VirtualFenceInfo.PointInfo();
////            VirtualFenceInfo.PointInfo pointInfo6 = new VirtualFenceInfo.PointInfo();
////
////            pointInfo.setPointX(10);
////            pointInfo1.setPointX(110);
////            pointInfo2.setPointX(120);
////            pointInfo3.setPointX(130);
////            pointInfo4.setPointX(140);
////            pointInfo5.setPointX(150);
////            pointInfo6.setPointX(160);
////
////            pointInfo.setPointY(10);
////            pointInfo1.setPointY(110);
////            pointInfo2.setPointY(120);
////            pointInfo3.setPointY(130);
////            pointInfo4.setPointY(140);
////            pointInfo5.setPointY(150);
////            pointInfo6.setPointY(160);
////
////            pointInfo.setPointId(0);
////            pointInfo1.setPointId(1);
////            pointInfo2.setPointId(2);
////            pointInfo3.setPointId(3);
////            pointInfo4.setPointId(4);
////            pointInfo5.setPointId(5);
////            pointInfo6.setPointId(6);
////
////            add(pointInfo);
////            add(pointInfo1);
////            add(pointInfo2);
////            add(pointInfo3);
////            add(pointInfo4);
////            add(pointInfo5);
////            add(pointInfo6);
//        }}, true);
    }

//    // view -> edit -> delete -> view
//    private void nextState() {
//        if (!isDeleteMode && !isEditMode) {
//            isEditMode = true;
//            binding.btnChanMode.setText("编辑模式");
//            binding.polyView.setEditMode(true);
//        } else if (isEditMode && !isDeleteMode) {
//            binding.btnChanMode.setText("删除模式");
//            isEditMode = false;
//            isDeleteMode = true;
//            binding.polyView.setEditMode(false);
//            binding.polyView.setDeleteMode(true);
//        } else if (!isEditMode && isDeleteMode) {
//            binding.btnChanMode.setText("观察模式");
//            isEditMode = false;
//            isDeleteMode = false;
//            binding.polyView.setEditMode(false);
//            binding.polyView.setDeleteMode(false);
//        }
//    }
}