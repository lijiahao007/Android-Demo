package com.example.myapplication.album;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.album.adapter.MediaBeanAdapter;
import com.example.myapplication.album.bean.MediaBean;
import com.example.myapplication.album.bean.MediaBeanDBHelper;
import com.example.myapplication.album.bean.MediaType;
import com.example.myapplication.album.viewmodel.AlbumViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PhotoFragment extends Fragment {

    private final static String TAG = "PhotoFragment";
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private MediaBeanAdapter adapter;
    private MediaBeanDBHelper helper = null;
    private SQLiteDatabase database = null;
    private ContentResolver contentResolver;
    public static final String DELETE_PHOTO_REQUEST_KEY = "delete_request_key";
    private AlbumViewModel viewModel;

    public PhotoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 2. 初始化数据库
        if (helper == null) {
            helper = new MediaBeanDBHelper(getContext());
        }
        if (database == null) {
            database = helper.getWritableDatabase();
        }
        viewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photo, container, false);
        // 1. 初始化recyclerView
        recyclerView = root.findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new MediaBeanAdapter(this);
        recyclerView.setAdapter(adapter);
        Objects.requireNonNull(recyclerView.getItemAnimator()).setChangeDuration(0);

        // 2. 获取并设置数据
        ArrayList<MediaBean> mediaBeans = queryPhoto();
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

        // 6. 监听全选 和全不选
        viewModel.isEditMode.observe(getViewLifecycleOwner(), isEditMode -> {
            adapter.setEditMode(isEditMode);
        });

        // 6. 监听全选 和全不选
        viewModel.isSelectAll.observe(getViewLifecycleOwner(), isSelectAll -> {
            adapter.setSelectAll(isSelectAll);
        });

        // 7. 监听分享操作
        getParentFragmentManager().setFragmentResultListener(AlbumActivity.SHARE, getViewLifecycleOwner(), new FragmentResultListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(AlbumActivity.SHARE)) {
                    ArrayList<Uri> uris = (ArrayList<Uri>) adapter.getCheckedBean().stream().map(MediaBean::getUri).collect(Collectors.toList());
                    Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    intent.setType("image/jpg");
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


        // 8. 监听CheckPhotoActivity中返回的删除MediaBean
        getParentFragmentManager().setFragmentResultListener(DELETE_PHOTO_REQUEST_KEY, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(DELETE_PHOTO_REQUEST_KEY)) {
                    ArrayList<MediaBean> deleteBeans = result.getParcelableArrayList(PhotoCheckActivity.DELETE_BUNDLE);
                    adapter.deleteItems(deleteBeans);
                    Log.i("result passing ", "PhotoFragment deleteBean:" + deleteBeans.size());
                }
            }
        });


        return root;
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

    @SuppressLint("Range")
    private ArrayList<MediaBean> queryPhoto() {
        String selection = MediaBean.Entry.TYPE + " = \"IMAGE\"";
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

                MediaBean bean = new MediaBean(Uri.parse(uri), fileName, MediaType.IMAGE);
                bean.setId(id);
                bean.setTimestamp(timeStamp);
                bean.setDate(date);

                File file = new File(bean.getFileName());
                if (!file.exists()) {
                    String where = MediaBean.Entry._ID + " = " + bean.id;
                    int delete = database.delete(MediaBean.Entry.TABLE_NAME, where, null);
                    Log.i("PhotoFragment", "文件已被删除：" + bean.toString() + "----已从数据库中删除" + delete);
                    continue;
                }
                res.add(bean);
                Log.i("PhotoFragment", bean.toString());
            } while (query.moveToNext());
        }
        if (query != null) {
            query.close();
        }
        return res;
    }

}