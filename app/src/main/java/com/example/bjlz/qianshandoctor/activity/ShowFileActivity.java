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
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.util.FileUtils;
import com.jtech.view.RecyclerHolder;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：QianShanDoctor
 * 类描述：ShowFileActivity 下载文件
 * 创建人：slj
 * 创建时间：2016-9-30 13:21
 * 修改人：slj
 * 修改时间：2016-9-30 13:21
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class ShowFileActivity extends BaseActivity {
    private ProgressBar progressBar;
    private File file;
    private EMFileMessageBody messageBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file);
        MyApplication.getInstance().addActivity(this);
        getData();
        initView();
        initData();
        setOnClick();
    }

    @Override
    public void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void initData() {
        file = new File(messageBody.getLocalUrl());
        //set head map
        final Map<String, String> maps = new HashMap<String, String>();
        if (!TextUtils.isEmpty(messageBody.getSecret())) {
            maps.put("share-secret", messageBody.getSecret());
        }
        //download file
        EMClient.getInstance().chatManager().downloadFile(messageBody.getRemoteUrl(), messageBody.getLocalUrl(), maps,
                new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                FileUtils.openFile(file, ShowFileActivity.this);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onProgress(final int progress,String status) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progress);
                            }
                        });
                    }

                    @Override
                    public void onError(int error, final String msg) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if(file != null && file.exists()&&file.isFile())
                                    file.delete();
                                String str4 = getResources().getString(R.string.Failed_to_download_file);
                                ToastUtil.shortToast(ShowFileActivity.this,str4+msg);
                                finish();
                            }
                        });
                    }
                });
    }

    @Override
    public void getData() {
        messageBody = getIntent().getParcelableExtra("msgbody");
    }

    @Override
    public void setOnClick() {

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
}
