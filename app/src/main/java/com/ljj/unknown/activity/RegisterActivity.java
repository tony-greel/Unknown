package com.ljj.unknown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ljj.unknown.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/7/30.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.tb_register)
    Toolbar tbRegister;
    @Bind(R.id.et_register_username_account)
    EditText etRegisterUsernameAccount;
    @Bind(R.id.et_register_password)
    EditText etRegisterPassword;
    @Bind(R.id.et_register_confirm_password)
    EditText etRegisterConfirmPassword;
    @Bind(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_register);
        initHome();
        initialization();
    }

    private void initialization() {
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String username = etRegisterUsernameAccount.getText().toString();
        String password = etRegisterPassword.getText().toString();
        String confirmpassword = etRegisterConfirmPassword.getText().toString();

        if (v == btnRegister) {
            if (username.equals("") || password.equals("")) {
                Toast.makeText(RegisterActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            } else if (etRegisterUsernameAccount.getText().toString().trim().length() < 11 || etRegisterUsernameAccount.getText().toString().trim().length() > 11) {
                Toast.makeText(RegisterActivity.this, "输入的账号必须是十一位数,请重新输入", Toast.LENGTH_SHORT).show();
                return;
            } else if (etRegisterPassword.getText().toString().trim().length() < 7 || etRegisterPassword.getText().toString().trim().length() > 20) {
                Toast.makeText(RegisterActivity.this, "输入的密码必须大于或等于七位数,请重新输入", Toast.LENGTH_SHORT).show();
                return;
            } else if (!password.equals(confirmpassword)) {
                Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }

            showProgressDialog();

            BmobUser user = new BmobUser();
            user.setUsername(username);
            user.setPassword(password);

            user.signUp(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if(e == null){
                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                        finish();
                        startActivity(intent);
                    }else if(e.getErrorCode() == 202){

                        Toast.makeText(RegisterActivity.this, "该用户名以存在", Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
            });

        }

    }
}
