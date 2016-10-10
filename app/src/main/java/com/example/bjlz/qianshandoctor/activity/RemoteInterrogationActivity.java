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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.adapter.MessageAdapter;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.bean.MsgBean;
import com.example.bjlz.qianshandoctor.pull.SwipyRefreshLayout;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.CommonUtil;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.DataUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.LogUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.PreferencesUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;
import com.example.bjlz.qianshandoctor.views.recycleView.DividerItemDecoration;
import com.example.bjlz.qianshandoctor.views.TitleBarView;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.jtech.view.RecyclerHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：QianShanDoctor
 * 类描述：RemoteInterrogationActivity 远程问诊
 * 创建人：slj
 * 创建时间：2016-8-22 15:09
 * 修改人：slj
 * 修改时间：2016-8-22 15:09
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class RemoteInterrogationActivity extends BaseActivity  implements SwipyRefreshLayout.OnRefreshListener,MessageAdapter.OnRecyclerViewItemClickListener,MessageAdapter.OnRecyclerViewLongItemClickListener,EMMessageListener{
    private TitleBarView title_bar;//标题
    private Context mContext = null;//上下文对象
    private RecyclerView jRecyclerView;//recycleview
    //临时的消息展示
//    private List<String> usernames;//好友名称
    private List<String> msgContext; //消息内容
    private List<String> msgTime;//消息时间
    private MessageAdapter adapter;//待处理消息匹配器
    private SwipyRefreshLayout swipyRefreshLayout;//刷新控件
    private String userId = null;//用户UserId
    private final int TOP_REFRESH = 1;//下拉刷新
    private final int BOTTOM_REFRESH = 2;//上拉加载更多

    // 消息监听器
    private EMMessageListener mMessageListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_interrogation);
        MyApplication.getInstance().addActivity(this);
        mContext = this;
        mMessageListener=this;
        getData();
        initView();
        initData();
        setOnClick();
    }

    @Override
    public void initView() {
        this.title_bar = (TitleBarView) findViewById(R.id.title_bar);
        jRecyclerView = (RecyclerView) findViewById(R.id.jrecycleView);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyRefreshLayout);
        //初始化recycleview
        LinearLayoutManager layout = new LinearLayoutManager(this);
        jRecyclerView.setLayoutManager(layout);
        //设置为垂直布局，这也是默认的
        layout.setOrientation(OrientationHelper.VERTICAL);
        jRecyclerView.setHasFixedSize(true);
        jRecyclerView.addItemDecoration(new DividerItemDecoration(this,layout.getOrientation(), CommonUtil.getColor(R.color.devide_line)));
    }
    @Override
    public void initData() {
        this.title_bar.setTitleText("好友列表");
        this.title_bar.setRightBtnIcon(R.drawable.addfriend);
//        try {
//            usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//            LogUtils.error("users:" +usernames);
//        } catch (HyphenateException e) {
//            e.printStackTrace();
//        }
        //待处理消息
        adapter = new MessageAdapter(this,msgContext,msgTime);
//        adapter = new MessageAdapter(this,usernames);
        jRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        swipyRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getData() {
        userId = PreferencesUtils.getUserId(getApplicationContext());
        msgContext = new ArrayList<>();
        msgContext.add("晓明");
        msgContext.add("小名");
        msgContext.add("小明");
        msgContext.add("小敏");
        msgContext.add("张三");
        msgContext.add("李四");
        msgContext.add("王五");
        msgContext.add("赵六");
        msgContext.add("李逵");
        msgContext.add("宋江");
        msgContext.add("阮小二");
        msgContext.add("阮晓东");
        msgContext.add("13782551343");
        msgContext.add("15712881338");
        msgTime = new ArrayList<>();
        msgTime.add("2016-08-02 08:58:38");
        msgTime.add("2016-08-02 08:40:32");
        msgTime.add("2016-08-02 08:58:38");
        msgTime.add("2016-08-02 08:40:32");
        msgTime.add("2016-08-02 08:58:38");
        msgTime.add("2016-08-02 08:40:32");
        msgTime.add("2016-08-02 08:58:38");
        msgTime.add("2016-08-02 08:40:32");
        msgTime.add("2016-08-02 08:38:36");
        msgTime.add("2016-08-02 08:46:34");
        msgTime.add("2016-08-03 08:40:35");
        msgTime.add("2016-08-03 08:45:55");
        msgTime.add("2016-08-03 11:45:55");
        msgTime.add("2016-08-03 11:45:55");
    }

    @Override
    public void setOnClick() {
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        swipyRefreshLayout.setOnRefreshListener(this);
        this.title_bar.setRightBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.startActivityForResult(RemoteInterrogationActivity.this,AddFriendActivity.class,50);
            }
        });
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
    public void onItemClick(View view, MsgBean data) {

        if (!userId.equals(data.getMsg())) {
            Bundle bundle = new Bundle();
            bundle.putString("name",data.getMsg());
            MyApplication.startActivity(RemoteInterrogationActivity.this, ChatActivity.class,bundle);
        }else
            ToastUtil.shortDiyToastByRec(getApplicationContext(),R.string.Cant_chat_with_yourself);
    }

    @Override
    public void onItemLongClick(View view, final MsgBean data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("确定删除该好友吗?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    EMClient.getInstance().contactManager().deleteContact(data.getMsg());
                    adapter.removeMessage();
                    onRefresh(TOP_REFRESH);
                    ToastUtil.shortDiyToast(getBaseContext(),"删除好友成功!");
                    dialog.dismiss();
                } catch (HyphenateException e) {
                    ToastUtil.shortDiyToast(getBaseContext(),"删除好友失败:"+e.getMessage());
                    onRefresh(TOP_REFRESH);
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //@Override
//public void onItemClick(View view, String data) {
//
//    if (!userId.equals(data)) {
//        Bundle bundle = new Bundle();
//        bundle.putString("name",data);
//        MyApplication.startActivity(RemoteInterrogationActivity.this, ChatActivity.class,bundle);
//    }else
//        ToastUtil.shortDiyToastByRec(getApplicationContext(),R.string.Cant_chat_with_yourself);
//}
    @Override
    public void onRefresh(int index) {
        dataOption(TOP_REFRESH);
        ToastUtil.shortDiyToastByRec(getApplicationContext(),R.string.data_is_new);
    }

    @Override
    public void onLoad(int index) {
        dataOption(BOTTOM_REFRESH);
        ToastUtil.shortDiyToastByRec(getApplicationContext(),R.string.data_is_load_over);
    }
    private void dataOption(int option){
        switch (option) {
            case TOP_REFRESH:
                //下拉刷新
                initData();
                break;
            case BOTTOM_REFRESH:
                //上拉加载更多
//                getData();
                initData();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userId = PreferencesUtils.getUserId(getApplicationContext());
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (final EMMessage msg:list) {
            LogUtils.error("来自:" + msg.getFrom() + ",消息内容为:" + msg.getBody());
            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar snackbar = Snackbar.make(RemoteInterrogationActivity.this.title_bar, "来自:" + msg.getFrom() + ",消息内容为:" + msg.getBody(), Snackbar.LENGTH_SHORT);
                    snackbar.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("name",msg.getFrom());
                            MyApplication.startActivity(RemoteInterrogationActivity.this, ChatActivity.class,bundle);
                        }
                    });
                    snackbar.show();
                }
            });
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {


    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMessageListener!=null)
            EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
             switch(requestCode){
                case 50:
                    String friend = data.getStringExtra("friend");
                    msgContext.add(friend);
                    msgTime.add(DataUtils.getStringDate());
                    adapter.notifyDataSetChanged();
                    onRefresh(TOP_REFRESH);
                    break;
        }
    }
}
