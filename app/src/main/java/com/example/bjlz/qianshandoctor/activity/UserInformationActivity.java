package com.example.bjlz.qianshandoctor.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.application.Address;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;
import com.example.bjlz.qianshandoctor.utils.volley.ImageLoaderUtil;
import com.example.bjlz.qianshandoctor.views.TitleBarView;
import com.jtech.view.RecyclerHolder;

/**
 * 项目名称：QianShanDoctor
 * 类描述：UserInformationActivity 用户详情
 * 创建人：slj
 * 创建时间：2016-6-28 17:17
 * 修改人：slj
 * 修改时间：2016-6-28 17:17
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class UserInformationActivity extends BaseActivity {
    private TitleBarView title_bar;//标题
    private WebView webView_userInfor;//显示患者详情
    private ProgressBar webView_ProgressBar;//进度条显示进度
    private ImageView image_user_icon, img_look_doctor, img_eat_medication;//用户头像
    private TextView tv_user_name, tv_user_sex, tv_user_age, tv_user_zhu_su;//用户姓名,年龄,性别,主诉
    private Context mContext = null;//上下文对象
    private String name, age, sex, zhusu;//用户姓名,年龄,性别,主诉
    private int imgId;//用户头像id
    private String imgUrl = "http://pics.sc.chinaz.com/Files/pic/icons128/6201/2r.png";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinformation);
        MyApplication.getInstance().addActivity(this);
        mContext = this;
        getData();
        initView();
        initData();
        setOnClick();
    }

    @Override
    public void initView() {
        this.title_bar = (TitleBarView) findViewById(R.id.title_bar);
        this.title_bar.setTitleText(R.string.user_infor);
        webView_ProgressBar = (ProgressBar) findViewById(R.id.webView_ProgressBar);
        webView_userInfor = (WebView) findViewById(R.id.webView_userInfor);

        image_user_icon = (ImageView) findViewById(R.id.image_user_icon);
        img_look_doctor = (ImageView) findViewById(R.id.img_look_doctor);
        img_eat_medication = (ImageView) findViewById(R.id.img_eat_medication);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_sex = (TextView) findViewById(R.id.tv_user_sex);
        tv_user_age = (TextView) findViewById(R.id.tv_user_age);
        tv_user_zhu_su = (TextView) findViewById(R.id.tv_user_zhu_su);

    }

    @Override
    public void initData() {
        // 设置JS交互数据
        webView_userInfor.getSettings().setJavaScriptEnabled(true);
        webView_userInfor.getSettings().setSupportZoom(true);
        webView_userInfor.getSettings().setBuiltInZoomControls(true);
        webView_userInfor.getSettings().setDisplayZoomControls(false);
        webView_userInfor.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    webView_ProgressBar.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == webView_ProgressBar.getVisibility()) {
                        webView_ProgressBar.setVisibility(View.VISIBLE);
                    }
                    webView_ProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        // 加载web资源
        webView_userInfor.loadUrl(Address.TEXT_URL4);
        ImageLoaderUtil.setImageLoader(imgUrl,image_user_icon,R.drawable.logo,R.drawable.menu);
//        ImageLoaderUtil.setNetWorkImageView(imgUrl,image_user_icon,R.drawable.logo,R.drawable.menu);
        //设置显示
        tv_user_name.setText(name);
        tv_user_age.setText(age+"岁");
        tv_user_sex.setText(sex);
        tv_user_zhu_su.setText("主诉:" +zhusu);
    }

    @Override
    public void getData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle !=null){
        name = bundle.getString("name");
        age = bundle.getString("age");
        sex = bundle.getString("sex");
        imgId = bundle.getInt("imgId");
        zhusu = bundle.getString("zhusu");
        }
    }

    @Override
    public void setOnClick() {
        // 设置webview的点击事件
        webView_userInfor.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        img_look_doctor.setOnClickListener(this);
        img_eat_medication.setOnClickListener(this);
    }

    @Override
    public void WeightOnClick(View v) {
        switch (v.getId()) {
            case R.id.img_look_doctor://就医记录
                ToastUtil.shortDiyToast(mContext, "跳转下一级界面");
                break;
            case R.id.img_eat_medication://用药记录
                ToastUtil.shortDiyToast(mContext, "跳转下一级界面");
                break;
        }
    }

    @Override
    public void onRecycleViewItemClick(RecyclerHolder holder, View view, int position) {

    }

    @Override
    public boolean onRecycleItemViewMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }
}
