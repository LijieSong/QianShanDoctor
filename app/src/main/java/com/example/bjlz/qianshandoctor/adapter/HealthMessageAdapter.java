package com.example.bjlz.qianshandoctor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.activity.UserInformationActivity;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.bean.UserManagerBean;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：QianShanDoctor
 * 类描述：HealthMessageAdapter 健康全信息匹配器
 * 创建人：slj
 * 创建时间：2016-6-28 14:45
 * 修改人：slj
 * 修改时间：2016-6-28 14:45
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class HealthMessageAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private Context context;
    private List<UserManagerBean> list;
    private String imgUrl = "http://pics.sc.chinaz.com/Files/pic/icons128/6201/3r.png";
//    private String imgUrl = "http://img.youguoquan.com/uploads/magazine/content/a62e045853ac7237c70a1e989caef932_magazine_web_m.jpg";

    public HealthMessageAdapter(Context context, int[] img , String[] names, String[] zhusu) {
        this.context = context;
        list = new ArrayList<UserManagerBean>();
        for (int i = 0; i < names.length ; i++) {
            UserManagerBean bean = new UserManagerBean(names[i],img[i],zhusu[i]);
            list.add(bean);
        }

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usermanager, null);
        ViewHodler vh=new ViewHodler(view);
        RelativeLayout.LayoutParams lp = new RelativeLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        view.setOnClickListener(this);
//        vh.tv_user_age.setOnClickListener(this);
//        vh.tv_user_sex.setOnClickListener(this);
//        vh.tv_user_name.setOnClickListener(this);
//        vh.image_user_icon.setOnClickListener(this);
//        vh.image_rightBack.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHodler mHodler = (ViewHodler) holder;
        mHodler.tv_user_name.setText(list.get(position).getName());
        mHodler.tv_user_zhu_su.setVisibility(View.GONE);
//        mHodler.tv_user_zhu_su.setText("主诉: " +list.get(position).getZhusu());
        mHodler.tv_user_sex.setText("男");
        mHodler.tv_user_age.setText("40");
//        mHodler.image_user_icon.setBackgroundResource(list.get(position).getImgId());
//        ImageLoaderUtil.setImageRequest(imgUrl,mHodler.image_user_icon);
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.menu)
                .into(mHodler.image_user_icon);
        mHodler.itemView.setTag(list.get(position));

    }

    @Override
    public int getItemCount() {
        if (list !=null){
            return list.size();
        } else {
            return  0;
        }
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (UserManagerBean) v.getTag());
        }
        switch(v.getId()){
            case R.id.image_user_icon:
            case R.id.tv_user_name:
            case R.id.tv_user_age:
                ToastUtil.shortDiyToast(context,"跳转患者详情界面");
                MyApplication.startActivity(context, UserInformationActivity.class);
                break;
            case R.id.image_rightBack:
//                ToastUtil.shortDiyToast(context,"跳转患者详情界面");
//                Bundle bundle = new Bundle();
//                bundle.putString("bean",list.toString());
//                MyApplication.startActivity(context, UserInformationActivity.class,bundle);
                break;
        }
    }


    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, UserManagerBean data);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private class ViewHodler extends RecyclerView.ViewHolder{

        private TextView tv_user_name,tv_user_sex,tv_user_age,tv_user_zhu_su;
        private ImageView image_rightBack,image_user_icon;
        public ViewHodler(View itemView) {
            super(itemView);
            tv_user_name= (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_user_sex= (TextView) itemView.findViewById(R.id.tv_user_sex);
            tv_user_age= (TextView) itemView.findViewById(R.id.tv_user_age);
            tv_user_zhu_su= (TextView) itemView.findViewById(R.id.tv_user_zhu_su);
            image_rightBack= (ImageView) itemView.findViewById(R.id.image_rightBack);
            image_user_icon= (ImageView) itemView.findViewById(R.id.image_user_icon);
        }
    }
}
