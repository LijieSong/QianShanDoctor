package com.example.bjlz.qianshandoctor.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.example.bjlz.qianshandoctor.utils.OtherTools.StatusBarUtils;
import com.jtech.listener.OnItemClickListener;
import com.jtech.listener.OnItemViewMoveListener;
import com.jtech.view.RecyclerHolder;

import cn.jpush.android.api.JPushInterface;


/**
 * 项目名称：qianshandoctor
 * 类描述：BaseActivity :基类
 * 创建人：slj
 * 创建时间：2016-5-28 17:24
 * 修改人：slj
 * 修改时间：2016-5-28 17:24
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener, OnItemViewMoveListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题
        StatusBarUtils.setStatusBar(BaseActivity.this);//设置透明状态栏
    }

    /**
     * 初始化UI
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 获取传递过来的数据或者存储的数据
     */
    public abstract void getData();

    /**
     * 设置监听事件
     */
    public abstract void setOnClick();

    /**
     * 处理点击事件
     *
     * @param v
     */
    public abstract void WeightOnClick(View v);

    /**
     * 处理RecycleViewItem的点击事件
     *
     * @param holder
     * @param view
     * @param position
     */
    public abstract void onRecycleViewItemClick(RecyclerHolder holder, View view, int position);

    /**
     * 处理RecycleViewItem的移动事件
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    public abstract boolean onRecycleItemViewMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);

    @Override
    public void onClick(View v) {
        WeightOnClick(v);
    }

    /**
     * 返回键关闭本页面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        JPushInterface.onResume(this);
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
    @Override
    public void onItemClick(RecyclerHolder holder, View view, int position) {
        onRecycleViewItemClick(holder, view, position);
    }

    @Override
    public boolean onItemViewMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return onRecycleItemViewMove(recyclerView, viewHolder, target);
    }

}
