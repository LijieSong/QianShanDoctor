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

package com.example.bjlz.qianshandoctor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.activity.BaiduMapActivity;
import com.example.bjlz.qianshandoctor.activity.QuestionnaireSurveyWebViewActivity;
import com.example.bjlz.qianshandoctor.activity.ShowImageViewActivity;
import com.example.bjlz.qianshandoctor.application.Address;
import com.example.bjlz.qianshandoctor.application.EaseCommonUtils;
import com.example.bjlz.qianshandoctor.application.EaseConstant;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.BitmapAndFilesTools.EaseImageCache;
import com.example.bjlz.qianshandoctor.utils.BitmapAndFilesTools.EaseImageUtils;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.DataUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.LogUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.FileUtils;
import com.hyphenate.util.TextFormater;

import java.io.File;

/**
 * 项目名称：QianShanDoctor
 * 类描述：ChatMessageAdapter 消息匹配器
 * 创建人：slj
 * 创建时间：2016-8-24 9:18
 * 修改人：slj
 * 修改时间：2016-8-24 9:18
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class ChatMessageAdapter extends BaseAdapter {
    private Context context;
    private EMConversation conversation;
    //语音操作对象
    private MediaPlayer mPlayer = null;
    private boolean IsPlay = false;//是否播放了
    private boolean isPlaying = true;//是否播放了
    private String playId;//播放的messageid
    private AnimationDrawable voiceAnimation;
    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_EXPRESSION = 12;
    private static final int MESSAGE_TYPE_RECV_EXPRESSION = 13;

    public ChatMessageAdapter(Context context, EMConversation conversation) {
        this.context = context;
        this.conversation = conversation;
    }

    @Override
    public int getCount() {
        return conversation.getAllMessages().size();
    }

    @Override
    public EMMessage getItem(int position) {
        return conversation.getAllMessages().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = getItem(position);
        if (message == null) {
            return -1;
        }
        if (message.getType() == EMMessage.Type.TXT) {
            if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
                return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_EXPRESSION : MESSAGE_TYPE_SENT_EXPRESSION;
            }
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
        }
        if (message.getType() == EMMessage.Type.IMAGE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

        }
        if (message.getType() == EMMessage.Type.LOCATION) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
        }
        if (message.getType() == EMMessage.Type.VOICE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
        }
        if (message.getType() == EMMessage.Type.VIDEO) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
        }
        if (message.getType() == EMMessage.Type.FILE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EMMessage message = conversation.getAllMessages().get(position);
        final String messageFrom = message.getFrom();
        final String userName = message.getUserName();
        playId = message.getMsgId();
        EMTextMessageBody TextBody = null;
        EMImageMessageBody ImgBody = null;
        EMVoiceMessageBody voiceBody = null;
        EMLocationMessageBody locationBody = null;
        EMVideoMessageBody videoBody =null;
        EMNormalFileMessageBody fileBody = null;
        EMMessageBody body = message.getBody();
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            switch (message.getType()) {
                case TXT:
                    TextBody = (EMTextMessageBody) body;
                    convertView = View.inflate(context,message.direct() == EMMessage.Direct.RECEIVE ?
                            R.layout.item_chart_rec : R.layout.item_chat_send,null);
                    break;
                case LOCATION:
                    locationBody = (EMLocationMessageBody) body;
                    convertView = View.inflate(context,message.direct() == EMMessage.Direct.RECEIVE ?
                            R.layout.ease_row_received_location : R.layout.ease_row_sent_location,null);
                    break;
                case IMAGE:
                    ImgBody = (EMImageMessageBody) body;
                    convertView = View.inflate(context,message.direct() == EMMessage.Direct.RECEIVE ?
                            R.layout.item_chat_img_rec : R.layout.item_chat_img_send,null);
                    break;
                case VOICE:
                    voiceBody = (EMVoiceMessageBody) body;
                    convertView = View.inflate(context,message.direct() == EMMessage.Direct.RECEIVE ?
                            R.layout.ease_row_received_voice : R.layout.ease_row_sent_voice,null);
                    break;
                case FILE:
                    fileBody = (EMNormalFileMessageBody) body;
                    convertView = View.inflate(context,message.direct() == EMMessage.Direct.RECEIVE ?
                            R.layout.ease_row_received_file : R.layout.ease_row_sent_file, null);
                    break;
                case VIDEO:
                    videoBody = (EMVideoMessageBody) body;
                    convertView = View.inflate(context,message.direct() == EMMessage.Direct.RECEIVE ?
                            R.layout.ease_row_received_video : R.layout.ease_row_sent_video, null);
                    break;
                default:
                    break;
            }
            holder.image_chat = (ImageView) convertView.findViewById(R.id.image_chat);
            holder.img_chat_image = (ImageView) convertView.findViewById(R.id.img_chat_image);
            holder.iv_voice = (ImageView) convertView.findViewById(R.id.iv_voice);
            holder.tv_length = (TextView) convertView.findViewById(R.id.tv_length);
            holder.textview_chat = (TextView) convertView.findViewById(R.id.textview_chat);
            holder.tv_chat_message = (TextView) convertView.findViewById(R.id.tv_chat_message);
            holder.tv_msg_time = (TextView) convertView.findViewById(R.id.tv_msg_time);
            holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            holder.tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_name);
            holder.tv_file_size = (TextView) convertView.findViewById(R.id.tv_file_size);
            holder.tv_file_state = (TextView) convertView.findViewById(R.id.tv_file_state);
            holder.bubble = (LinearLayout) convertView.findViewById(R.id.bubble);
            holder.ll_loading = (LinearLayout) convertView.findViewById(R.id.ll_loading);
            holder.percentage = (TextView) convertView.findViewById(R.id.percentage);
            holder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
            holder.tv_delivered = (TextView) convertView.findViewById(R.id.tv_delivered);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_msg_time.setText(DataUtils.transferLongToDate("yyyy-MM-dd HH:mm:ss", message.getMsgTime()));

        switch (message.getType()) {
            case TXT:
                if (TextBody != null)
                    holder.tv_chat_message.setText(TextBody.getMessage());
                break;
            case LOCATION:
                if (locationBody != null) {
                    final double latitude = locationBody.getLatitude();
                    final double longitude = locationBody.getLongitude();
                    final String address = locationBody.getAddress();
                    holder.tv_location.setText(address);
                    holder.tv_location.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, BaiduMapActivity.class);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            intent.putExtra("address", address);
                            context.startActivity(intent);
                        }
                    });
                }
                break;
            case IMAGE:
                if (ImgBody != null) {
                    String thumbPath = ImgBody.thumbnailLocalPath();
                    if (!new File(thumbPath).exists()) {
                        // 兼容旧版SDK收到的thumbnail
                        thumbPath = EaseImageUtils.getThumbnailImagePath(ImgBody.getLocalUrl());
                    }
                    showImageView(thumbPath, holder.img_chat_image, ImgBody.getLocalUrl(), message);
                    final EMImageMessageBody finalImgBody = ImgBody;
                    holder.img_chat_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ShowImageViewActivity.class);
                            File file = new File(finalImgBody.getLocalUrl());
                            if (file.exists()) {
                                Uri uri = Uri.fromFile(file);
                                intent.putExtra("uri", uri);
                            } else {
                                // The local full size pic does not exist yet.
                                // ShowBigImage needs to download it from the server
                                // first
                                intent.putExtra("secret", finalImgBody.getSecret());
                                intent.putExtra("remotepath", finalImgBody.getRemoteUrl());
                                intent.putExtra("localUrl", finalImgBody.getLocalUrl());
                            }
                            if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()
                                    && message.getChatType() == EMMessage.ChatType.Chat) {
                                try {
                                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            context.startActivity(intent);
                        }
                    });
                }
                break;
            case VOICE:
                if (voiceBody != null) {
                    int length = voiceBody.getLength();
                    String time = DataUtils.getFormatDateTime("mm:ss", length);
                    holder.tv_length.setText(time);
                    final EMVoiceMessageBody finalVoiceBody = voiceBody;
                    holder.iv_voice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (IsPlay) {
                                if (playId != null && playId.equals(message.getMsgId())) {
                                    LogUtils.error("停止播放2");
                                    stopPlayVoice();
                                    return;
                                }
                                LogUtils.error("停止播放1");
                                stopPlayVoice();
                            }
                            if (message.direct() == EMMessage.Direct.SEND) {
                                // for sent msg, we will try to play the voice file directly
//                            playVoice(finalVoiceBody.getLocalUrl());
                                LogUtils.error("开始播放1");
                                playVoice(finalVoiceBody.getLocalUrl(),message);
                            } else {
                                if (message.status() == EMMessage.Status.SUCCESS) {
                                    File file = new File(finalVoiceBody.getLocalUrl());
                                    if (file.exists() && file.isFile()){
                                        LogUtils.error("开始播放2");
                                        playVoice(finalVoiceBody.getLocalUrl(), message);
                                    } else
                                        LogUtils.error( "file not exist");

                                } else if (message.status() == EMMessage.Status.INPROGRESS) {
//                                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                                } else if (message.status() == EMMessage.Status.FAIL) {
//                                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                                    new AsyncTask<Void, Void, Void>() {

                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            EMClient.getInstance().chatManager().downloadAttachment(message);
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void result) {
                                            super.onPostExecute(result);
                                            notifyDataSetChanged();
                                        }

                                    }.execute();
                                }
                            }
                        }
                    });
                }
                break;
            case FILE:
                if (fileBody !=null){
                    holder.tv_delivered.setVisibility(View.VISIBLE);
                    holder.ll_loading.setVisibility(View.GONE);
                    String filePath = fileBody.getLocalUrl();
                    holder.tv_file_name.setText(fileBody.getFileName());
                    holder.tv_file_size.setText(TextFormater.getDataSize(fileBody.getFileSize()));
                    if (message.direct() == EMMessage.Direct.RECEIVE) {
                        holder.ll_loading.setVisibility(View.GONE);
                        holder.tv_ack.setVisibility(View.VISIBLE);
                        File file = new File(filePath);
                        if (file.exists()) {
                            holder.tv_file_state.setText(R.string.Have_downloaded);
                        } else {
                            holder.tv_file_state.setText(R.string.Did_not_download);
                        }
                    }
                    final EMNormalFileMessageBody finalFileBody = fileBody;
                    holder.bubble.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String filePath = finalFileBody.getLocalUrl();
                            File file = new File(filePath);
                            if (file.exists()) {
                                // open files if it exist
                                FileUtils.openFile(file, (Activity) context);
                            } else {
                                // download the file
                                context.startActivity(new Intent(context, ShowImageViewActivity.class).putExtra("msgbody", message.getBody()));
                            }
                            if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
                                try {
                                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
                break;
            case VIDEO:

                break;
            default:
                break;
        }
        if (userName != null)
            holder.textview_chat.setText(userName);
        else
            holder.textview_chat.setText(messageFrom);
            holder.image_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "个人资料");
                bundle.putString("url", Address.TEXT_URL4);
                MyApplication.startActivity(context, QuestionnaireSurveyWebViewActivity.class, bundle);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView image_chat, img_chat_image, iv_voice;
        TextView textview_chat, tv_chat_message, tv_msg_time,
                tv_location, tv_length,tv_file_name,tv_file_size,
                tv_file_state,percentage,tv_ack,tv_delivered;
        LinearLayout bubble,ll_loading;
    }

    /**
     * load image into image view
     *
     * @param thumbernailPath
     * @param iv
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath, final EMMessage message) {
        final EMImageMessageBody body = (EMImageMessageBody) message.getBody();
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
            return true;
        } else {
            new AsyncTask<Object, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    if (file.exists()) {
                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 160, 160);
                    } else if (new File(body.thumbnailLocalPath()).exists()) {
                        return EaseImageUtils.decodeScaleImage(body.thumbnailLocalPath(), 160, 160);
                    } else {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                                return EaseImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        iv.setImageBitmap(image);
                        EaseImageCache.getInstance().put(thumbernailPath, image);
                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (EaseCommonUtils.isNetWorkConnected(context)) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        EMClient.getInstance().chatManager().downloadThumbnail(message);
                                    }
                                }).start();
                            }
                        }

                    }
                }
            }.execute();

            return true;
        }
    }
    public void stopPlayVoice() {
        // stop play voice
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        IsPlay = false;
        playId = null;
        notifyDataSetChanged();
    }

    public void playVoice(String filePath,EMMessage message) {
        if (!(new File(filePath).exists())) {
            return;
        }
        playId = message.getMsgId();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        mPlayer = new MediaPlayer();
        if (isPlaying) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayer.release();
                    mPlayer = null;
                    stopPlayVoice(); // stop animation
                }
            });
            IsPlay = true;
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.error("playingError:"+e.getMessage());
        }
    }
}
