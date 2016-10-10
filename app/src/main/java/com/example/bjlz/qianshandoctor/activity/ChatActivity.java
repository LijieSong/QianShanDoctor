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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.bjlz.qianshandoctor.Manifest;
import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.adapter.ChatMessageAdapter;
import com.example.bjlz.qianshandoctor.adapter.GradViewAdapter;
import com.example.bjlz.qianshandoctor.application.EaseCommonUtils;
import com.example.bjlz.qianshandoctor.application.EaseConstant;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.BitmapAndFilesTools.FileUtils;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.CommonUtil;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.DataUtils;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.StringUtil;
import com.example.bjlz.qianshandoctor.utils.OtherTools.LogUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ScreenUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.SystemUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;
import com.example.bjlz.qianshandoctor.utils.PermissionsManager.EasyPermissionsEx;
import com.example.bjlz.qianshandoctor.views.CustomDialog;
import com.example.bjlz.qianshandoctor.views.TitleBarView;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.PathUtil;
import com.jtech.view.RecyclerHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：QianShanDoctor
 * 类描述：ChatActivity 聊天界面
 * 创建人：slj
 * 创建时间：2016-8-25 11:14
 * 修改人：slj
 * 修改时间：2016-8-25 11:14
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class ChatActivity extends BaseActivity implements AdapterView.OnItemClickListener,EMMessageListener,SwipeRefreshLayout.OnRefreshListener{
    private TitleBarView title_bar;//标题
    private Context mContext = null;//上下文对象
    private String toChatUsername =null;//要会话列表的人的名字
    private ImageButton btn_voice,btn_open;//打开语音/打开列表
    private EditText edit_content;//输入框
    private Button btn_send,btn_send_voice;//输入/语音
    private LinearLayout ll_voice;//语音控制
    private GridView gridView_Chat_btn;//额外按钮
    private ListView list_chat;//消息列表
    private SwipeRefreshLayout chat_swipe_layout;//刷新控件
    //语音操作对象
    private MediaRecorder mRecorder = null;
    //获取聊天记录
    protected int pagesize = 20;
    protected boolean isloading;
    protected boolean haveMoreData = true;
    //请求码
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected File cameraFile;//照相的地址

    protected int chatType = EaseConstant.CHATTYPE_SINGLE;//聊天类型
    // 当前会话对象
    private EMConversation mConversation;
    // 消息监听器
    private EMMessageListener mMessageListener;

    private GradViewAdapter gradradadapter;//按钮匹配器
    private int[] gride_image = {R.drawable.logo, R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo};
    private String[] gride_name = {"拍照", "图片", "位置", "视频", "文件", "语音电话","视频电话"};
    private ChatMessageAdapter messageAdapter;//消息列表匹配器
        //    权限请求吗
    private static final int STORAGE_PERMISSION = 0X23;
    private MyContentListener contentListener; //聊天的连接监听
    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    //屏幕高度
    int screenHeight = 0;
    //语音文件保存路径
    private String VoiceFiles = null;
    private File file=null;//文件
    //录音开始时间
    private long startTime = 0;
    //录音结束时间
    private long endTime = 0;
    //录音时长
    private int voiceTime = 0;

    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
//                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
//                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    // 将新的消息内容和时间加入到下边
                    mConversation.markAllMessagesAsRead();
                    mConversation.getAllMessages().add(message);
                    EMClient.getInstance().chatManager().importMessages(mConversation.getAllMessages());
                    messageAdapter.notifyDataSetChanged();
                    list_chat.setAdapter(messageAdapter);
                    list_chat.setSelection(messageAdapter.getCount() - 1);
                    break;
                case 1:
                    Integer msgid = (Integer) msg.obj;
                    mConversation.markAllMessagesAsRead();
                    mConversation.getAllMessages().remove(msgid);
                    messageAdapter.notifyDataSetChanged();
                    list_chat.setAdapter(messageAdapter);
                    list_chat.setSelection(messageAdapter.getCount() - 1);
                    break;
                case 2:
//                    EMMessage mesage = (EMMessage) msg.obj;
//                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
//                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    // 将新的消息内容和时间加入到下边
//                    mConversation.markAllMessagesAsRead();
//                    mConversation.getAllMessages().add(mesage);

                    messageAdapter.notifyDataSetChanged();
                    list_chat.setAdapter(messageAdapter);
                    chat_swipe_layout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        MyApplication.getInstance().addActivity(this);
        mContext = this;
        mMessageListener=this;
//        if (EasyPermissionsEx.hasPermissions(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
//            EasyPermissionsEx.requestPermissions(ChatActivity.this,"Storage",100,android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
        getData();
        initView();
        initData();
        setOnClick();
    }
    @Override
    public void initView() {
        this.title_bar = (TitleBarView) findViewById(R.id.title_bar);
        btn_voice = (ImageButton) findViewById(R.id.btn_voice);
        btn_open = (ImageButton) findViewById(R.id.btn_open);
        edit_content = (EditText) findViewById(R.id.edit_content);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send_voice = (Button) findViewById(R.id.btn_send_voice);
        ll_voice = (LinearLayout) findViewById(R.id.ll_voice);
        gridView_Chat_btn = (GridView) findViewById(R.id.gridView_Chat_btn);
        list_chat = (ListView) findViewById(R.id.list_chat);
        chat_swipe_layout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
    }



    @Override
    public void initData() {
        this.title_bar.setTitleText(toChatUsername);
        this.title_bar.setRightBtnIcon(R.drawable.delete);
        /**
         * 主页面的适配器
         */
        List<HashMap<String, Object>> gradeViewList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < gride_image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", gride_image[i]);
            map.put("text", gride_name[i]);
            gradeViewList.add(map);
        }
        gradradadapter = new GradViewAdapter(mContext, gradeViewList);
        gridView_Chat_btn.setAdapter(gradradadapter);
        contentListener = new MyContentListener();
        messageAdapter = new ChatMessageAdapter(ChatActivity.this,mConversation);
        list_chat.setAdapter(messageAdapter);
        list_chat.setSelection(messageAdapter.getCount() - 1);
        chat_swipe_layout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
    }

    @Override
    public void getData() {
        //设置sdcard的路径
        screenHeight = ScreenUtils.getScreenHeight(this);
        toChatUsername =getIntent().getBundleExtra("bundle").getString("name");
        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EMConversation.EMConversationType.Chat, true);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
//      // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = mConversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < mConversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            mConversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }
    }

    @Override
    public void setOnClick() {
        btn_voice.setOnClickListener(this);
        btn_open.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_send_voice.setOnClickListener(this);
        chat_swipe_layout.setOnRefreshListener(this);
        gridView_Chat_btn.setOnItemClickListener(this);
        this.title_bar.setRightBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomDialog.Builder builder = new CustomDialog.Builder(ChatActivity.this);
                builder.setMessage("确定要清空消息列表么?");
                builder.setTitle("温馨提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //删除当前会话的某条聊天记录
                        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername);
                        conversation.clearAllMessages();
                        messageAdapter.notifyDataSetChanged();
                        list_chat.setAdapter(messageAdapter);
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
        });
        this.title_bar.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageAdapter!=null){
                    messageAdapter.stopPlayVoice();
                }
                finish();
            }
        });
        btn_send_voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mRecorder.setAudioChannels(1); // MONO
                        mRecorder.setAudioSamplingRate(8000); // 8000Hz
                        mRecorder.setAudioEncodingBitRate(64); // seems if change this to
                        // 128, still got same file
                        // size.
//                        VoiceFiles = PathUtil.getInstance().getVoicePath() + "/" + toChatUsername;
                        VoiceFiles = PathUtil.getInstance().getVoicePath() + "/" + System.currentTimeMillis();
                        file = new File(VoiceFiles);
                        mRecorder.setOutputFile(file.getAbsolutePath());
                        LogUtils.error("activityFileName:"+file.getAbsolutePath());
                        try {
                            mRecorder.prepare();
                        } catch (IOException e) {
                            LogUtils.error("prepare() failed:"+e.getMessage());
                        }
                        LogUtils.error("开始录音");
                        startTime = System.currentTimeMillis();
                        mRecorder.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        mRecorder.stop();
                        mRecorder.release();
                        mRecorder = null;
                        endTime = System.currentTimeMillis();
                        LogUtils.error("录音结束");
                        voiceTime = (int)(endTime -startTime);
                        sendVoiceMessage(file.getAbsolutePath(), voiceTime);
                        break;
                }
                return false;
            }
        });
//        list_chat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                final CustomDialog.Builder builder = new CustomDialog.Builder(ChatActivity.this);
//                builder.setMessage("确定删除/撤回此条消息么?");
//                builder.setTitle("温馨提示");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        list_chat.getSelectedItemPosition();
//                        dialog.dismiss();
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = 1;
//                        msg.obj=list_chat.getSelectedItemPosition();
//                        mHandler.sendMessage(msg);
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.create().show();
//                return true;
//            }
//        });
    }

    @Override
    public void WeightOnClick(View v) {
    switch(v.getId()){
        case R.id.btn_open: //打开按钮列表
            if (gridView_Chat_btn.getVisibility() == View.GONE) {
                if (ll_voice.getVisibility() != View.GONE)
                    ll_voice.setVisibility(View.GONE);
                    gridView_Chat_btn.setVisibility(View.VISIBLE);
            }else
                gridView_Chat_btn.setVisibility(View.GONE);
                ll_voice.setVisibility(View.GONE);
        break;
        case R.id.btn_voice: //打开语音
            if (ll_voice.getVisibility() == View.GONE) {
                if (gridView_Chat_btn.getVisibility() != View.GONE)
                    gridView_Chat_btn.setVisibility(View.GONE);
                    ll_voice.setVisibility(View.VISIBLE);
            }  else
                ll_voice.setVisibility(View.GONE);
                 gridView_Chat_btn.setVisibility(View.GONE);
            break;
        case R.id.btn_send: //发送消息
            String trim = edit_content.getText().toString().trim();
            if (trim!=null || !TextUtils.isEmpty(trim) || trim.equals(""))
                    sendTextMessage(trim);
            else
                ToastUtil.shortDiyToast(ChatActivity.this,"消息不能为空");
            break;
        case R.id.btn_send_voice: //发送消息
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

    /**
     * 收到新消息
     *
     * @param list 收到的新消息集合
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        // 循环遍历当前收到的消息
        for (EMMessage message : list) {
            if (message.getFrom().equals(toChatUsername)) {
                // 设置消息为已读
                mConversation.markMessageAsRead(message.getMsgId());
                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            } else {
                LogUtils.error("message.getBody():"+message);
                // 如果消息不是当前会话的消息发送通知栏通知
                Snackbar.make(edit_content,"来自:"+message.getFrom()+",消息内容为:"+message.getBody(),Snackbar.LENGTH_SHORT).show();
//                CommonUtil.runOnUIThreadToast(getApplicationContext(),"有新消息:"+message.getBody().toString().split(":")[1]+"--来自"+message.getFrom());
            }
        }
    }

    /**
     * 收到新的 CMD 消息
     *
     * @param list
     */
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            // 透传消息
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
            LogUtils.error("body.action():"+body.action());
        }
    }

    /**
     * 收到新的已读回执
     *
     * @param list 收到消息已读回执
     */
    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {
    }

    /**
     * 收到新的发送回执
     * TODO 无效 暂时有bug
     *
     * @param list 收到发送回执的消息集合
     */
    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {
    }

    /**
     * 消息的状态改变
     *
     * @param message 发生改变的消息
     * @param object  包含改变的消息
     */
    @Override
    public void onMessageChanged(EMMessage message, Object object) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
        EMClient.getInstance().addConnectionListener(contentListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(gride_name[position]){
            //{"拍照", "图片", "位置", "视频", "文件", "语音电话","视频电话"};
            case "拍照":
                  selectPicFromCamera();
            break;
            case "图片":
                    selectPicFromLocal();
                break;
            case "位置":
                MyApplication.startActivityForResult(ChatActivity.this,BaiduMapActivity.class,REQUEST_CODE_MAP);
//                MyApplication.startActivityForResult(ChatActivity.this,EaseBaiduMapActivity.class,REQUEST_CODE_MAP);
                break;
            case "视频":

                break;
            case "文件":
                selectFileFromLocal();//文件
                break;
            case "语音电话":

                break;
            case "视频电话":

                break;
        }
    }
    //send message
    protected void sendTextMessage(String content) {
        if (content !=null){
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
            sendMessage(message);
        }else{
            ToastUtil.shortDiyToast(ChatActivity.this,"消息不能为空");
        }
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }
    protected void sendMessage(final EMMessage message){
        if (message == null) {
            return;
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP){
            message.setChatType(EMMessage.ChatType.GroupChat);
        }else if(chatType == EaseConstant.CHATTYPE_CHATROOM){
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
        edit_content.setText(null);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                // 消息发送成功，打印下日志，正常操作应该去刷新ui
                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {
                // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onProgress(int i, String s) {
            // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            ll_voice.setVisibility(View.GONE);
            gridView_Chat_btn.setVisibility(View.GONE);
            switch (requestCode){
                case REQUEST_CODE_CAMERA: // 发送照片
                    if (cameraFile != null && cameraFile.exists())
                        sendImageMessage(cameraFile.getAbsolutePath());
                    break;
                case REQUEST_CODE_LOCAL:// 发送本地图片
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            sendPicByUri(selectedImage);
                        }
                    }
                    break;
                case REQUEST_CODE_MAP://发送位置
                    double latitude = data.getDoubleExtra("latitude", 0);
                    double longitude = data.getDoubleExtra("longitude", 0);
                    String locationAddress = data.getStringExtra("address");
                    if (locationAddress != null && !locationAddress.equals("")) {
                        sendLocationMessage(latitude, longitude, locationAddress);
                    } else {
                        ToastUtil.shortDiyToastByRec(getApplicationContext(),R.string.unable_to_get_loaction);
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE://发送文件
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case  REQUEST_CODE_SELECT_VIDEO:
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (list_chat.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                    List<EMMessage> messages = null;
                    try {
                        if (chatType == EaseConstant.CHATTYPE_SINGLE ) {
                            messages = mConversation.loadMoreMsgFromDB(messageAdapter.getItem(0).getMsgId(),
                                    pagesize);
                        } else{
                            messages = mConversation.loadMoreMsgFromDB(messageAdapter.getItem(0).getMsgId(),
                                    pagesize);
                        }
                    } catch (Exception e1) {
                        chat_swipe_layout.setRefreshing(false);
                        return;
                    }
                    if (messages.size() > 0) {
                        list_chat.setSelection(messages.size() - 1);
                        if (messages.size() != pagesize) {
                            haveMoreData = false;
                        }
                    } else {
                        haveMoreData = false;
                    }
                    isloading = false;

                } else {
                    ToastUtil.shortDiyToast(getBaseContext(),CommonUtil.getString(R.string.no_more_messages));
                }
                chat_swipe_layout.setRefreshing(false);
            }
        }, 600);
    }

    private class MyContentListener implements EMConnectionListener {

        @Override
        public void onConnected() {
//            CommonUtil.runOnUIThreadToast(getApplicationContext(),"连接聊天服务器成功");
        }

        @Override
        public void onDisconnected(int i) {
            CommonUtil.runOnUIThreadToast(getApplicationContext(),"与聊天服务器失去连接,请重新登录");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(contentListener);
    }
    /**
     * 照相获取图片
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isExitsSdcard()) {
            ToastUtil.shortDiyToastByRec(getBaseContext(),R.string.sd_card_does_not_exist);
            return;
        }
        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }
    /**
     * 从图库获取图片
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }
    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                ToastUtil.shortDiyToastByRec(getBaseContext(),R.string.cant_find_pictures);
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                ToastUtil.shortDiyToastByRec(getBaseContext(),R.string.cant_find_pictures);
                return;
            }
            sendImageMessage(file.getAbsolutePath());
        }

    }
    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }
    /**
     * 根据uri发送文件
     * @param uri
     */
    protected void sendFileByUri(Uri uri){
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;

            try {
                cursor = mContext.getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            ToastUtil.shortDiyToastByRec(getApplicationContext(),R.string.File_does_not_exist);
            return;
        }
        //大于10M不让发送
        if (file.length() > 10 * 1024 * 1024) {
            ToastUtil.shortDiyToastByRec(getApplicationContext(),R.string.The_file_is_not_greater_than_10_m);
            return;
        }
        sendFileMessage(filePath);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0){
            if (messageAdapter!=null){
                messageAdapter.stopPlayVoice();
            }
            finish();
        }
        return true;
    }
}
