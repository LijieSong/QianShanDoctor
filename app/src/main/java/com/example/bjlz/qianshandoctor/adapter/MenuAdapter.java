package com.example.bjlz.qianshandoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bjlz.qianshandoctor.R;

import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：QianShanDoctor
 * 类描述：MenuAdapter 数据适配器
 * 创建人：slj
 * 创建时间：2016-6-27 21:14
 * 修改人：slj
 * 修改时间：2016-6-27 21:14
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class MenuAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, Object>> list;
//    private String imgUrl = "http://img.youguoquan.com/uploads/magazine/content/a62e045853ac7237c70a1e989caef932_magazine_web_m.jpg";
private String imgUrl = "http://pics.sc.chinaz.com/Files/pic/icons128/6201/3r.png";
    public MenuAdapter(Context context, List<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
         convertView = View.inflate(context,R.layout.view_item_test,null);
            holder = new ViewHolder();
            holder.image_icon = (ImageView) convertView.findViewById(R.id.image_icon);
            holder.text_menu = (TextView) convertView.findViewById(R.id.text_menu);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, Object> map = list.get(position);
        int image = (int) map.get("image");
        String text = (String) map.get("text");
//        holder.image_icon.setBackgroundResource(image);
//        Glide.with(context).load(imgUrl).into(holder.image_icon);
        Glide.with(context)
                .load(imgUrl)
                .centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.menu)
                .crossFade()
                .into(holder.image_icon);

//        ImageLoaderUtil.setImageLoader(imgUrl,holder.image_icon,R.drawable.logo,R.drawable.menu);
            holder.text_menu.setText(text);
        return convertView;
    }
    private class ViewHolder{
        TextView text_menu;
        ImageView image_icon;
    }
}