package com.example.myapplication.album;

import static com.example.myapplication.album.AlbumActivity.DELETE_BUNDLE;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.album.adapter.MediaBeanAdapter;
import com.example.myapplication.album.bean.MediaBean;
import com.example.myapplication.album.bean.MediaBeanDBHelper;
import com.example.myapplication.album.bean.MediaType;
import com.example.myapplication.album.viewmodel.AlbumViewModel;
import com.example.myapplication.tablayout.LifecycleLogObserver;
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
    public static final String DELETE_PHOTO_REQUEST_KEY = "delete_photo_request_key";
    public static final String DELETE_VIDEO_REQUEST_KEY = "delete_video_request_key";
    private AlbumViewModel viewModel;

    public PhotoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 2. ??????????????????
        if (helper == null) {
            helper = new MediaBeanDBHelper(getContext());
        }
        if (database == null) {
            database = helper.getWritableDatabase();
        }
        viewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photo, container, false);
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleLogObserver("PhotoFragment-lifecycle"));
        // 1. ?????????recyclerView
        recyclerView = root.findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new MediaBeanAdapter(this);
        recyclerView.setAdapter(adapter);
        Objects.requireNonNull(recyclerView.getItemAnimator()).setChangeDuration(0);

        // 2. ?????????????????????
        ArrayList<MediaBean> mediaBeans = queryPhoto();
        adapter.setList(mediaBeans);

        // 3. ????????????
        recyclerView.addItemDecoration(new MediaItemDecoration(requireContext()));

        // 4. ????????????
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

        // 5. ??????????????????
        viewModel.isEditMode.observe(getViewLifecycleOwner(), isEditMode -> {
            adapter.setEditMode(isEditMode);
        });

        // 6. ???????????? ????????????
        viewModel.isSelectAll.observe(getViewLifecycleOwner(), isSelectAll -> {
            if (isSelectAll) {
                adapter.selectAll();
            }
        });
        viewModel.isDeselectAll.observe(getViewLifecycleOwner(), isDeselectAll -> {
            if (isDeselectAll) {
                adapter.deSelectAll();
            }
        });

        // 7. ??????????????????
        viewModel.shareBehavior.observe(getViewLifecycleOwner(), isShare -> {
            Log.i(TAG, "?????????????????????:" + isShare);
            if (isShare && viewModel.curItem == 0) {
                ArrayList<Uri> uris = (ArrayList<Uri>) adapter.getCheckedBean().stream().map(MediaBean::getUri).collect(Collectors.toList());
                new MaterialAlertDialogBuilder(requireContext())
                        .setMessage("??????" + uris.size() + "?????????")
                        .setPositiveButton("??????", (dialog, which) -> {
                            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                            intent.setType("image/jpg");
                            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                            requireContext().startActivity(intent);
                        })
                        .setNegativeButton("??????", (dialog, which) -> {
                            dialog.cancel();
                        })
                        .show();
                viewModel.shareBehavior.setValue(false);
            }
        });

        // 8. ??????????????????
        viewModel.deleteBehavior.observe(getViewLifecycleOwner(), isDelete -> {
            if (isDelete && viewModel.curItem == 0) {
                List<MediaBean> checkedBean = adapter.getCheckedBean();
                new MaterialAlertDialogBuilder(requireContext())
                        .setMessage("??????" + checkedBean.size() + "?????????")
                        .setPositiveButton("??????", (dialog, which) -> {
                            for (MediaBean bean : checkedBean) {
                                int res = deleteMediaBean(bean);
                            }
                            adapter.deleteItems(checkedBean);
                        })
                        .setNegativeButton("??????", (dialog, which) -> {
                            dialog.cancel();
                        })
                        .show();
                viewModel.deleteBehavior.setValue(false);
            }
        });

        // 9. ??????CheckPhotoActivity??????????????????MediaBean
        getParentFragmentManager().setFragmentResultListener(DELETE_PHOTO_REQUEST_KEY, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(DELETE_PHOTO_REQUEST_KEY)) {
                    ArrayList<MediaBean> deleteBeans = result.getParcelableArrayList(DELETE_BUNDLE);
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
            // ????????????
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
                    Log.i("PhotoFragment", "?????????????????????" + bean.toString() + "----????????????????????????" + delete);
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