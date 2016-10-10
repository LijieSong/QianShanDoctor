package com.example.bjlz.qianshandoctor.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.CommonUtil;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.StringUtil;
import com.example.bjlz.qianshandoctor.utils.OtherTools.KeyboardUtil;
import com.example.bjlz.qianshandoctor.utils.OtherTools.LogUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.PreferencesUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;
import com.example.bjlz.qianshandoctor.utils.OtherTools.UserUtils;
import com.example.bjlz.qianshandoctor.views.ClearEditText;
import com.example.bjlz.qianshandoctor.views.TitleBarView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jtech.view.RecyclerHolder;

import java.util.Map;

/**
 * 项目名称：qianshandoctor
 * 类描述：LoginActivity :登录界面
 * 创建人：slj
 * 创建时间：2016-5-28 17:24
 * 修改人：slj
 * 修改时间：2016-5-28 17:24
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class LoginActivity extends BaseActivity {
    // implements View.OnTouchListener
    private ClearEditText user_name, user_password;
    private Button btn_login, btn_registered;//登录/注册按钮
    private String name, word, userName, userPwd;//用户名 密码
    private TitleBarView title_bar;
    private Map<String, String> map = null;//存储用户名密码
    // 弹出框
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyApplication.getInstance().addActivity(this);
        getData();
        initView();
        initData();
        setOnClick();
    }

    @Override
    public void initView() {
        this.title_bar = (TitleBarView) findViewById(R.id.title_bar);
        user_name = (ClearEditText) findViewById(R.id.user_name);
        user_password = (ClearEditText) findViewById(R.id.user_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_registered = (Button) findViewById(R.id.btn_registered);
    }

    @Override
    public void initData() {
        this.title_bar.setTitleText(R.string.login);
        this.title_bar.getTitleBarBg().setBackgroundColor(getResources().getColor(R.color.transparent));
        if (name != null || word != null) {
//            name = StringUtil.hideMobileNumber(name);
            user_name.setText(name);
            user_password.setText(word);
        }
    }

    @Override
    public void getData() {
        map = UserUtils.readInfo(getApplicationContext());
        if (map != null) {
            name = map.get("name");
            word = map.get("word");
        }
    }

    //设置事件监听
    @Override
    public void setOnClick() {
        btn_registered.setOnClickListener(this);
        btn_login.setOnClickListener(this);
//        user_name.setOnTouchListener(this);
    }

    @Override
    public void WeightOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userName = user_name.getText().toString().trim();
                userPwd = user_password.getText().toString().trim();
                if (userName == null || TextUtils.isEmpty(userName)) {
                    ToastUtil.shortToastByRes(getApplicationContext(), R.string.name_is_not_allow_null);
                } else if (userPwd == null || TextUtils.isEmpty(userPwd)) {
                    ToastUtil.shortToastByRes(LoginActivity.this, R.string.pwd_is_not_allow_null);
                } else {

//                    if (NetworkUtils.isConnected(getApplicationContext()) ){
//                        if (NetworkUtils.isWifi(getApplicationContext())){
////                            ToastUtil.shortToastBackImgStrByRes(LoginActivity.this, R.string.isWifi, R.drawable.bgtoast);
//                        } else if (NetworkUtils.isMobileConnected(getApplicationContext())){
//                            ToastUtil.shortToastByRes(getApplicationContext(),R.string.isMobile);
//                        } else if (NetworkUtils.isNetworkConnected(getApplicationContext())){
//                            ToastUtil.shortToastByRes(getApplicationContext(),R.string.isMobileNet);
//                        }
//                    Map<String,String> param = new HashMap<>();
//                    param.put("userName",userName);
//                    param.put("password",userPwd);
//                    VolleyRequestUtil.RequestPost(LoginActivity.this, Address.Login_Url, "login", param, new VolleyListenerInterface(LoginActivity.this, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    }) {
//                        @Override
//                        public void onMySuccess(String result) {
//                            LogUtils.error("result:" +result);
//                            if (result != null) {
//                                try {
//                                    JSONObject obj = new JSONObject(result);
//                                    boolean msg = obj.getBoolean("msg");
//                                    if (msg == true) {
//                                        String userId = obj.getString("sysusername");
//                                        String data = obj.getString("menuList");
//                                        ToastUtil.shortToastByRes(LoginActivity.this, R.string.login_success);
//                                        UserUtils.saveInfo(getApplicationContext(), LoginActivity.this.userName, userPwd);
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("userName",userId);
//                                        MyApplication.startActivity(LoginActivity.this, MainActivity.class,bundle);
//                                            PreferencesUtils.putString(getApplicationContext(), "userName", userId);
//                                        PreferencesUtils.putBoolean(getApplicationContext(), "isLogin", msg);
//                                        PreferencesUtils.putString(getApplicationContext(), "data", data);
//                                    } else {
//                                        ToastUtil.shortToastByRes(getApplicationContext(),R.string.failed_login);
//                                    }
//                                } catch (Exception e) {
//                                    ToastUtil.shortToastByRes(getApplicationContext(),R.string.notContent);
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                ToastUtil.shortToastByRes(getApplicationContext(),R.string.failed_login);
//                            }
//                        }
//
//                        @Override
//                        public void onMyError(VolleyError error) {
//                            LogUtils.error("error:" +error);
//                            ToastUtil.shortDiyToast(getApplicationContext(),VolleyErrorHelper.getMessage(error,getApplicationContext()));
////                            ToastUtil.shortToastByRes(getApplicationContext(),R.string.failed_login);
//                        }
//                    });
//                    } else {
//                        ToastUtil.shortToastByRes(getApplicationContext(),R.string.notContent);
//                    }

                    // 登录是耗时过程，所以要显示一个dialog来提示下用户
                    mDialog = new ProgressDialog(this);
                    mDialog.setMessage("登录中，请稍后...");
                    mDialog.show();
                    EMClient.getInstance().login(userName, userPwd, new EMCallBack() {//回调
                        @Override
                        public void onSuccess() {
                            LogUtils.error("登录成功");
                            UserUtils.saveInfo(getApplicationContext(), LoginActivity.this.userName, userPwd);
                            Bundle bundle = new Bundle();
                            bundle.putString("userName", userName);
                            mDialog.dismiss();
                            CommonUtil.runOnUIThreadToast(LoginActivity.this, R.string.login_success);
                            MyApplication.startActivity(LoginActivity.this, MainActivity.class, bundle);
                            PreferencesUtils.putBoolean(getApplicationContext(), "isLogin", true);
                            PreferencesUtils.putUserId(getApplicationContext(), userName);
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }

                        @Override
                        public void onError(int code, final String message) {
                            mDialog.dismiss();
                            CommonUtil.runOnUIThreadToast(LoginActivity.this, "登录失败:" + message);
                            LogUtils.error("code:" + code + "---message:" + message);
                        }
                    });
//                    UserUtils.saveInfo(getApplicationContext(), LoginActivity.this.userName, userPwd);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("userName", userName);
//                    ToastUtil.shortToastByRes(LoginActivity.this, R.string.login_success);
//                    MyApplication.startActivity(LoginActivity.this, MainActivity.class, bundle);
//                    PreferencesUtils.putBoolean(getApplicationContext(), "isLogin", true);
//                    PreferencesUtils.putString(getApplicationContext(), "userName", userName);
                }
                break;
            case R.id.btn_registered:
                MyApplication.startActivity(LoginActivity.this, RegisterActivity.class);
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

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//            //这样是在触摸到控件时，软键盘才会显示出来
//            user_name.setInputType(InputType.TYPE_NULL);
//            new KeyboardUtil(LoginActivity.this, LoginActivity.this, user_name).showKeyboard();
//            int inputback = user_name.getInputType();
//            user_name.setInputType(inputback);
//        return false;
//    }
}
