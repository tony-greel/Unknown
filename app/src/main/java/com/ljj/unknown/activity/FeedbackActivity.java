package com.ljj.unknown.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ljj.unknown.R;
import com.ljj.unknown.bean.Feedback;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/8/10.
 */

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tb_feedback)
    Toolbar tbFeedback;
    @Bind(R.id.cb_product_feedback)
    CheckBox cbProductFeedback;
    @Bind(R.id.cb_program_feedback)
    CheckBox cbProgramFeedback;
    @Bind(R.id.btn_submit_feedback)
    Button btnSubmitFeedback;
    @Bind(R.id.et_proposal_feedback)
    EditText etProposalFeedback;
    @Bind(R.id.et_telephone_feedback)
    EditText etTelephoneFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_feedback);
        initHome();
        initialization();
    }

    private void initialization() {
        btnSubmitFeedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmitFeedback) {
            String content = etProposalFeedback.getText().toString();
            String contacts = etTelephoneFeedback.getText().toString();

            if (content.equals("") || contacts.equals("")) {
                Toast.makeText(this, "反馈内容或联系方式不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            showProgressDialog();

            Feedback feedback = new Feedback();
            feedback.setContent(content);
            feedback.setContacts(contacts);
            feedback.save(new SaveListener<String>() {

                @Override
                public void done(String s, BmobException e) {
                  if(e == null){
                      Toast.makeText(FeedbackActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
                  }else {
                      Toast.makeText(FeedbackActivity.this, "反馈失败", Toast.LENGTH_SHORT).show();
                  }
                  dismiss();
                }
            });
        }
    }
}
