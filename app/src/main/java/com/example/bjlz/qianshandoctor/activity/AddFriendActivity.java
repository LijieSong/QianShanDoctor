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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.OtherTools.LogUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.PreferencesUtils;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.StringUtil;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;
import com.example.bjlz.qianshandoctor.views.ClearEditText;
import com.example.bjlz.qianshandoctor.views.TitleBarView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jtech.view.RecyclerHolder;

/**
 * 项目名称：QianShanDoctor
 * 类描述：
 * 创建人：slj
 * 创建时间：2016-9-19 16:36
 * 修改人：slj
 * 修改时间：2016-9-19 16:36
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class AddFriendActivity extends BaseActivity {
    private TitleBarView title_bar;//标题
    private Context mContext = null;//上下文对象
    private ClearEditText cdt_add_friend, cdt_add_friend_context;//添加好友的账号/原因
    private Button btn_add;//添加好友
    private String userId = null;//用户UserId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
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
        cdt_add_friend = (ClearEditText) findViewById(R.id.cdt_add_friend);
        cdt_add_friend_context = (ClearEditText) findViewById(R.id.cdt_add_friend_context);
        btn_add = (Button) findViewById(R.id.btn_add);
    }

    @Override
    public void initData() {
        this.title_bar.setTitleText(R.string.add_friend);
    }

    @Override
    public void getData() {
        userId = PreferencesUtils.getUserId(getApplicationContext());
    }

    @Override
    public void setOnClick() {
        btn_add.setOnClickListener(this);
    }

    @Override
    public void WeightOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                final String user = cdt_add_friend.getText().toString().trim();
                String context = cdt_add_friend_context.getText().toString().trim();
                if (user.equals(userId)) {
                    ToastUtil.shortDiyToastByRec(getBaseContext(), R.string.not_add_myself);
                    return;
                }
                if (user == null || TextUtils.isEmpty(user)) {
                    ToastUtil.shortDiyToastByRec(getBaseContext(), R.string.User_name_cannot_be_empty);
                } else {
                    if (StringUtil.isNumber(user)) {
                        //参数为要添加的好友的username和添加理由
                        try {
                            EMClient.getInstance().contactManager().addContact(user, context);
                            Intent intent = new Intent();
                            intent.putExtra("friend", user);
                            AddFriendActivity.this.setResult(RESULT_OK, intent);
                            AddFriendActivity.this.finish();
                        } catch (HyphenateException e) {
                            LogUtils.error("失败! ===code:"+e.getMessage());
                            ToastUtil.shortDiyToast(getBaseContext(),"添加好友失败:"+e.getMessage());
                            finish();
                            e.printStackTrace();
                        }
                    }else{
                        ToastUtil.shortDiyToastByRec(getBaseContext(),R.string.illegal_user_name);
                    }
                }
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
