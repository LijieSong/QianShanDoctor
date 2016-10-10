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
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：QianShanDoctor
 * 类描述：
 * 创建人：slj
 * 创建时间：2016-8-29 16:43
 * 修改人：slj
 * 修改时间：2016-8-29 16:43
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class TextRecycleViewAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private Context context;
    private List<String> list = new ArrayList<>();

    public static enum ITEM_TYPE {
        ITEM_TYPE_TEXT, ITEM_TYPE_TEXT2
    }

    public TextRecycleViewAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_pending_msg, null);
        view.setOnClickListener(this);
        if (viewType == ITEM_TYPE.ITEM_TYPE_TEXT.ordinal()) {
            view.setBackgroundColor(CommonUtil.getColor(R.color.devide_line));
            TextViewHolder holder = new TextViewHolder(view);
            return holder;
        } else {
            view.setBackgroundColor(CommonUtil.getColor(R.color.gold));
            TextViewHolder2 holder2 = new TextViewHolder2(view);

            return holder2;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder){
                ((TextViewHolder) holder).tv_pending_msg.setText(list.get(position));
            ((TextViewHolder) holder).tv_pending_msgTime.setText("");
            ((TextViewHolder) holder).itemView.setTag(list.get(position));
        }else if (holder instanceof  TextViewHolder2){
                ((TextViewHolder2) holder).tv_pending_msgTime.setText(list.get(position));
            ((TextViewHolder2) holder).tv_pending_msg.setText("");
            ((TextViewHolder2) holder).itemView.setTag(list.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? ITEM_TYPE.ITEM_TYPE_TEXT2.ordinal() : ITEM_TYPE.ITEM_TYPE_TEXT.ordinal();
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 第一个item的viewHolder
     */
    private class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv_pending_msg, tv_pending_msgTime;
        public TextViewHolder(View itemView) {
            super(itemView);
            tv_pending_msg = (TextView) itemView.findViewById(R.id.tv_pending_msg);
            tv_pending_msgTime = (TextView) itemView.findViewById(R.id.tv_pending_msgTime);
        }
    }

    /**
     * 第二个item的viewHolder
     */
    private class TextViewHolder2 extends RecyclerView.ViewHolder {
        TextView tv_pending_msg, tv_pending_msgTime;
        public TextViewHolder2(View itemView) {
            super(itemView);
            tv_pending_msg = (TextView) itemView.findViewById(R.id.tv_pending_msg);
            tv_pending_msgTime = (TextView) itemView.findViewById(R.id.tv_pending_msgTime);
        }
    }
}
