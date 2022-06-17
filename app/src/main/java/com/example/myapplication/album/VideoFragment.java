package com.example.myapplication.album;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.album.adapter.MediaBeanAdapter;
import com.example.myapplication.album.bean.MediaBean;
import com.example.myapplication.album.bean.MediaBeanDBHelper;
import com.example.myapplication.album.bean.MediaType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class VideoFragment extends Fragment {

    private final static String TAG = "VideoFragment";
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private MediaBeanAdapter adapter;
    private MediaBeanDBHelper helper = null;
    private SQLiteDatabase database = null;
    private ArrayList<MediaBean> mediaBeans;
    private ContentResolver contentResolver;
    public static final String DELETE_REQUEST_KEY = "delete_request_key";

    public VideoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (helper == null) {
            helper = new MediaBeanDBHelper(getContext());
        }
        if (database == null) {
            database = helper.getWritableDatabase();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_photo, container, false);
        // 1. 初始化recyclerView
        recyclerView = root.findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new MediaBeanAdapter(this);
        recyclerView.setAdapter(adapter);
        Objects.requireNonNull(recyclerView.getItemAnimator()).setChangeDuration(0);

        // 2. 获取并设置数据
        ArrayList<MediaBean> mediaBeans = queryVideo();
        adapter.setList(mediaBeans);

        // 3. 设置分隔
        recyclerView.addItemDecoration(new MediaItemDecoration(requireContext()));

        // 4. 设置分行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean lastInDate = adapter.isLastInDate(position);
                if (lastInDate) {
                    int dateMediaSize = adapter.getDateMediaSize(position);
                    if (dateMediaSize % 3 == 1) {
                        return 3;
                    } else if (dateMediaSize % 3 == 2) {
                        return 2;
                    }
                }
                return 1;
            }
        });


        // 5. 监听FragmentResult，判断进出编辑模式
        getParentFragmentManager().setFragmentResultListener(AlbumActivity.EDIT_MODE_CHANGE, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.i(TAG, "onFragmentResult");

                if (requestKey.equals(AlbumActivity.EDIT_MODE_CHANGE)) {
                    boolean isEditMode = result.getBoolean(AlbumActivity.EDIT_MODE);
                    adapter.setEditMode(isEditMode);
                }
            }
        });


        // 6. 监听全选 和全不选
        getParentFragmentManager().setFragmentResultListener(AlbumActivity.SELECT_ALL_CHANGE, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(AlbumActivity.SELECT_ALL_CHANGE)) {
                    boolean isSelectAll = result.getBoolean(AlbumActivity.SELECT_ALL);
                    adapter.setSelectAll(isSelectAll);
                }
            }
        });

        // 7. 监听分享操作
        getParentFragmentManager().setFragmentResultListener(AlbumActivity.SHARE, getViewLifecycleOwner(), new FragmentResultListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                if (requestKey.equals(AlbumActivity.SHARE)) {
                    ArrayList<Uri> uris = (ArrayList<Uri>) adapter.getCheckedBean().stream().map(MediaBean::getUri).collect(Collectors.toList());
                    Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    intent.setType("video/mp4");
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                    requireContext().startActivity(intent);
                }
            }
        });

        // 7. 监听删除操作
        getParentFragmentManager().setFragmentResultListener(AlbumActivity.DELETE, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(AlbumActivity.DELETE)) {
                    List<MediaBean> checkedBean = adapter.getCheckedBean();
                    new MaterialAlertDialogBuilder(requireContext())
                            .setMessage("删除" + checkedBean.size() + "张图片")
                            .setPositiveButton("确定", (dialog, which) -> {
                                for (MediaBean bean : checkedBean) {
                                    int res = deleteMediaBean(bean);
                                }
                                adapter.deleteItems(checkedBean);
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                dialog.cancel();
                            })
                            .show();
                }
            }
        });
        return root;
    }

    @SuppressLint("Range")
    private ArrayList<MediaBean> queryVideo() {
        String selection = MediaBean.Entry.TYPE + " = \"VIDEO\"";
        String orderBy = MediaBean.Entry.DATE + " DESC";
        Cursor query = database.query(MediaBean.Entry.TABLE_NAME, MediaBean.Entry.ALL_COLUMN, selection, null, null, null, orderBy);
        ArrayList<MediaBean> res = new ArrayList<>();
        if (query != null && query.moveToFirst()) {
            do {
                 int id = query.getInt(query.getColumnIndex(MediaBean.Entry._ID));
                String uri = query.getString(query.getColumnIndex(MediaBean.Entry.URI));
                long timeStamp = query.getLong(query.getColumnIndex(MediaBean.Entry.TIMESTAMP));
                String fileName = query.getString(query.getColumnIndex(MediaBean.Entry.FILENAME));
                String date = query.getString(query.getColumnIndex(MediaBean.Entry.DATE));
                int duration = query.getInt(query.getColumnIndex(MediaBean.Entry.DURATION));
                MediaBean bean = new MediaBean(Uri.parse(uri), fileName, MediaType.VIDEO);
                bean.setId(id);
                bean.setTimestamp(timeStamp);
                bean.setDate(date);
                bean.setDuration(duration);

                File file = new File(bean.getFileName());
                if (!file.exists()) {
                    String where = MediaBean.Entry._ID + " = " + bean.id;
                    int delete = database.delete(MediaBean.Entry.TABLE_NAME, where, null);
                    Log.i("PhotoFragment", "文件已被删除：" + bean.toString() + "----已从数据库中删除" + delete);
                    continue;
                }
                res.add(bean);
                Log.i("VideoFragment", bean.toString());
            } while (query.moveToNext());
        }
        if (query != null) {
            query.close();
        }
        return res;
    }

    private int deleteMediaBean(MediaBean bean) {
        String where = MediaBean.Entry._ID + " = " + bean.getId();
        int delete = database.delete(MediaBean.Entry.TABLE_NAME, where, null);
        if (delete == 1) {
            // 删除文件
            contentResolver = requireContext().getContentResolver();
            delete = contentResolver.delete(bean.getUri(), null, null);
        }
        return delete;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
        helper.close();

    }
}