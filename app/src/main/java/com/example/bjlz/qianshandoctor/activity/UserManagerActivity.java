package com.example.bjlz.qianshandoctor.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.adapter.UserManagerAdapter;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.bean.UserManagerBean;
import com.example.bjlz.qianshandoctor.pull.SwipyRefreshLayout;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;
import com.example.bjlz.qianshandoctor.views.recycleView.DividerItemDecoration;
import com.example.bjlz.qianshandoctor.views.TitleBarView;
import com.jtech.view.RecyclerHolder;


/**
 * 项目名称：QianShanDoctor
 * 类描述：UserManagerActivity 用户管理
 * 创建人：slj
 * 创建时间：2016-6-28 14:17
 * 修改人：slj
 * 修改时间：2016-6-28 14:17
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class UserManagerActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener,UserManagerAdapter.OnRecyclerViewItemClickListener{
    private TitleBarView title_bar;//标题
    private RecyclerView v7_recyerview;//recycleview
    private SwipyRefreshLayout refreshLayout;//刷新的控制
    private final int TOP_REFRESH = 1;//下拉刷新
    private final int BOTTOM_REFRESH = 2;//上拉加载更多

    private UserManagerAdapter adapter;//适配器
    private LinearLayoutManager linearLayoutManager;//布局管理器
    private Context mContext;//上下文对象
    private int[] user_image = {R.drawable.logo, R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo, R.drawable.logo};
    private String[] user_names = {"王小二", "李晓光", "张三", "李四", "王麻子", "轩辕红"};
    private String[] zhusu = {"糖尿病", "血压高", "经常失眠", "头晕头疼", "头疼脑热", "浑身酸痛"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanager);
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
        v7_recyerview= (RecyclerView) findViewById(R.id.v7_recyerview);
        refreshLayout= (SwipyRefreshLayout) findViewById(R.id.refreshLayout);
    }

    @Override
    public void initData() {
        this.title_bar.setTitleText(R.string.user_manager);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        v7_recyerview.setLayoutManager(linearLayoutManager);
        v7_recyerview.addItemDecoration(new DividerItemDecoration(mContext,linearLayoutManager.getOrientation(),getResources().getColor(R.color.devide_line)));
        adapter=new UserManagerAdapter(this,user_image,user_names,zhusu);
        adapter.setOnItemClickListener(this);
        v7_recyerview.setAdapter(adapter);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void getData() {

    }

    @Override
    public void setOnClick() {
        refreshLayout.setOnRefreshListener(this);
        adapter.setOnItemClickListener(this);
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
    public void onRefresh(int index) {
        dataOption(TOP_REFRESH);
        ToastUtil.shortDiyToastColorBgTextSizeTextColor(mContext, "已是最新数据了", getResources().getColor(R.color.transparent), 16f, getResources().getColor(R.color.black));
    }

    @Override
    public void onLoad(int index) {
        dataOption(BOTTOM_REFRESH);
        ToastUtil.shortDiyToastColorBgTextSizeTextColor(mContext, "加载完成", getResources().getColor(R.color.transparent), 16f, getResources().getColor(R.color.black));
    }
    private void dataOption(int option){
        switch (option) {
            case TOP_REFRESH:
                //下拉刷新
                initData();
                break;
            case BOTTOM_REFRESH:
                //上拉加载更多
                initData();
                break;
        }
        // adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, UserManagerBean data) {
        if (data != null){
            data.setAge("40");
            data.setSex("男");
            Bundle bundle = new Bundle();
            bundle.putString("name",data.getName());
            bundle.putString("age",data.getAge());
            bundle.putString("sex",data.getSex());
            bundle.putInt("imgId",data.getImgId());
            bundle.putString("zhusu",data.getZhusu());
            MyApplication.startActivity(UserManagerActivity.this,UserInformationActivity.class,bundle);
//            ToastUtil.shortDiyToastColorBgTextSizeTextColor(mContext, data.getName(), getResources().getColor(R.color.transparent), 16f, getResources().getColor(R.color.black));
        }
    }
}
