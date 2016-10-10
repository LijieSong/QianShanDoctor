package com.example.bjlz.qianshandoctor.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.application.Address;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.CommonUtil;
import com.example.bjlz.qianshandoctor.views.TitleBarView;
import com.jtech.view.RecyclerHolder;

/**
 * 项目名称：QianShanDoctor
 * 类描述：QuestionnaireSurveyActivity 问卷调查
 * 创建人：slj
 * 创建时间：2016-6-29 17:13
 * 修改人：slj
 * 修改时间：2016-6-29 17:13
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class QuestionnaireSurveyActivity extends BaseActivity {
    private Context mContext = null;//上下文对象
    private TitleBarView title_bar;//标题
    private ImageView img_health_china, img_health_psychology, img_health_routine;//箭头
    private String name;//名字
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnairesurvey);
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
        img_health_china = (ImageView) findViewById(R.id.img_health_china);
        img_health_psychology = (ImageView) findViewById(R.id.img_health_psychology);
        img_health_routine = (ImageView) findViewById(R.id.img_health_routine);
    }

    @Override
    public void initData() {
        this.title_bar.setTitleText(name +"的"+ CommonUtil.getString(R.string.QuestionnaireSurvey));
    }

    @Override
    public void getData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle !=null){
            name = bundle.getString("name");
//            age = bundle.getString("age");
//            sex = bundle.getString("sex");
//            imgId = bundle.getInt("imgId");
//            zhusu = bundle.getString("zhusu");
        }
    }

    @Override
    public void setOnClick() {
        img_health_china.setOnClickListener(this);
        img_health_psychology.setOnClickListener(this);
        img_health_routine.setOnClickListener(this);
    }

    @Override
    public void WeightOnClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("name",name);

    switch(v.getId()){
        case R.id.img_health_china://中医体质问卷
            bundle.putString("url", Address.TEXT_URL1);
            bundle.putString("title","中医体质问卷");
            MyApplication.startActivity(QuestionnaireSurveyActivity.this,QuestionnaireSurveyWebViewActivity.class,bundle);
        break;
        case R.id.img_health_psychology://心理问卷
            bundle.putString("url",Address.TEXT_URL2);
            bundle.putString("title","心理问卷");
            MyApplication.startActivity(QuestionnaireSurveyActivity.this,QuestionnaireSurveyWebViewActivity.class,bundle);
            break;
        case R.id.img_health_routine://常规健康问卷
            bundle.putString("url",Address.TEXT_URL3);
            bundle.putString("title","常规健康问卷");
            MyApplication.startActivity(QuestionnaireSurveyActivity.this,QuestionnaireSurveyWebViewActivity.class,bundle);
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
