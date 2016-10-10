package com.example.bjlz.qianshandoctor.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.example.bjlz.qianshandoctor.R;
//import com.example.bjlz.qianshandoctor.receiver.CallReceiver;
import com.example.bjlz.qianshandoctor.utils.OtherTools.LogUtils;
import com.example.bjlz.qianshandoctor.utils.appManager.CrashHandler;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * 项目名称：QianShanDoctor
 * 类描述：MyApplication 全局变量
 * 创建人：slj
 * 创建时间：2016-6-27 20:06
 * 修改人：slj
 * 修改时间：2016-6-27 20:06
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class MyApplication extends Application {

    public static Map<String, Long> map;//用来存放倒计时的时间
    private List<Activity> activityList = new LinkedList<Activity>();//activity管理器
    private static Handler mainHandler = new Handler();
    private static MyApplication mApplication;//application
    public static RequestQueue queue;//请求队列
    private static Context mContext;//全局变量
    public boolean isVoiceCalling;
    public boolean isVideoCalling;

    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mContext = this;
        //分包
        MultiDex.install(mContext);
        //图片加载
        Fresco.initialize(this);
//        极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //请求
        queue = Volley.newRequestQueue(this);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            LogUtils.error("enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        options.setAutoLogin(true);//设置自动登录
//        // 设置是否需要发送已读回执
//        options.setRequireAck(true);
//        // 设置是否需要发送回执，TODO 这个暂时有bug，上层收不到发送回执
//        options.setRequireDeliveryAck(true);
//        // 设置是否需要服务器收到消息确认
//        options.setRequireServerAck(true);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
//        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
//        options.setAutoAcceptGroupInvitation(false);
//        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
//        options.setDeleteMessagesAsExitGroup(false);
//        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
//        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);

        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        //初始化百度地图
        SDKInitializer.initialize(mContext);

//        bMOb后端云
        //第一：默认初始化
//        Bmob.initialize(mContext, "5fffefc9d3fdc2a9f5410bc22c0e4312");
        Bmob.initialize(mContext,Address.BMobKey);
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

        //初始化异常收集器
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(mContext);
    }

    /**
     * 获取请求队列
     * @return
     */
    public static RequestQueue getHttpQueue() {
        return queue;
    }
    /**
     * 获取全局变量
     *
     * @return
     */
    public static MyApplication getInstance() {
        return mApplication;
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取主线程的handler
     * @return
     */
    public static Handler getHandler() {
        return mainHandler;
    }

//    public static void regiestBroadcastReceiver(Context activity){
//        CallReceiver callReceiver;
//        callReceiver = new CallReceiver();
//        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
//        //注册通话广播接收者
//        activity.registerReceiver(callReceiver, callFilter);
//    }

    /**
     * 无参跳转
     *
     * @param activity
     * @param clazz
     */
    public static <T> void startActivity(Activity activity, Class<T> clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.cuntomanim, R.anim.cunexitmanim);
    }
    /**
     * 无参跳转
     *
     * @param context
     * @param clazz
     */
    public static <T> void startActivity(Context context, Class<T> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    /**
     * 有参跳转  传递bundle
     *
     * @param activity
     * @param clazz
     * @param bundle
     */
    public static <T> void startActivity(Activity activity, Class<T> clazz, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtra("bundle", bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.cuntomanim, R.anim.cunexitmanim);
    }
    /**
     * 有参跳转  传递bundle
     *
     * @param context
     * @param clazz
     * @param bundle
     */
    public static <T> void startActivity(Context context, Class<T> clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    /**
     * 开启服务
     *
     * @param activity
     * @param serviceClazz
     */
    public static <T> void startService(Activity activity, Class<T> serviceClazz) {
        Intent intent = new Intent(activity, serviceClazz);
        activity.startService(intent);
    }

    /**
     * 停止服务
     *
     * @param activity
     * @param serviceClazz
     */
    public static <T> void stopService(Activity activity, Class<T> serviceClazz) {
        Intent intent = new Intent(activity, serviceClazz);
        activity.stopService(intent);

    }

    /**
     * 带请求码的跳转方式
     *
     * @param activity
     * @param clazz
     * @param requestCode
     */
    public static <T> void startActivityForResult(Activity activity, Class<T> clazz,
                                                  int requestCode) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.cuntomanim, R.anim.cunexitmanim);
    }

    /**
     * 添加Activity到容器中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 关闭所有activity
     */
    public void close() {
        for (Activity activity : activityList) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityList = new LinkedList<Activity>();
    }

    /**
     * 移除activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        for (Activity ac : activityList) {
            if (ac.equals(activity)) {
                activityList.remove(ac);
                break;
            }
        }
    }

    /**
     * 获取app的名字
     * @param pID 进程ID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
