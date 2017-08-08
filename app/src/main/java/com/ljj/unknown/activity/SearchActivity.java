package com.ljj.unknown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ljj.unknown.R;
import com.ljj.unknown.bean.User;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends BaseActivity {


    @Bind(R.id.tb_search)
    Toolbar tbSearch;
    @Bind(R.id.image_but_search)
    ImageButton imageButSearch;
    @Bind(R.id.rl_search_result)
    RelativeLayout rlSearchResult;
    @Bind(R.id.et_search_username)
    EditText etSearchUsername;
    @Bind(R.id.cv_result_head)
    CircleImageView cvResultHead;
    @Bind(R.id.tv_result_nickname)
    TextView tvResultNickname;
    @Bind(R.id.tv_result_username)
    TextView tvResultUsername;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_search);
        initHome();
    }

    @OnClick({R.id.image_but_search, R.id.rl_search_result})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_but_search:
                if (TextUtils.isEmpty(etSearchUsername.getText().toString())) {
                    Toast.makeText(mActivity, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    showProgressDialog();
                    BmobQuery<User> query = new BmobQuery();
                    query.addWhereEqualTo("username", etSearchUsername.getText().toString());
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            dismiss();
                            if (e == null) {
                                if (list.size() > 0) {
                                    rlSearchResult.setVisibility(View.VISIBLE);
                                    user = list.get(0);
                                    if (TextUtils.isEmpty(user.getHeadUrl())) {
                                        Glide.with(SearchActivity.this)
                                                .load(R.drawable.ic_portrait_but)
                                                .into(cvResultHead);
                                    } else {
                                        Glide.with(SearchActivity.this)
                                                .load(user.getHeadUrl())
                                                .into(cvResultHead);
                                    }
                                    tvResultNickname.setText(user.getNickname());
                                    tvResultUsername.setText("用户名："+user.getUsername());

                                } else {
                                    Toast.makeText(mActivity, "查无此人", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (e.getErrorCode() == 9016) {
                                    Toast.makeText(mActivity, "网络不给力", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.rl_search_result:
                Intent intent = new Intent(this,PersonalInformation.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("friend",user);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
