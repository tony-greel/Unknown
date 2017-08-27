package com.ljj.unknown.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.ljj.unknown.R;
import com.ljj.unknown.bean.User;
import com.ljj.unknown.other.CacheManager;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PersonalActivity extends BaseActivity {

    @Bind(R.id.tb_scan)
    Toolbar tbScan;
    @Bind(R.id.iv_persona2)
    ImageView ivPersona2;
    @Bind(R.id.ll_portrait)
    LinearLayout llPortrait;
    @Bind(R.id.ll_name)
    LinearLayout llName;
    @Bind(R.id.ll_number)
    LinearLayout llNumber;
    @Bind(R.id.ll_more)
    LinearLayout llMore;
    @Bind(R.id.tv_persona2_name)
    TextView tvPersona2Name;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.iv_qr_code)
    ImageView ivQrCode;

    public static final String AVATAR_FILE_NAME = "avatar.png";
    public static final int REQUEST_CODE_AVATAR = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_scan);
        initHome();

        if (TextUtils.isEmpty(BmobUser.getCurrentUser(User.class).getHeadUrl())) {
            Glide.with(this)
                    .load(R.drawable.ic_portrait_but)
                    .into(ivPersona2);
        } else {
            Glide.with(this)
                    .load(BmobUser.getCurrentUser(User.class).getHeadUrl())
                    .into(ivPersona2);
        }

        User user = User.getCurrentUser(User.class);
        tvPersona2Name.setText(user.getNickname());
        tvNumber.setText(user.getUsername());

    }

    @OnClick({R.id.iv_persona2, R.id.iv_qr_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_persona2:
                if (getStorage() && getCcamra()) {
                    SImagePicker
                            .from(PersonalActivity.this)
                            .pickMode(SImagePicker.MODE_AVATAR)
                            .showCamera(true)
                            .cropFilePath(
                                    CacheManager.getInstance().getImageInnerCache()
                                            .getAbsolutePath(AVATAR_FILE_NAME))
                            .forResult(REQUEST_CODE_AVATAR);
                    return;
                }

                break;
            case R.id.iv_qr_code:
                Intent intent = new Intent(this,ScanActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AVATAR) {
            final ArrayList<String> pathList = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            showProgressDialog();
            final BmobFile bmobfile = new BmobFile(new File(pathList.get(0)));
            bmobfile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(PersonalActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        User newUser = new User();
                        newUser.setHeadUrl(bmobfile.getUrl());
                        User user = BmobUser.getCurrentUser(User.class);
                        newUser.update(user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    dismiss();
                                    Glide.with(PersonalActivity.this)
                                            .load(BmobUser.getCurrentUser(User.class).getHeadUrl())
                                            .into(ivPersona2);
                                } else {
                                    dismiss();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(PersonalActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

