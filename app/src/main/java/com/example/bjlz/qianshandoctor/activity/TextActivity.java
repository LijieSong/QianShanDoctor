/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；

package com.example.bjlz.qianshandoctor.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.adapter.TextRecycleViewAdapter;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;
import com.example.bjlz.qianshandoctor.views.recycleView.DividerItemDecoration;
import com.jtech.view.RecyclerHolder;

import java.util.ArrayList;

/**
 * 项目名称：QianShanDoctor
 * 类描述：TextActivity 测试activity
 * 创建人：slj
 * 创建时间：2016-8-29 16:37
 * 修改人：slj
 * 修改时间：2016-8-29 16:37
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class TextActivity extends BaseActivity implements TextRecycleViewAdapter.OnRecyclerViewItemClickListener {
    private RecyclerView recycleView_Text;//显示recycleView
    private ArrayList<String> mTitles = new ArrayList<String>();
    private  TextRecycleViewAdapter adater;//匹配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        MyApplication.getInstance().addActivity(this);
        getData();
        initView();
        initData();
        setOnClick();
    }

    @Override
    public void initView() {
        recycleView_Text = (RecyclerView)findViewById(R.id.recycleView_Text);
    }

    @Override
    public void initData() {
        recycleView_Text.setLayoutManager(new LinearLayoutManager(this));
        recycleView_Text.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL,0));

        adater = new TextRecycleViewAdapter(this,mTitles);
        recycleView_Text.setAdapter(adater);
    }

    @Override
    public void getData() {

        for(int i=0;i<50;i++){
            mTitles.add("测试数据-"+i);
        }
    }

    @Override
    public void setOnClick() {
        adater.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(View view, String data) {
        ToastUtil.shortDiyToast(getApplicationContext(),data);
    }
}
