package com.ljj.unknown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ljj.unknown.R;
import com.ljj.unknown.bean.User;
import com.ljj.unknown.util.Microblogtools;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Administrator on 2017/7/30.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.long_img)
    ImageView longImg;
    @Bind(R.id.long_but)
    Button longBut;
    @Bind(R.id.et_login_username)
    EditText etLoginUsername;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.register_but)
    Button registerBut;
    @Bind(R.id.qq_long)
    ImageButton qqLong;
    @Bind(R.id.micro_long)
    ImageButton microLong;

    private static final String TAG = "MainActivity";
    private static final String APP_ID = "1106163416";//官方获取的APPID
    private Tencent mTencent;
    public BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private UsersAPI mUsersAPI;
    public User thirdUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initial();//初始化
    }

    private void initial() {
        StatusBarCompat.translucentStatusBar(this);//去掉状态栏
        ButterKnife.bind(this);
        longBut.setOnClickListener(this);
        registerBut.setOnClickListener(this);
        qqLong.setOnClickListener(this);
        microLong.setOnClickListener(this);
//        mAuthInfo = new AuthInfo(this, Microblogtools.APP_KEY, Microblogtools.REDIRECT_URL, Microblogtools.SCOPE);
//        mSsoHandler = new SsoHandler(this, mAuthInfo);
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.longin);
                longImg.startAnimation(animation);
            }
        }, 100);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.long_but) {
            String name = etLoginUsername.getText().toString();
            String password = etLoginPassword.getText().toString();
            if (name.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            showProgressDialog();
            BmobUser user = new BmobUser();
            user.setUsername(name);
            user.setPassword(password);
            user.login(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobuser, BmobException e) {
                    dismiss();
                    if (e == null) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dismiss();
        }

        if (v.getId() == R.id.register_but) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.qq_long) {
            mIUiListener = new BaseUiListener();
            //all表示获取所有权限
            mTencent.login(LoginActivity.this, "all", mIUiListener);
        }

        if(v.getId() == R.id.micro_long){
            mSsoHandler.authorize(new AuthListener());
        }
    }

    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle bundle) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
            if (mAccessToken.isSessionValid()) {

                //获取用户具体信息
                getUserInfo();
            } else {
                String code = bundle.getString("code");//直接从bundle里边获取
                if (!TextUtils.isEmpty(code)) {
                    Toast.makeText(LoginActivity.this, "签名不正确", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

            Toast.makeText(LoginActivity.this, "授权异常", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

        private void getUserInfo() {
            //调用接口
            long uid = Long.parseLong(mAccessToken.getUid());
            new UsersAPI(LoginActivity.this, Constants.KEY_APP_NAME, mAccessToken)
                    .show(uid, mListener);
        }

        //实现异步请求接口回调
        private RequestListener mListener = new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    com.sina.weibo.sdk.openapi.models.User user = com.sina.weibo.sdk.openapi.models.User.parse(response);
                    thirdUser = new User();
                    thirdUser.setUsername(user.id);
                    thirdUser.setHeadUrl(user.avatar_hd);
                    thirdUser.setSex(user.gender);
                    thirdUser.setAddress(user.province +"-"+ user.city);
                    thirdUser.setPassword("123456");
                    thirdUser.setNickname(user.screen_name);
                    thirdUserLogin();
                    String nickName = user.screen_name;
                    Toast.makeText(LoginActivity.this, "用户昵称：" + nickName.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "获取用户个人信息 出现异常", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void thirdUserLogin(){
        showProgressDialog();
        thirdUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e == null || e.getErrorCode() == 202){
                    e=null;
                    thirdUser.login(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            dismiss();
                            if(e == null){
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, e.getErrorCode()+" "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    dismiss();
                    Toast.makeText(LoginActivity.this, e.getErrorCode()+" "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void thirdLogin(){
        showProgressDialog();
        thirdUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User user1, BmobException e) {
                if (e == null || e.getErrorCode() == 202){
                    e=null;
                    thirdUser.login(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            dismiss();
                            if (e == null){
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, e.getErrorCode()+" "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    dismiss();
                    Toast.makeText(LoginActivity.this, e.getErrorCode()+" "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                thirdUser= new User();
                thirdUser.setUsername(openID);
                thirdUser.setPassword("!@#$q432~wer&*432(CDE2/(ㄒoㄒ)/~~");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        try {
                            thirdUser.setNickname(((JSONObject) response).getString("nickname"));
                            thirdUser.setSex(((JSONObject) response).getString("gender"));
                            thirdUser.setAddress(((JSONObject) response).getString("province")+"-"+((JSONObject) response).getString("city"));
                            thirdUser.setHeadUrl(((JSONObject) response).getString("figureurl_qq_2"));

                            thirdLogin();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "登录成功" + response.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录成功" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "登录取消", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == Constants.REQUEST_LOGIN) {
                Tencent.onActivityResultData(requestCode, resultCode, data,mIUiListener);
            }
            if (mSsoHandler != null){
                mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
