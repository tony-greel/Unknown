package com.ljj.unknown.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.ljj.unknown.R;
import com.ljj.unknown.adapter.ImageAdapter;
import com.ljj.unknown.bean.Post;
import com.ljj.unknown.bean.User;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class PublishActivity extends BaseActivity {

    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.rv_publish_image)
    RecyclerView rvPublishImage;
    ImageAdapter adapter;

    public static final int REQUEST_CODE_IMAGE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_publish);
        initHome();
        List<String> data = new ArrayList<>();
        data.add("image");
        adapter = new ImageAdapter(data);
        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SImagePicker
                        .from(PublishActivity.this)
                        .maxCount(10 - adapter.getData().size())
                        .rowCount(3)
                        .showCamera(true)
                        .pickMode(SImagePicker.MODE_IMAGE)
                        .forResult(REQUEST_CODE_IMAGE);
            }
        });
        rvPublishImage.setLayoutManager(new GridLayoutManager(this, 3));
        rvPublishImage.setItemAnimator(new DefaultItemAnimator());
        rvPublishImage.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            for (String s : pathList) {
                adapter.getData().add(adapter.getItemCount() - 1, s);
                adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.publish_post) {
            if (TextUtils.isEmpty(etContent.getText().toString())){
                Toast.makeText(mActivity, "请填写内容", Toast.LENGTH_SHORT).show();
            }else {
                showProgressDialog();
                final Post post = new Post();
                post.setContent(etContent.getText().toString());
                final String[] image = new String[adapter.getData().size()-1];
                for (int i = 0; i < adapter.getData().size(); i++) {
                    if (i!=adapter.getData().size()-1){
                        image[i] = adapter.getData().get(i);
                    }
                }
                if (image.length > 0){
                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    options.quality=50;
                    Tiny.getInstance().source(image).batchAsFile().withOptions(options).batchCompress(new FileBatchCallback() {
                        @Override
                        public void callback(boolean isSuccess, String[] outfile) {
                            if (isSuccess){
                                uploadBatch(outfile, new OnUploadBatchListener() {
                                    @Override
                                    public void success(List<String> images) {
                                        String[] imagesUrl = new String[images.size()];
                                        for (int i = 0; i < images.size(); i++) {
                                            imagesUrl[i] = images.get(i);
                                        }
                                        post.setImages(imagesUrl);
                                        publish(post);
                                    }

                                    @Override
                                    public void fail(String error) {
                                        dismiss();
                                        Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                dismiss();
                                Toast.makeText(mActivity, "压缩图片出错", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    post.setImages(image);
                    publish(post);
                }


            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void publish(Post post){
        post.setPublisher(BmobUser.getCurrentUser(User.class));
        post.setPublishTime(System.currentTimeMillis());
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                dismiss();
                if (e == null){
                    Toast.makeText(mActivity, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    if (e.getErrorCode() == 9016){
                        Toast.makeText(mActivity, "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void uploadBatch(final String[] path, final OnUploadBatchListener listener){

        BmobFile.uploadBatch(path, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (path.length==list1.size()){
                    listener.success(list1);
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                if (i==9016){
                    listener.fail("请检查网络");
                }else {
                    listener.fail(i+s);
                }
            }
        });
    }

    public interface OnUploadBatchListener{
        void success(List<String> images);
        void fail(String error);
    }
}
