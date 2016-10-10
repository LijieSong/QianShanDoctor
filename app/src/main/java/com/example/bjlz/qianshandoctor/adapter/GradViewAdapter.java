package com.example.bjlz.qianshandoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.utils.volley.ImageLoaderUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：QianShanDoctor
 * 类描述：GradViewAdapter gradview适配器
 * 创建人：slj
 * 创建时间：2016-6-28 10:49
 * 修改人：slj
 * 修改时间：2016-6-28 10:49
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class GradViewAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, Object>> list;
    private String imgUrl = "http://pics.sc.chinaz.com/Files/pic/icons128/6201/2r.png";
//private String imgUrl = "http://img.youguoquan.com/uploads/magazine/content/77f0ad28fd055c0bb332d24cf182e10b_magazine_web_m.jpg";

    public GradViewAdapter(Context context, List<HashMap<String, Object>> list) {
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
            convertView = View.inflate(context, R.layout.item,null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.textview = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, Object> map = list.get(position);
        int image = (int) map.get("image");
        String text = (String) map.get("text");
//        holder.image.setBackgroundResource(image);
        ImageLoaderUtil.setImageLoader(imgUrl, holder.image,R.drawable.logo,R.drawable.menu);
        holder.textview.setText(text);
        return convertView;
    }
    private class ViewHolder{
        TextView textview;
        ImageView image;
    }
}
