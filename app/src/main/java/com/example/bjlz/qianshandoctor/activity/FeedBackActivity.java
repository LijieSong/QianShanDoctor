package com.example.bjlz.qianshandoctor.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;
import com.example.bjlz.qianshandoctor.views.TitleBarView;
import com.jtech.view.RecyclerHolder;

/**
 * 项目名称：QianShanDoctor
 * 类描述：FeedBackActivity 反馈
 * 创建人：slj
 * 创建时间：2016-6-28 14:45
 * 修改人：slj
 * 修改时间：2016-6-28 14:45
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class FeedBackActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    private TitleBarView title_bar;//标题
    private Context mContext = null;//上下文对象
    private RadioGroup radioTypeId;
    private RadioButton radioTypeId1;
    private RadioButton radioTypeId2;
    private RadioButton radioTypeId3;
    private EditText fb_et_content;
    private Button btnSubmit;
    private String content;//反馈内容
    private int typeId = 1;//默认为1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
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

        radioTypeId = (RadioGroup) findViewById(R.id.radioTypeId);
        radioTypeId1 = (RadioButton) findViewById(R.id.radioTypeId1);
        radioTypeId2 = (RadioButton) findViewById(R.id.radioTypeId2);
        radioTypeId3 = (RadioButton) findViewById(R.id.radioTypeId3);
        fb_et_content = (EditText) findViewById(R.id.fb_et_content);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    @Override
    public void initData() {
        this.title_bar.setTitleText(R.string.feeBack);
    }

    @Override
    public void getData() {

    }

    @Override
    public void setOnClick() {
// 判断radiobutton的点击事件
        radioTypeId.setOnCheckedChangeListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void WeightOnClick(View v) {
        content = fb_et_content.getText().toString().trim();
        // 判断输入内容是否为空
        if (TextUtils.isEmpty(content)) {
            // 说明输入内容为空
            ToastUtil.shortDiyToast(getApplicationContext(),"意见反馈不能为空");
        } else {
            ToastUtil.shortDiyToast(getApplicationContext(),"意见反馈已成功");
            finish();
        }
    }

    @Override
    public void onRecycleViewItemClick(RecyclerHolder holder, View view, int position) {

    }

    @Override
    public boolean onRecycleItemViewMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int buttonId = group.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) FeedBackActivity.this
                .findViewById(buttonId);
        switch (rb.getId()) {
            case R.id.radioTypeId1:
                typeId = 1;
                break;
            case R.id.radioTypeId2:
                typeId = 2;
                break;
            case R.id.radioTypeId3:
                typeId = 3;
                break;
        }
    }
}
