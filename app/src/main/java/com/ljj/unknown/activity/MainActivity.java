package com.ljj.unknown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ljj.unknown.R;
import com.ljj.unknown.activity.fragment.ContactsFragment;
import com.ljj.unknown.activity.fragment.ConversationFragment;
import com.ljj.unknown.activity.fragment.PersonalFragment;
import com.ljj.unknown.adapter.FragmentAdapter;
import com.ljj.unknown.util.ImUtil;
import com.ljj.unknown.util.NetworkState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMMessage;

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
    private FragmentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ConversationFragment());
        fragments.add(new ContactsFragment());
        fragments.add(new PersonalFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPagerMian.setCurrentItem(0);
        imgConversation.setImageResource(R.drawable.main_conversation_img);
        imgContacts.setImageResource(R.drawable.main_contacts_ash_img);
        imgPersonal.setImageResource(R.drawable.main_personal_ash_img);
        viewPagerMian.setAdapter(adapter);
        viewPagerMian.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        imgConversation.setImageResource(R.drawable.main_conversation_img);
                        imgContacts.setImageResource(R.drawable.main_contacts_ash_img);
                        imgPersonal.setImageResource(R.drawable.main_personal_ash_img);
                        break;
                    case 1:
                        imgConversation.setImageResource(R.drawable.main_conversation_ash_img);
                        imgPersonal.setImageResource(R.drawable.main_personal_ash_img);
                        imgContacts.setImageResource(R.drawable.main_contacts_img);
                        break;
                    case 2:
                        imgContacts.setImageResource(R.drawable.main_contacts_ash_img);
                        imgConversation.setImageResource(R.drawable.main_conversation_ash_img);
                        imgPersonal.setImageResource(R.drawable.main_personal_img);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ImUtil.connectServer();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BmobIMMessage event) {
        if (event.getContent().equals("#video^chat#")){
            Intent intent = new Intent(this,CallActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",event.getBmobIMUserInfo());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }





    @OnClick({R.id.ll_bottom_menu_conversation, R.id.ll_bottom_menu_contacts, R.id.ll_bottom_menu_personal, R.id.iv_add_friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_bottom_menu_conversation:
                viewPagerMian.setCurrentItem(0);
                break;
            case R.id.ll_bottom_menu_contacts:
                viewPagerMian.setCurrentItem(1);
                break;
            case R.id.ll_bottom_menu_personal:
                viewPagerMian.setCurrentItem(2);
                break;
            case R.id.iv_add_friend:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
        }
    }

}
