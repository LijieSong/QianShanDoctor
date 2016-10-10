package com.example.bjlz.qianshandoctor.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.OtherTools.PreferencesUtils;
import com.hyphenate.chat.EMClient;
import com.jtech.view.RecyclerHolder;

/**
 * 项目名称：qianshandoctor
 * 类描述：SplashActivity :欢迎动画
 * 创建人：slj
 * 创建时间：2016-5-28 17:24
 * 修改人：slj
 * 修改时间：2016-5-28 17:24
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class SplashActivity extends BaseActivity {
    private Handler handler = new Handler();
    private boolean isLogin = false;//是否登录
    private boolean loggedInBefore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getData();
        initView();
        initData();
        setOnClick();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin == false ||loggedInBefore == false){
                    MyApplication.startActivity(SplashActivity.this, LoginActivity.class);
                }else{
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    MyApplication.startActivity(SplashActivity.this, MainActivity.class);
                }
                finish();
            }
        }, 50);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void getData() {
        loggedInBefore = EMClient.getInstance().isLoggedInBefore();
        isLogin = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin", false);
    }

    @Override
    public void setOnClick() {

    }

    @Override
    public void WeightOnClick(View v) {

    }

    @Override
    public void onRecycleViewItemClick(RecyclerHolder holder, View view, int position) {

    }

    @Override
    public boolean onRecycleItemViewMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }
}
