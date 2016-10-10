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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.bean.MsgBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：QianShan
 * 类描述：MessageAdapter 好友列表数据匹配器
 * 创建人：slj
 * 创建时间：2016-8-2 8:53
 * 修改人：slj
 * 修改时间：2016-8-2 8:53
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class MessageAdapter extends RecyclerView.Adapter implements View.OnClickListener,View.OnLongClickListener {
    private Context context;
    private int msgId;
//    private List<String> userNames;
//
//    public MessageAdapter(Context context, List<String> usernames) {
//        this.context = context;
//        this.userNames = usernames;
//    }
    private List<MsgBean> msgBean;
    public MessageAdapter(Context context, List<String> msg , List<String> msgTime) {
        this.context = context;
        msgBean = new ArrayList<MsgBean>();
        for (int i = 0; i < msg.size(); i++) {
            MsgBean bean = new MsgBean(msg.get(i),msgTime.get(i));
            msgBean.add(bean);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_pending_msg, null);
        ViewHolder holder = new ViewHolder(view);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.
//                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        view.setLayoutParams(lp);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
//        holder.tv_pending_msg.setOnClickListener(this);
//        holder.tv_pending_msgTime.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holde = (ViewHolder) holder;
            msgId = position;
//        holde.tv_pending_msg.setText(userNames.get(position));
//        holde.tv_pending_msgTime.setVisibility(View.GONE);
//        holde.itemView.setTag(userNames.get(position));
            holde.tv_pending_msg.setText(msgBean.get(position).getMsg());
            holde.tv_pending_msgTime.setText(msgBean.get(position).getMsgTime());
           holde.itemView.setTag(msgBean.get(position));
    }

    @Override
    public int getItemCount() {
        if (msgBean !=null){
            return msgBean.size();
        } else {
            return  0;
        }
//        if (userNames !=null){
//            return userNames.size();
//        } else {
//            return  0;
//        }
    }
    public void removeMessage(){
            msgBean.remove(msgId);
             notifyDataSetChanged();
    }
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view, (MsgBean) view.getTag());
//            mOnItemClickListener.onItemClick(view, (String) view.getTag());
        }
//    switch(view.getId()){
//        case R.id.tv_pending_msg:
//        case R.id.tv_pending_msgTime:
//        break;
//        }
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerViewLongItemClickListener mOnLongItemClickListener = null;
    @Override
    public boolean onLongClick(View v) {
        if (mOnLongItemClickListener !=null){
            mOnLongItemClickListener.onItemLongClick(v,(MsgBean) v.getTag());
        }
        return true;
    }

    public  interface OnRecyclerViewLongItemClickListener{
        void onItemLongClick(View view, MsgBean data);
    }
    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, MsgBean data);
//        void onItemClick(View view, String data);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void setOnItemLongClickListener(OnRecyclerViewLongItemClickListener listener) {
        this.mOnLongItemClickListener = listener;
    }
    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_pending_msg,tv_pending_msgTime;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_pending_msg = (TextView)itemView.findViewById(R.id.tv_pending_msg);
            tv_pending_msgTime = (TextView)itemView.findViewById(R.id.tv_pending_msgTime);
        }
    }
}
