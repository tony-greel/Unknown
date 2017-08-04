package com.ljj.unknown.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ljj.unknown.R;

import butterknife.Bind;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.viewPager_mian)
    ViewPager viewPagerMian;
    @Bind(R.id.img_contacts)
    ImageView imgContacts;
    @Bind(R.id.ll_bottom_menu_contacts)
    LinearLayout llBottomMenuContacts;
    @Bind(R.id.img_conversation)
    ImageView imgConversation;
    @Bind(R.id.ll_bottom_menu_conversation)
    LinearLayout llBottomMenuConversation;
    @Bind(R.id.img_personal)
    ImageView imgPersonal;
    @Bind(R.id.ll_bottom_menu_personal)
    LinearLayout llBottomMenuPersonal;
    @Bind(R.id.bottom_layout)
    LinearLayout bottomLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
