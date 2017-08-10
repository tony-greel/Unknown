package com.ljj.unknown.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.ljj.unknown.R;
import com.ljj.unknown.bean.User;
import com.ljj.unknown.util.FriendUtil;
import com.ljj.unknown.zxing.CaptureActivity;
import com.ljj.unknown.zxing.EncodingHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class ScanActivity extends BaseActivity {

    @Bind(R.id.iv_scan_code)
    ImageView ivScanCode;
    @Bind(R.id.cv_scan_head)
    CircleImageView cvScanHead;
    @Bind(R.id.tv_scan_name)
    TextView tvScanName;
    @Bind(R.id.rl_scan_code)
    RelativeLayout rlScanCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_scan);
        initHome();
        initView();
    }

    private void initView() {
        int width=dipToPx(this,160.0f);
        User user = BmobUser.getCurrentUser(User.class);
        ivScanCode.setImageBitmap(createCode(user.getObjectId(),width));
        if (TextUtils.isEmpty(user.getHeadUrl())){
            Glide.with(this)
                    .load(R.drawable.ic_portrait_but)
                    .into(cvScanHead);
        }else {
            Glide.with(this)
                    .load(user.getHeadUrl())
                    .into(cvScanHead);
        }

        if (TextUtils.isEmpty(user.getNickname())){
            tvScanName.setText(user.getUsername());
        }else {
            tvScanName.setText(user.getNickname());
        }


    }

    private Bitmap createCode(String message, int width) {
        Bitmap qrCodeBitmap = null;
        try {
            qrCodeBitmap = EncodingHandler.createQRCode(message, width);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return qrCodeBitmap;
    }

    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @OnClick(R.id.rl_scan_code)
    public void onViewClicked() {
        Intent openCameraIntent = new Intent(this,
                CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            showProgressDialog();
            Bundle bundle = data.getExtras();
            String objectId = bundle.getString("result");
            User user = new User();
            user.setObjectId(objectId);
            FriendUtil.addFriend(user, new FriendUtil.OnFriendDealListener() {
                @Override
                public void onSuccess() {
                    dismiss();
                    Toast.makeText(mActivity, "添加好友成功!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    dismiss();
                    Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
